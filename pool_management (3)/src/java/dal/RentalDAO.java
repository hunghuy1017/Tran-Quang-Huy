package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.ProductRental;

public class RentalDAO extends DBContext {

    // Lấy tất cả đơn thuê theo PoolID với tìm kiếm tùy chọn
    public List<ProductRental> getRentalsByPoolID(int poolID, String searchTerm) {
        List<ProductRental> rentals = new ArrayList<>();
        String sql = "SELECT RentalID, ProductID, PoolID, StartDate, EndDate, CreatedAt, RentalPrice, TotalCost, Status, CustomerName, PhoneNumber " +
                     "FROM ProductRental WHERE PoolID = ? " +
                     "AND (CustomerName LIKE ? OR PhoneNumber LIKE ?) " +
                     "ORDER BY CreatedAt DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, poolID);
            stmt.setString(2, "%" + (searchTerm != null ? searchTerm : "") + "%");
            stmt.setString(3, "%" + (searchTerm != null ? searchTerm : "") + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductRental rental = new ProductRental(
                    rs.getInt("RentalID"),
                    rs.getInt("ProductID"),
                    rs.getInt("PoolID"),
                    rs.getTimestamp("StartDate"),
                    rs.getTimestamp("EndDate"),
                    rs.getDouble("RentalPrice"),
                    rs.getDouble("TotalCost"),
                    rs.getString("Status")
                );
                rental.setCreatedAt(rs.getTimestamp("CreatedAt"));
                rental.setCustomerName(rs.getString("CustomerName"));
                rental.setPhoneNumber(rs.getString("PhoneNumber"));
                rental.setProductName(getProductNameByID(rs.getInt("ProductID")));
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    // Lấy giá thuê (RentalPrice) theo ProductID
    public double getRentalPriceByProductID(int productID) {
        double rentalPrice = 0;
        String sql = "SELECT RentalPrice FROM Product WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rentalPrice = rs.getDouble("RentalPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentalPrice;
    }

    // Tạo đơn thuê mới và liên kết với Order và OrderDetails
    public void createRental(ProductRental rental, int userID) {
        try {
            connection.setAutoCommit(false); // Bắt đầu giao dịch
            // Bước 1: Tạo Order
            String orderSql = "INSERT INTO [Order] (UserID, Total, OrderDate, Status) VALUES (?, ?, GETDATE(), 0)";
            try (PreparedStatement orderStmt = connection.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userID);
                orderStmt.setDouble(2, rental.getTotalCost());
                orderStmt.executeUpdate();

                // Lấy OrderID được tạo
                ResultSet rs = orderStmt.getGeneratedKeys();
                int orderID;
                if (rs.next()) {
                    orderID = rs.getInt(1);
                } else {
                    throw new SQLException("Không thể lấy OrderID.");
                }

                // Bước 2: Tạo OrderDetails
                String orderDetailsSql = "INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement detailsStmt = connection.prepareStatement(orderDetailsSql)) {
                    detailsStmt.setDouble(1, rental.getTotalCost());
                    detailsStmt.setInt(2, 1); // Số lượng là 1 cho một lần thuê
                    detailsStmt.setInt(3, 0); // Status = 0
                    detailsStmt.setInt(4, orderID);
                    detailsStmt.setInt(5, rental.getProductID());
                    detailsStmt.executeUpdate();
                }

                // Bước 3: Tạo ProductRental
                String rentalSql = "INSERT INTO ProductRental (ProductID, PoolID, StartDate, EndDate, RentalPrice, TotalCost, Status, CustomerName, PhoneNumber) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement rentalStmt = connection.prepareStatement(rentalSql)) {
                    rentalStmt.setInt(1, rental.getProductID());
                    rentalStmt.setInt(2, rental.getPoolID());
                    rentalStmt.setTimestamp(3, rental.getStartDate());
                    rentalStmt.setTimestamp(4, rental.getEndDate());
                    rentalStmt.setDouble(5, rental.getRentalPrice());
                    rentalStmt.setDouble(6, rental.getTotalCost());
                    rentalStmt.setString(7, rental.getStatus());
                    rentalStmt.setString(8, rental.getCustomerName());
                    rentalStmt.setString(9, rental.getPhoneNumber());
                    rentalStmt.executeUpdate();
                }
            }
            connection.commit(); // Xác nhận giao dịch
        } catch (SQLException e) {
            try {
                connection.rollback(); // Hoàn tác nếu có lỗi
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            throw new IllegalArgumentException("Lỗi khi tạo đơn thuê đồ: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); // Khôi phục chế độ auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Cập nhật trạng thái đơn thuê
    public void updateRentalStatus(int rentalID, String status) {
        String sql = "UPDATE ProductRental SET Status = ? WHERE RentalID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, rentalID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách sản phẩm có thể thuê theo PoolID
    public List<ProductRental> getAvailableProductsForRental(int poolID) {
        List<ProductRental> products = new ArrayList<>();
        String sql = "SELECT ProductID, ProductName, RentalPrice FROM Product WHERE PoolID = ? AND Status = 1 AND CategoryID = 2 AND IsRentable = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, poolID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductRental product = new ProductRental();
                product.setProductID(rs.getInt("ProductID"));
                product.setProductName(rs.getString("ProductName"));
                product.setRentalPrice(rs.getDouble("RentalPrice"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Lấy tên sản phẩm theo ID
    public String getProductNameByID(int productID) {
        String productName = null;
        String sql = "SELECT ProductName FROM Product WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                productName = rs.getString("ProductName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productName;
    }
}