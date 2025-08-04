package dal;

import java.sql.*;
import models.Employee;

public class EmployeeDAO {

    private final DBContext db = new DBContext();

    // Cập nhật mô tả của nhân viên (Description)
    public void updateEmployee(Employee emp) {
        String sql = "UPDATE Employee SET Description = ? WHERE UserID = ?";
        try (PreparedStatement ps = db.connection.prepareStatement(sql)) {
            ps.setString(1, emp.getDescription());
            ps.setInt(2, emp.getUserID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy thông tin nhân viên theo userID
    public Employee getEmployeeById(int userId) {
        String sql = "SELECT u.UserID, u.FullName, u.Email, u.Phone, u.Address, u.Image, "
                + "e.Description, e.StartDate, e.EndDate, e.Attendance, e.Salary, "
                + "e.PoolID, sp.Name AS PoolName "
                + "FROM Users u "
                + "JOIN Employee e ON u.UserID = e.UserID "
                + "LEFT JOIN SwimmingPools sp ON e.PoolID = sp.PoolID "
                + "WHERE u.UserID = ?";
        try (PreparedStatement ps = db.connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setUserID(rs.getInt("UserID"));
                    emp.setFullName(rs.getString("FullName"));
                    emp.setEmail(rs.getString("Email"));
                    emp.setPhone(rs.getString("Phone"));
                    emp.setAddress(rs.getString("Address"));
                    emp.setImage(rs.getString("Image"));
                    emp.setDescription(rs.getString("Description"));
                    emp.setStartDate(rs.getDate("StartDate"));
                    emp.setEndDate(rs.getDate("EndDate"));
                    emp.setAttendance(rs.getInt("Attendance"));
                    emp.setSalary(rs.getBigDecimal("Salary"));
                    emp.setPoolID(rs.getInt("PoolID"));
                    emp.setPoolName(rs.getString("PoolName"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Lấy tên bể bơi mà nhân viên đang làm việc
    public String getPoolNameByUserId(int userId) {
        String sql = "SELECT sp.PoolName "
                + "FROM Employee e "
                + "JOIN SwimmingPools sp ON e.PoolID = sp.PoolID "
                + "WHERE e.UserID = ?";
        try (PreparedStatement ps = db.connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PoolName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPositionByUserId(int userId) {
        String sql = "SELECT Position FROM Employee WHERE UserID = ?";
        try (PreparedStatement ps = db.connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Position");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
