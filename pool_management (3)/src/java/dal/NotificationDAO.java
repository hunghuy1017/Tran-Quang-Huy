/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.DBContext;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.Notifications;

/**
 *
 * @author THIS PC
 */
public class NotificationDAO extends DBContext {

    public Vector<Notifications> getAllNotifications(String sql) {
        Vector<Notifications> list = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Notifications n = new Notifications(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getTimestamp(6)
                );
                list.add(n);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    public List<Notifications> getAllNotifications(int limit) { 
        List<Notifications> notifications = new ArrayList<>();
        String sql = "SELECT TOP (?) NotificationID, UserID, Title, Message, IsRead, CreatedAt FROM Notifications ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifications notif = new Notifications();
                notif.setNotificationID(rs.getInt("NotificationID"));
                notif.setUserID(rs.getInt("UserID")); 
                notif.setTitle(rs.getString("Title"));
                notif.setMessage(rs.getString("Message"));
                notif.setIsRead(rs.getBoolean("IsRead"));
                notif.setCreatedAt(rs.getTimestamp("CreatedAt"));
                notifications.add(notif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    // Thêm thông báo mới
    public void insertNotification(Notifications n) {
        
        String sql = "INSERT INTO Notifications (UserID, Title, Message, IsRead, CreatedAt) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, n.getUserID());
            ptm.setString(2, n.getTitle());
            ptm.setString(3, n.getMessage());
            ptm.setBoolean(4, n.getIsRead());
            ptm.setTimestamp(5, n.getCreatedAt());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm thông báo theo ID
    public Notifications searchNotification(int id) {
        String sql = "SELECT * FROM Notifications WHERE NotificationID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, id);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new Notifications(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getTimestamp(6)
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Cập nhật thông báo
    public void updateNotification(Notifications n) {
        String sql = "UPDATE Notifications SET UserID = ?, Title = ?, Message = ?, IsRead = ?, CreatedAt = ? WHERE NotificationID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, n.getUserID());
            ptm.setString(2, n.getTitle());
            ptm.setString(3, n.getMessage());
            ptm.setBoolean(4, n.getIsRead());
            ptm.setTimestamp(5, n.getCreatedAt());
            ptm.setInt(6, n.getNotificationID());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thay đổi trạng thái IsRead
    public void changeIsRead(int notificationID, boolean isRead) {
        String sql = "UPDATE Notifications SET IsRead = ? WHERE NotificationID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setBoolean(1, isRead);
            ptm.setInt(2, notificationID);
            ptm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xoá thông báo
    public int deleteNotification(int notificationID) {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        int result = 0;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, notificationID);
            result = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Vector<Notifications> getNotificationsByPage(int userId, String title, String status, String sort, int offset, int fetch) {
        Vector<Notifications> list = new Vector<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Notifications WHERE UserID = ? AND Title LIKE ?");

        if (status.equals("read")) {
            sql.append(" AND IsRead = 1");
        } else if (status.equals("unread")) {
            sql.append(" AND IsRead = 0");
        }

        sql.append(" ORDER BY CreatedAt ").append(sort.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            PreparedStatement ptm = connection.prepareStatement(sql.toString());
            ptm.setInt(1, userId);
            ptm.setString(2, "%" + title + "%");
            ptm.setInt(3, offset);
            ptm.setInt(4, fetch);
            ResultSet rs = ptm.executeQuery();

            while (rs.next()) {
                Notifications n = new Notifications(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5),
                        rs.getTimestamp(6)
                );
                list.add(n);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

// Tổng số thông báo (để tính số trang)
    public int countNotifications(int userId, String title, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Notifications WHERE UserID = ? AND Title LIKE ?");
        if (status.equals("read")) {
            sql.append(" AND IsRead = 1");
        } else if (status.equals("unread")) {
            sql.append(" AND IsRead = 0");
        }

        try {
            PreparedStatement ptm = connection.prepareStatement(sql.toString());
            ptm.setInt(1, userId);
            ptm.setString(2, "%" + title + "%");
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public void checkExpiringUserPackagesAndNotify() {
    String sql = "SELECT up.UserID, u.FullName, up.EndDate " +
                 "FROM UserPackages up " +
                 "JOIN Users u ON up.UserID = u.UserID " +
                 "WHERE DATEDIFF(day, GETDATE(), up.EndDate) = 1";

    try {
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("UserID");
            String fullName = rs.getString("FullName");
            Date endDate = rs.getDate("EndDate");

            Notifications n = new Notifications();
            n.setUserID(userId);
            n.setTitle("Your package will expire soon");
            n.setMessage("Hello " + fullName + ", your package will expire on " + endDate + ".");
            n.setIsRead(false);
            n.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            insertNotification(n);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    

    public static void main(String[] args) {
//        int id = 2;
//        String sql = "SELECT * FROM [dbo].[Notifications]\n"
//                + "  where [UserID] =" + id;
        NotificationDAO NDao = new NotificationDAO();
//        Vector<Notifications> list = NDao.getAllNotifications(sql);
//        for (Notifications notifications : list) {
//            System.out.println(notifications);
//        }
     Notifications user = NDao.searchNotification(2);
        System.out.println(user);
    }
}
