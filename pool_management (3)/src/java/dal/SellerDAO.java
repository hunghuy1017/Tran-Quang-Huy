package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Product;
import models.Category;
import models.Order;
import models.OrderDetail;

public class SellerDAO extends DBContext {
    public List<Product> getProductsWithSearchAndPagination(int poolID, String searchTerm, int categoryID, int offset, int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT ProductID, ProductName, Price, Quantity, Image, PoolID, Status " +
                     "FROM Product WHERE PoolID = ? AND Status = 1 " +
                     "AND ProductName COLLATE Latin1_General_CI_AI LIKE ? " +
                     (categoryID > 0 ? "AND CategoryID = ? " : "") +
                     "ORDER BY ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, poolID);
            stmt.setString(2, "%" + (searchTerm != null ? searchTerm : "") + "%");
            int paramIndex = 3;
            if (categoryID > 0) {
                stmt.setInt(paramIndex++, categoryID);
            }
            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("ProductID"),
                    rs.getInt("PoolID"),
                    rs.getString("ProductName"),
                    rs.getDouble("Price"),
                    rs.getInt("Quantity"),
                    rs.getString("Image"),                  
                    rs.getBoolean("Status")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public int getTotalProducts(int poolID, String searchTerm, int categoryID) {
        int total = 0;
        String sql = "SELECT COUNT(*) as total FROM Product WHERE PoolID = ? AND Status = 1 " +
                     "AND ProductName COLLATE Latin1_General_CI_AI LIKE ? " +
                     (categoryID > 0 ? "AND CategoryID = ? " : "");
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, poolID);
            stmt.setString(2, "%" + (searchTerm != null ? searchTerm : "") + "%");
            if (categoryID > 0) {
                stmt.setInt(3, categoryID);
            }
                  ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName FROM Categories";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Product getProductByID(int productID) {
        Product product = null;
        String sql = "SELECT ProductID, ProductName, Price, Quantity, Image, PoolID, Status " +
                     "FROM Product WHERE ProductID = ? AND Status = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product = new Product(
                    rs.getInt("ProductID"),
                    rs.getInt("PoolID"),
                    rs.getString("ProductName"),
                    rs.getDouble("Price"),
                    rs.getInt("Quantity"),
                    rs.getString("Image"),                  
                    rs.getBoolean("Status")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public int getOrCreatePendingOrder(int userID) {
        int orderID = -1;
        String selectSql = "SELECT OrderID FROM [Order] WHERE UserID = ? AND Status = 0";
        String insertSql = "INSERT INTO [Order] (UserID, Total, OrderDate, Status) VALUES (?, 0, GETDATE(), 0)";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setInt(1, userID);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                orderID = rs.getInt("OrderID");
            }
            rs.close();
            if (orderID == -1) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setInt(1, userID);
                    insertStmt.executeUpdate();
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    }
                    generatedKeys.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderID;
    }

    public void addToOrderDetails(int orderID, int productID, double price, int quantity) {
        Product product = getProductByID(productID);
        if (product == null || product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ hoặc sản phẩm không tồn tại.");
        }
        String selectSql = "SELECT detailID, quantity FROM OrderDetails WHERE OrderID = ? AND ProductID = ?";
        String updateSql = "UPDATE OrderDetails SET quantity = ?, price = ? WHERE detailID = ?";
        String insertSql = "INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES (?, ?, 1, ?, ?)";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setInt(1, orderID);
            selectStmt.setInt(2, productID);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int detailID = rs.getInt("detailID");
                int existingQuantity = rs.getInt("quantity");
                if (product.getQuantity() < existingQuantity + quantity) {
                    throw new IllegalArgumentException("Số lượng tồn kho không đủ.");
                }
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, existingQuantity + quantity);
                    updateStmt.setDouble(2, price);
                    updateStmt.setInt(3, detailID);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setDouble(1, price);
                    insertStmt.setInt(2, quantity);
                    insertStmt.setInt(3, orderID);
                    insertStmt.setInt(4, productID);
                    insertStmt.executeUpdate();
                }
            }
            rs.close();
            updateOrderTotal(orderID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Lỗi khi thêm sản phẩm vào giỏ hàng.");
        }
    }

    public void decreaseOrderDetailQuantity(int orderID, int productID) {
        String sql = "UPDATE OrderDetails SET quantity = quantity - 1 WHERE OrderID = ? AND ProductID = ? AND quantity > 1";
        String deleteSql = "DELETE FROM OrderDetails WHERE OrderID = ? AND ProductID = ? AND quantity = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            stmt.setInt(2, productID);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, orderID);
                    deleteStmt.setInt(2, productID);
                    deleteStmt.executeUpdate();
                }
            }
            updateOrderTotal(orderID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OrderDetail> getOrderDetails(int orderID) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT od.detailID, od.price, od.quantity, od.Status, od.ProductID, p.ProductName, p.Image " +
                     "FROM OrderDetails od JOIN Product p ON od.ProductID = p.ProductID WHERE od.OrderID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                    rs.getInt("detailID"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getBoolean("Status"),
                    rs.getInt("ProductID"),
                    orderID
                );
                detail.setProductName(rs.getString("ProductName"));
                detail.setProductImage(rs.getString("Image"));
                details.add(detail);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public int getCartItemCount(int orderID) {
        int count = 0;
        String sql = "SELECT SUM(quantity) as totalItems FROM OrderDetails WHERE OrderID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("totalItems");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void removeFromOrderDetails(int orderID, int productID) {
        String sql = "DELETE FROM OrderDetails WHERE OrderID = ? AND ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            stmt.setInt(2, productID);
            stmt.executeUpdate();
            updateOrderTotal(orderID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderTotal(int orderID) {
        String sql = "UPDATE [Order] SET Total = (SELECT SUM(price * quantity) FROM OrderDetails WHERE OrderID = ?) WHERE OrderID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            stmt.setInt(2, orderID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order getOrderByID(int orderID) {
        Order order = null;
        String sql = "SELECT OrderID, UserID, Total, OrderDate, Status FROM [Order] WHERE OrderID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = new Order(
                    rs.getInt("OrderID"),
                    rs.getInt("UserID"),
                    rs.getInt("Total"),
                    rs.getTimestamp("OrderDate"),
                    rs.getBoolean("Status")
                );
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public void completeOrder(int orderID) {
        String sql = "UPDATE [Order] SET Status = 1 WHERE OrderID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductQuantity(int productID, int quantity) {
        String sql = "UPDATE Product SET Quantity = Quantity - ? WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPoolIDFromUserID(int userID) {
        String sql = "SELECT PoolID FROM Employee WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("PoolID");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}