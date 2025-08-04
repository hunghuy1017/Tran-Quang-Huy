/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.Vector;
import models.RevenueProduct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import models.RevenuePackage;
import models.RevenuePool;
import models.RevenueTrainerBookings;

/**
 *
 * @author THIS PC
 */
public class RevenueReportDAO extends DBContext {

   
    public Vector<RevenueProduct> getRevenueProduct(String productName, String orderDate, String status, int poolID) {
        Vector<RevenueProduct> list = new Vector<>();
        String sql = "SELECT u.FullName AS userName, "
                + "p.ProductName AS productName, "
                + "od.Quantity AS quantity, "
                + "od.Status AS status, "
                + "(od.Quantity * od.Price) AS total, "
                + "o.OrderDate AS orderDate, "
                + "sp.Name AS swimmingpool "
                + "FROM [Order] o "
                + "JOIN OrderDetails od ON o.OrderID = od.OrderID "
                + "JOIN Product p ON od.ProductID = p.ProductID "
                + "JOIN SwimmingPools sp ON p.PoolID = sp.PoolID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE 1=1 ";

        if (productName != null && !productName.trim().isEmpty()) {
            sql += " AND p.ProductName LIKE ?";
        }
        if (orderDate != null && !orderDate.trim().isEmpty()) {
            sql += " AND CAST(o.OrderDate AS DATE) = ?";
        }
        if (status != null && !status.equals("all")) {
            sql += " AND od.Status = ?";
        }
        if (poolID > 0) {
            sql += " AND p.PoolID = ?";
        }

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            int index = 1;
            if (productName != null && !productName.trim().isEmpty()) {
                ptm.setString(index++, "%" + productName.trim() + "%");
            }
            if (orderDate != null && !orderDate.trim().isEmpty()) {
                ptm.setString(index++, orderDate.trim());
            }
            if (status != null && !status.equals("all")) {
                ptm.setBoolean(index++, Boolean.parseBoolean(status));
            }
            if (poolID > 0) {
                ptm.setInt(index++, poolID);
            }

            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                RevenueProduct info = new RevenueProduct();
                info.setUserName(rs.getString("userName"));
                info.setProductName(rs.getString("productName"));
                info.setQuantity(rs.getInt("quantity"));
                info.setStatus(rs.getBoolean("status"));
                info.setTotal(rs.getDouble("total"));
                info.setOrderDate(rs.getTimestamp("orderDate"));
                info.setSwimmingpool(rs.getString("swimmingpool"));
                list.add(info);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public Vector<RevenuePackage> getRevenuePackage(String name, String paymentTime, int poolID) {
        Vector<RevenuePackage> list = new Vector<>();
        String sql = "SELECT u.FullName AS userName, "
                + "       p.PackageName AS packageName, "
                + "       sp.Name AS swimmingpool, "
                + "       pay.PaymentMethod, "
                + "       pay.Total, "
                + "       pay.Status, "
                + "       pay.PaymentTime "
                + "FROM Payments pay "
                + "JOIN Users u ON pay.UserID = u.UserID "
                + "JOIN Package p ON pay.PackageID = p.PackageID "
                + "JOIN SwimmingPools sp ON pay.PoolID = sp.PoolID "
                + "WHERE 1=1 ";

        if (name != null && !name.trim().isEmpty()) {
            sql += " AND p.PackageName LIKE ?";
        }
        if (paymentTime != null && !paymentTime.trim().isEmpty()) {
            sql += " AND CONVERT(DATE, pay.PaymentTime) = ?";
        }
        if (poolID > 0) {
            sql += " AND pay.PoolID = ?";
        }

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            int index = 1;
            if (name != null && !name.trim().isEmpty()) {
                ptm.setString(index++, "%" + name + "%");
            }
            if (paymentTime != null && !paymentTime.trim().isEmpty()) {
                ptm.setString(index++, paymentTime);
            }
            if (poolID > 0) {
                ptm.setInt(index++, poolID);
            }

            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                RevenuePackage info = new RevenuePackage();
                info.setUserName(rs.getString("userName"));
                info.setPackageName(rs.getString("packageName"));
                info.setSwimmingpool(rs.getString("swimmingpool"));
                info.setPaymentMethod(rs.getString("PaymentMethod"));
                info.setTotal(rs.getDouble("Total"));
                info.setStatus(rs.getString("Status"));
                info.setPaymentTime(rs.getTimestamp("PaymentTime"));
                list.add(info);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    
    public Vector<RevenueTrainerBookings> getRevenueTrainerBookings(String userName, String trainerName, String paymentDate, int poolID) {
        Vector<RevenueTrainerBookings> list = new Vector<>();

        String sql = "SELECT u.FullName AS UserName, t.FullName AS TrainerName, sp.Name AS SwimmingPool, "
                + "tb.Note AS ClassName, tbp.PaymentMethod, tbp.Amount, tbp.PaymentDate "
                + "FROM TrainerBookingPayments tbp "
                + "JOIN TrainerBookings tb ON tb.BookingID = tbp.BookingID "
                + "JOIN Users u ON u.UserID = tb.UserID "
                + "JOIN Users t ON t.UserID = tb.TrainerID "
                + "JOIN SwimmingPools sp ON sp.PoolID = tb.PoolID "
                + "WHERE u.FullName LIKE ? AND t.FullName LIKE ? AND CONVERT(DATE, tbp.PaymentDate) LIKE ?";

        if (poolID > 0) {
            sql += " AND tb.PoolID = ?";
        }

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + userName + "%");
            ptm.setString(2, "%" + trainerName + "%");
            ptm.setString(3, "%" + paymentDate + "%");

            if (poolID > 0) {
                ptm.setInt(4, poolID);
            }

            ResultSet rs = ptm.executeQuery();

            while (rs.next()) {
                list.add(new RevenueTrainerBookings(
                        rs.getString("UserName"),
                        rs.getString("TrainerName"),
                        rs.getString("SwimmingPool"),
                        rs.getString("ClassName"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount"),
                        rs.getTimestamp("PaymentDate")
                ));
            }

            rs.close();
            ptm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

   
    public double getTotalRevenue(int poolID) {
        String sql;

        if (poolID == 0) {
            sql = "SELECT "
                    + "ISNULL((SELECT SUM(o.Total) "
                    + "        FROM [Order] o "
                    + "        WHERE o.Status = 1), 0) + "
                    + "ISNULL((SELECT SUM(p.Total) FROM Payments p WHERE p.Status = N'Completed'), 0) + "
                    + "ISNULL((SELECT SUM(tb.Amount) "
                    + "        FROM TrainerBookingPayments tb "
                    + "        WHERE tb.PaymentStatus = N'Completed'), 0) AS TotalRevenue";
        } else {
            sql = "SELECT "
                    + "ISNULL((SELECT SUM(o.Total) "
                    + "        FROM [Order] o "
                    + "        JOIN OrderDetails od ON o.OrderID = od.OrderID "
                    + "        JOIN Product p ON od.ProductID = p.ProductID "
                    + "        WHERE o.Status = 1 AND p.PoolID = ?), 0) + "
                    + "ISNULL((SELECT SUM(p.Total) FROM Payments p WHERE p.Status = N'Completed' AND p.PoolID = ?), 0) + "
                    + "ISNULL((SELECT SUM(tb.Amount) "
                    + "        FROM TrainerBookingPayments tb "
                    + "        JOIN TrainerBookings b ON tb.BookingID = b.BookingID "
                    + "        WHERE tb.PaymentStatus = N'Completed' AND b.PoolID = ?), 0) AS TotalRevenue";
        }

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            if (poolID > 0) {
                ptm.setInt(1, poolID);
                ptm.setInt(2, poolID);
                ptm.setInt(3, poolID);
            }

            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TotalRevenue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Vector<RevenuePool> getRevenueByPool(String name) {
        Vector<RevenuePool> list = new Vector<>();
        String sql = "SELECT sp.Name AS swimmingpool, "
                + "       ISNULL(o.TotalOrder, 0) + ISNULL(p.TotalPayment, 0) + ISNULL(tbp.TotalTrainerPayment, 0) AS Revenue "
                + "FROM SwimmingPools sp "
                + "LEFT JOIN ("
                + "    SELECT pr.PoolID, SUM(od.price * od.quantity) AS TotalOrder "
                + "    FROM [Order] o "
                + "    JOIN OrderDetails od ON o.OrderID = od.OrderID "
                + "    JOIN Product pr ON od.ProductID = pr.ProductID "
                + "    WHERE o.Status = 1 AND od.Status = 1 "
                + "    GROUP BY pr.PoolID "
                + ") o ON sp.PoolID = o.PoolID "
                + "LEFT JOIN ("
                + "    SELECT PoolID, SUM(Total) AS TotalPayment "
                + "    FROM Payments "
                + "    WHERE Status = N'Completed' "
                + "    GROUP BY PoolID "
                + ") p ON sp.PoolID = p.PoolID "
                + "LEFT JOIN ("
                + "    SELECT tb.PoolID, SUM(tbp.Amount) AS TotalTrainerPayment "
                + "    FROM TrainerBookingPayments tbp "
                + "    JOIN TrainerBookings tb ON tb.BookingID = tbp.BookingID "
                + "    WHERE tbp.PaymentStatus = N'Completed' "
                + "    GROUP BY tb.PoolID "
                + ") tbp ON sp.PoolID = tbp.PoolID "
                + "WHERE sp.Name LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String poolName = rs.getString("swimmingpool");
                double revenue = rs.getDouble("Revenue");
                list.add(new RevenuePool(poolName, revenue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
