package dal;

// import models.MonthlyRevenue; // Đã bỏ import này
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List;
import models.MonthlyRevenue;

public class OrderDAO extends DBContext {
    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=YourDatabaseName;encrypt=false;trustServerCertificate=true;";
    private static final String DB_USER = "your_db_username";
    private static final String DB_PASSWORD = "your_db_password";

    static {
        try { Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); }
        catch (ClassNotFoundException e) { e.printStackTrace(); throw new RuntimeException("Lỗi tải JDBC Driver!"); }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    public int getTotalOrders() { // Tổng số đơn hàng
        String sql = "SELECT COUNT(OrderID) FROM [Order]";
        try (PreparedStatement ps = connection.prepareStatement(sql); // Đã sửa: Dùng 'connection' trực tiếp từ DBContext
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTodayRevenue() { // Doanh thu hôm nay
        LocalDate today = LocalDate.now();
        String sql = "SELECT " +
                     "   ISNULL(SUM(o.Total), 0) + " +
                     "   ISNULL(SUM(p.Total), 0) + " +
                     "   ISNULL(SUM(tbp.Amount), 0) " +
                     "FROM [Order] o " +
                     "LEFT JOIN Payments p ON CAST(o.OrderDate AS DATE) = ? AND CAST(p.PaymentTime AS DATE) = ? " +
                     "LEFT JOIN TrainerBookings tbp ON CAST(o.OrderDate AS DATE) = ? AND CAST(tbp.PaymentDate AS DATE) = ? " +
                     "WHERE CAST(o.OrderDate AS DATE) = ?";

        double revenue = 0;
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, today.toString());
            ps.setString(2, today.toString());
            ps.setString(3, today.toString());
            ps.setString(4, today.toString());
            ps.setString(5, today.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) revenue = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); } return revenue;
    }
    public List<MonthlyRevenue> getMonthlyRevenue(int year) {
        List<MonthlyRevenue> revenues = new ArrayList<>();
        String sql = "SELECT " +
                     "    MONTH(CombinedDate) AS Month, " +
                     "    SUM(Amount) AS TotalRevenue " +
                     "FROM " +
                     "( " +
                     "    SELECT OrderDate AS CombinedDate, Total AS Amount FROM [Order] WHERE YEAR(OrderDate) = ? " +
                     "    UNION ALL " +
                     "    SELECT PaymentTime AS CombinedDate, Total AS Amount FROM Payments WHERE YEAR(PaymentTime) = ? " +
                     "    UNION ALL " +
                     "    SELECT PaymentDate AS CombinedDate, Amount FROM TrainerBookingPayments WHERE YEAR(PaymentDate) = ? " +
                     ") AS CombinedRevenue " +
                     "GROUP BY MONTH(CombinedDate) " +
                     "ORDER BY Month";

        try (PreparedStatement ps = connection.prepareStatement(sql)) { // Dùng connection từ DBContext
            ps.setInt(1, year); 
            ps.setInt(2, year); 
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                revenues.add(new MonthlyRevenue(rs.getInt("Month"), rs.getDouble("TotalRevenue"), year));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
        return revenues;
    }

    // Đã bỏ phương thức getMonthlyRevenue() ở đây
}