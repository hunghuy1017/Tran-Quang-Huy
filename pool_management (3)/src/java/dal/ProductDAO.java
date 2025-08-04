/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.Vector;
import models.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import models.Employee;

/**
 *
 * @author THIS PC
 */
public class ProductDAO extends DBContext {

    public Vector<Product> getAllProducts(String sql) {
        Vector<Product> list = new Vector<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getTimestamp(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getBoolean(9),
                        rs.getInt(10),
                        rs.getBoolean(11),
                        rs.getDouble(12)
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm mới
    public void insertProduct(Product p) {
        String sql = "INSERT INTO Product "
                + "(PoolID, ProductName, Description, Price, AddedDate, Quantity, Image, Status, CategoryID, IsRentable, RentalPrice) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, p.getPoolID());
            ps.setString(2, p.getProductName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getPrice());
            ps.setTimestamp(5, p.getAddedDate());
            ps.setInt(6, p.getQuantity());
            ps.setString(7, p.getImage());
            ps.setBoolean(8, p.getStatus());
            ps.setInt(9, p.getCategoryID());
            ps.setBoolean(10, p.getIsRentable());
            ps.setDouble(11, p.getRentalPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm sản phẩm theo ID
    public Product getProductByID(int ProductID) {
        String sql = "SELECT * FROM Product WHERE ProductID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ProductID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getTimestamp(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getBoolean(9),
                        rs.getInt(10),
                        rs.getBoolean(11),
                        rs.getDouble(12)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product p) {
        String sql = "UPDATE Product SET PoolID = ?, ProductName = ?, Description = ?, Price = ?, AddedDate = ?, Quantity = ?, Image = ?, Status = ?, CategoryID = ?, IsRentable = ?, RentalPrice = ?\n"
                + "WHERE ProductID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, p.getPoolID());
            ps.setString(2, p.getProductName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getPrice());
            ps.setTimestamp(5, p.getAddedDate());
            ps.setInt(6, p.getQuantity());
            ps.setString(7, p.getImage());
            ps.setBoolean(8, p.getStatus());
            ps.setInt(9, p.getCategoryID());
            ps.setBoolean(10, p.getIsRentable());
            ps.setDouble(11, p.getRentalPrice());
            ps.setInt(12, p.getProductID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeStatus(int ProductID) {
        String sql = "UPDATE [dbo].[Product]\n"
                + "   SET [Status] = 0\n"
                + " WHERE ProductID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, ProductID);
            ptm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int deleteProduct(int ProductID) {
        int result = 0;
        String checkSql = "SELECT COUNT(*) FROM OrderDetails WHERE ProductID = ?";
        String deleteSql = "DELETE FROM [dbo].[Product] WHERE ProductID = ?";

        try {
            // Kiểm tra xem ProductID có xuất hiện trong OrderDetails hay không
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, ProductID);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    // Nếu đã từng có trong OrderDetails, không xóa, chỉ đổi trạng thái
                    changeStatus(ProductID);
                    return 0; // Trả về 0 để biểu thị không xóa mà đổi trạng thái
                }
            }

            // Nếu không tồn tại trong OrderDetails thì xóa
            PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
            deleteStmt.setInt(1, ProductID);
            result = deleteStmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result; // Trả về 1 nếu xóa thành công, 0 nếu không xóa
    }

    public Vector<Product> getProductsByFilter(int poolID, String name, String status, int offset, int pageSize) {
        Vector<Product> list = new Vector<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");//	Là điều kiện luôn đúngSQL sẽ duyệt từng dòng của bảng Product

//Với mỗi dòng, nó kiểm tra điều kiện: 1=1 → luôn đúng ✅
//
//Vì điều kiện luôn đúng, nên mọi dòng đều được lấy ra
        if (poolID > 0) {
            sql.append(" AND PoolID = ").append(poolID);
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND ProductName LIKE ?");
        }
        if (status != null && !status.equals("all")) {
            if (status.equals("for sale")) {
                sql.append(" AND Status = 1");
            } else if (status.equals("stop selling")) {
                sql.append(" AND Status = 0");
            }
        }

        sql.append(" ORDER BY AddedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (name != null && !name.isEmpty()) {
                ps.setString(paramIndex++, "%" + name + "%");
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getTimestamp(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getBoolean(9),
                        rs.getInt(10),
                        rs.getBoolean(11),
                        rs.getDouble(12)
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countProductsByFilter(int poolID, String name, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product WHERE 1=1");

        if (poolID > 0) {
            sql.append(" AND PoolID = ").append(poolID);
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND ProductName LIKE ?");
        }
        if (status != null && !status.equals("all")) {
            if (status.equals("for sale")) {
                sql.append(" AND Status = 1");
            } else if (status.equals("stop selling")) {
                sql.append(" AND Status = 0");
            }
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (name != null && !name.isEmpty()) {
                ps.setString(paramIndex++, "%" + name + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Employee getEmployeeInfoByUserID(int userID) {
        String sql = "SELECT Position, PoolID FROM Employee WHERE UserID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String position = rs.getString("Position");
                int poolID = rs.getInt("PoolID");
                return new Employee( position,poolID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getManagerNameByPoolID(int poolID) {
        String sql = "SELECT u.FullName "
                + "FROM Employee e JOIN Users u ON e.UserID = u.UserID "
                + "WHERE e.Position = N'Warehouse Management' AND e.PoolID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, poolID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("FullName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countWarehouseEmployees(int poolID) {
        String sql = "SELECT COUNT(*) FROM Employee E "
                + "JOIN Users U ON E.UserID = U.UserID "
                + "WHERE E.Position = N'Warehouse Management' ";

        if (poolID != 0) {
            sql += "AND E.PoolID = ?";
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (poolID != 0) {
                ps.setInt(1, poolID);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countProducts(int poolID) {
        String sql = "SELECT COUNT(*) FROM Product WHERE 1=1 ";
        if (poolID != 0) {
            sql += "AND PoolID = ?";
        }
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (poolID != 0) {
                ps.setInt(1, poolID);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
