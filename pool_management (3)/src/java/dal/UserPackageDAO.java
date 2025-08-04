/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.UserPackage;
import java.sql.SQLException;
import java.util.Vector;
import models.Users;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author LENOVO
 */
public class UserPackageDAO extends DBContext {

    public UserPackage searchLatestUserPackageByUserID(int userID) {
        PreparedStatement ptm = null;
        UserPackage userPackage = null;
        String sql = "SELECT TOP 1 * FROM UserPackages WHERE UserID = ? ORDER BY PurchaseDate DESC";
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, userID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                userPackage = new UserPackage(
                        rs.getInt("UserPackageID"),
                        rs.getInt("UserID"),
                        rs.getInt("PoolID"),
                        rs.getInt("PackageID"),
                        rs.getTimestamp("PurchaseDate"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getBoolean("IsActive"),
                        rs.getString("PaymentStatus")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPackage;
    }

    public UserPackage searchUserPackage(int userID) {
        PreparedStatement ptm = null;
        UserPackage userPackage = null;
        String sql = "select * from  UserPackages \n"
                + "where UserPackageID =?";
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, userID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                userPackage = new UserPackage(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getTimestamp(4),
                        rs.getDate(5),
                        rs.getDate(6),
                        rs.getBoolean(7),
                        rs.getString(8)
                );
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return userPackage;
    }

    public boolean insertUserPackage(UserPackage userPackage) {
        String sql = "INSERT INTO UserPackages (UserID, PoolID, PackageID, PurchaseDate, StartDate, EndDate, IsActive, PaymentStatus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userPackage.getUserID());
            ps.setInt(2, userPackage.getPoolID());
            ps.setInt(3, userPackage.getPackageID());
            ps.setTimestamp(4, userPackage.getPurchaseDate());
            ps.setDate(5, userPackage.getStartDate());
            ps.setDate(6, userPackage.getEndTime());
            ps.setBoolean(7, userPackage.getIsActive());
            ps.setString(8, userPackage.getPaymentStatus());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vector<UserPackage> getByUserIDAndPoolID(int userID, int poolID) {
        Vector<UserPackage> list = new Vector<>();
        String sql = "SELECT * FROM UserPackages WHERE UserID = ? AND PoolID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, poolID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserPackage up = new UserPackage(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getTimestamp(5),
                        rs.getDate(6),
                        rs.getDate(7),
                        rs.getBoolean(8),
                        rs.getString(9)
                );
                list.add(up);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
 public boolean updateOnlyIsActive(int userPackageID, boolean isActive) {
        String sql = "UPDATE UserPackages SET IsActive = 1  WHERE UserPackageID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userPackageID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng bị ảnh hưởng (cập nhật thành công)
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái IsActive cho UserPackageID " + userPackageID + ": " + e.getMessage());
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    /**
     * Cập nhật trạng thái PaymentStatus của một UserPackage.
     *
     * @param userPackageID ID của gói người dùng cần cập nhật.
     * @param paymentStatus Trạng thái thanh toán mới (ví dụ: "Paid", "Pending", "Failed").
     * @return true nếu cập nhật thành công, false nếu có lỗi.
     */
    public boolean updateOnlyPaymentStatus(int userPackageID, String paymentStatus) {
        String sql = "UPDATE UserPackages SET PaymentStatus = 'Completed' WHERE UserPackageID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userPackageID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái PaymentStatus cho UserPackageID " + userPackageID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public UserPackage findPendingUserPackage(int userID, int packageID, Timestamp purchaseTime) {
        UserPackage userPackage = null;
        String sql = "SELECT TOP 1 * FROM UserPackages WHERE UserID = ? AND PackageID = ? AND PurchaseDate = ? AND PaymentStatus = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setInt(2, packageID);
            ps.setTimestamp(3, purchaseTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userPackage = new UserPackage(
                            rs.getInt("UserPackageID"),
                            rs.getInt("UserID"),
                            rs.getInt("PoolID"),
                            rs.getInt("PackageID"),
                            rs.getTimestamp("PurchaseDate"),
                            rs.getDate("StartDate"),
                            rs.getDate("EndDate"),
                            rs.getBoolean("IsActive"),
                            rs.getString("PaymentStatus")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm gói người dùng đang chờ xử lý: " + e.getMessage());
            e.printStackTrace();
        }
        return userPackage;
    }
    
    public boolean updateUserPackageStatus(int userID, int packageID, Timestamp purchaseDate) {
    String sql = "UPDATE UserPackages SET IsActive = 1, PaymentStatus = 'Completed' " +
                 "WHERE UserID = ? AND PackageID = ? AND PurchaseDate = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, userID);
        ps.setInt(2, packageID);
        ps.setTimestamp(3, purchaseDate);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; // Trả về true nếu có ít nhất 1 dòng bị ảnh hưởng
    } catch (SQLException e) {
        System.err.println("Lỗi khi cập nhật UserPackage: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    public static void main(String[] args) {
        
        // Lấy thời gian hiện tại làm startDate
    Date startDate = new Date(System.currentTimeMillis());

    // Tính endDate = startDate + 30 ngày
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    cal.add(Calendar.DAY_OF_MONTH, 30);
    Date endDate = new Date(cal.getTimeInMillis());
    UserPackageDAO userPackageDAO = new UserPackageDAO();
    UserPackage pendingPackage = new UserPackage();
    pendingPackage.setUserID(20);
    pendingPackage.setPoolID(1);
    pendingPackage.setPackageID(1);
    pendingPackage.setPurchaseDate(new java.sql.Timestamp(System.currentTimeMillis())); // nhớ set PurchaseDate
    pendingPackage.setStartDate(startDate);
    pendingPackage.setEndTime(endDate);
    pendingPackage.setIsActive(false);
    pendingPackage.setPaymentStatus("Pending");
    boolean insertPackageSuccess = userPackageDAO.insertUserPackage(pendingPackage);
    System.out.println("Insert thành công? " + insertPackageSuccess);
    }
}
