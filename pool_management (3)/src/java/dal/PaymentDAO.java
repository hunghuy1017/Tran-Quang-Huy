/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import com.sun.jdi.connect.spi.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Payments;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.dbcp.dbcp2.SQLExceptionList;
/**
 *
 * @author LENOVO
 */
public class PaymentDAO extends DBContext{
    public int insertPayment(Payments p) {
    String sql = "INSERT INTO Payments (UserID, PackageID, PoolID, PaymentMethod, Total, Status, PaymentTime) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement ptm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ptm.setInt(1, p.getUserID());
        ptm.setInt(2, p.getPackageID());
        ptm.setInt(3, p.getPoolID());
        ptm.setString(4, p.getPaymentMethod());
        ptm.setDouble(5, p.getTotal());
        ptm.setString(6, p.getStatus());
       ptm.setTimestamp(7, new java.sql.Timestamp(p.getPaymentTime().getTime()));

        int affectedRows = ptm.executeUpdate();
        if (affectedRows == 0) {
        throw new Exception("create payment fall");
        }
            try(ResultSet rs = ptm.getGeneratedKeys()){
            if(rs.next()){
            return rs.getInt(1);
            }else{
                throw  new Exception("create payment fall , no id");
            }
          }
        
    } catch (Exception e) {
        System.out.println("Insert Payment Error: " + e.getMessage());
    }
    return -1;
}
public int updatePayment(Payments p) {
    String sql = "UPDATE Payments SET "
               + "UserID = ?, "
               + "PackageID = ?, "
               + "PoolID = ?, "
               + "PaymentMethod = ?, "
               + "Total = ?, "
               + "Status = ?, "
               + "PaymentTime = ? "
               + "WHERE PaymentID = ?";
    try {
        PreparedStatement ptm = connection.prepareStatement(sql);
        ptm.setInt(1, p.getUserID());
        ptm.setInt(2, p.getPackageID());
        ptm.setInt(3, p.getPoolID());
        ptm.setString(4, p.getPaymentMethod());
        ptm.setDouble(5, p.getTotal());
        ptm.setString(6, p.getStatus());
      ptm.setTimestamp(7, new java.sql.Timestamp(p.getPaymentTime().getTime()));

        ptm.setInt(8, p.getPaymentID());

        return ptm.executeUpdate(); // Trả về số dòng đã update
    } catch (Exception e) {
        System.out.println("Update Payment Error: " + e.getMessage());
    }
    return -1;
}
public boolean updateStatusOnly(Payments p) {
    String sql = "UPDATE Payments SET Status = ? WHERE PaymentID = ?";
    try {
        PreparedStatement ptm = connection.prepareStatement(sql);
        ptm.setString(1, p.getStatus());
        ptm.setInt(2, p.getPaymentID());
        return ptm.executeUpdate() > 0;
    } catch (Exception e) {
        System.out.println("Update Status Error: " + e.getMessage());
    }
    return false;
}
 public List<Payments> getPaymentsByUserId(int userId) {
        List<Payments> list = new ArrayList<>();
        String sql = "SELECT p.PaymentID, p.PaymentTime, p.PackageID, p.Total, p.PaymentMethod, p.Status " +
                     "FROM Payments p JOIN Users u ON u.UserID = p.UserID " +
                     "WHERE p.UserID = ? AND p.Status = 'Completed'";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, userId);
            ResultSet rs = ptm.executeQuery();

            while (rs.next()) {
                Payments p = new Payments();
                p.setPaymentID(rs.getInt("PaymentID"));
                p.setPaymentTime(rs.getTimestamp("PaymentTime"));
                p.setPackageID(rs.getInt("PackageID"));
                p.setTotal(rs.getDouble("Total"));
                p.setPaymentMethod(rs.getString("PaymentMethod"));
                p.setStatus(rs.getString("Status"));

                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("Get Payments By UserID Error: " + e.getMessage());
        }

        return list;
    }
 public List<Payments> getPaymentsByUserIdAndDateRange(int userId, String startDate, String endDate) {
    List<Payments> list = new ArrayList<>();
    String sql = "SELECT p.PaymentID, p.PaymentTime, p.PackageID, p.Total, p.PaymentMethod, p.Status " +
                 "FROM Payments p JOIN Users u ON u.UserID = p.UserID " +
                 "WHERE p.UserID = ? AND p.Status = 'Completed' " +
                 "AND p.PaymentTime BETWEEN ? AND ? " +
                 "ORDER BY p.PaymentTime DESC";

    try (PreparedStatement ptm = connection.prepareStatement(sql)) {
        ptm.setInt(1, userId);
        ptm.setString(2, startDate + " 00:00:00"); // Bắt đầu ngày
        ptm.setString(3, endDate + " 23:59:59");   // Kết thúc ngày

        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            Payments p = new Payments();
            p.setPaymentID(rs.getInt("PaymentID"));
            p.setPaymentTime(rs.getTimestamp("PaymentTime"));
            p.setPackageID(rs.getInt("PackageID"));
            p.setTotal(rs.getDouble("Total"));
            p.setPaymentMethod(rs.getString("PaymentMethod"));
            p.setStatus(rs.getString("Status"));
            list.add(p);
        }
    } catch (Exception e) {
        System.out.println("Get Payments By Date Range Error: " + e.getMessage());
    }

    return list;
}

    public static void main(String[] args) {
            PaymentDAO dao = new PaymentDAO();
    Payments p = new Payments();

   List<Payments> p1 = dao.getPaymentsByUserId(11);
   for(Payments var:p1){
       System.out.println(var);
   }
    }
    
}
