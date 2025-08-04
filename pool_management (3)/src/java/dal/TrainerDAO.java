package dal;

import models.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import models.TrainerBooking;

public class TrainerDAO extends DBContext {

    // Lấy tổng số trainer (chỉ RoleID = 4 trong bảng Users)
    public int getTotalTrainers() {
        String sql = "SELECT COUNT(*) AS Total FROM Users WHERE RoleID = 4";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("Total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy danh sách trainer (Employee + Users với RoleID = 4)
    public List<Employee> getAllTrainers() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.UserID, e.Description, e.StartDate, e.EndDate, e.Attendance, e.Salary, "
                + "u.FullName, u.Image, u.Email, u.Phone, u.Address "
                + "FROM Employee e "
                + "JOIN Users u ON e.UserID = u.UserID "
                + "WHERE u.RoleID = 4";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setUserID(rs.getInt("UserID"));
                e.setDescription(rs.getString("Description"));
                e.setStartDate(rs.getDate("StartDate"));
                e.setEndDate(rs.getDate("EndDate"));
                e.setAttendance(rs.getInt("Attendance"));
                e.setSalary(rs.getBigDecimal("Salary"));

                // Gán thêm thông tin từ bảng Users
                e.setFullName(rs.getString("FullName"));
                e.setImage(rs.getString("Image"));
                e.setEmail(rs.getString("Email"));
                e.setPhone(rs.getString("Phone"));
                e.setAddress(rs.getString("Address"));

                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }
    public Employee getTrainerByID(int id) {
        String sql = "SELECT u.UserID, u.FullName, u.Email, u.Phone, u.Address, u.Image, "
                + "e.Description, e.StartDate, e.EndDate, e.Attendance, e.Salary, e.Position, e.HourlyRate, "
                + "e.PoolID, sp.Name AS PoolName "
                + "FROM Employee e "
                + "JOIN Users u ON e.UserID = u.UserID "
                + "JOIN SwimmingPools sp ON e.PoolID = sp.PoolID "
                + "WHERE u.UserID = ? AND e.Position = N'Swim trainer'";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
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
                emp.setPosition(rs.getString("Position"));
                emp.setHourlyRate(rs.getBigDecimal("HourlyRate"));
                emp.setPoolID(rs.getInt("PoolID"));
                emp.setPoolName(rs.getString("PoolName"));
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Vector<TrainerBooking> getByUserIDAndTrainerID(int userID, int TrainerID) {
        Vector<TrainerBooking> list = new Vector<>();
        String sql = "SELECT *\n"
                + "  FROM [dbo].[TrainerBookings]\n"
                + "  where UserID =? and TrainerID =?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, TrainerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TrainerBooking up = new TrainerBooking(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getString(7)
                );
                list.add(up);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }
    public Employee getTrainerById1(int id) {
        String sql = "SELECT u.UserID, u.FullName, u.Email, u.Phone, u.Address, u.Image, "
                + "e.Description, e.StartDate, e.EndDate, e.Attendance, e.Salary, e.Position, e.HourlyRate, "
                + "e.PoolID, sp.Name AS PoolName "
                + "FROM Employee e "
                + "JOIN Users u ON e.UserID = u.UserID "
                + "JOIN SwimmingPools sp ON e.PoolID = sp.PoolID "
                + "WHERE u.UserID = ? AND u.RoleID = 4";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
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
                emp.setPosition(rs.getString("Position"));
                emp.setHourlyRate(rs.getBigDecimal("HourlyRate"));
                emp.setPoolID(rs.getInt("PoolID"));
                emp.setPoolName(rs.getString("PoolName"));
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Employee> getTopTrainers(String nameFilter, int limit) { 
        List<Employee> trainers = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT TOP (?) u.UserID, u.FullName, u.Email, " +
            "       COUNT(tb.BookingID) AS BookingCount, " +
            "       MAX(tb.BookingDate) AS LastBookingDate " +
            "FROM TrainerBookings tb " +
            "JOIN Users u ON tb.TrainerID = u.UserID " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        params.add(limit);

        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            sql.append(" AND u.FullName LIKE ? ");
            params.add("%" + nameFilter.trim() + "%");
        }

        sql.append("GROUP BY u.UserID, u.FullName, u.Email " +
                   "ORDER BY BookingCount DESC, LastBookingDate DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) { // Dùng connection từ DBContext
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    ps.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Integer) {
                    ps.setInt(i + 1, (Integer) params.get(i));
                }
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee trainer = new Employee();
                trainer.setUserID(rs.getInt("UserID"));
                trainer.setFullName(rs.getString("FullName"));
                trainer.setEmail(rs.getString("Email"));
                trainer.setBookingCount(rs.getInt("BookingCount"));
                trainer.setLastBookingDate(rs.getDate("LastBookingDate"));
                trainers.add(trainer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainers;
    }
}
