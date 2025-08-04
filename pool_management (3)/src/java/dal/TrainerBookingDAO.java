package dal;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

import models.TrainerBooking;

/**
 * DAO phụ trách bảng TrainerBookings
 * Kế thừa DBContext để dùng trực tiếp biến connection
 */
public class TrainerBookingDAO extends DBContext {

    /**
     * Thêm mới 1 booking trainer.
     * @param b          Đối tượng booking đã gán đủ trường
     * @param packageId  Gói học (nếu cần ghi vào bảng UserPackages)
     * @param startDate  Ngày bắt đầu của gói
     */
    public void addBooking(TrainerBooking b, int packageId, java.time.LocalDate startDate) {

        String sql = """
                INSERT INTO TrainerBookings
                (UserID, PoolID, TrainerID, ClassID, BookingDate,
                 StartTime, EndTime, BookPrice, Status, UserName, Note)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt   (1,  b.getUserID());
            ps.setInt   (2,  b.getPoolID());
            ps.setInt   (3,  b.getTrainerID());
            if (b.getClassID() == null) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, b.getClassID());
            }
            ps.setDate  (5,  b.getBookingDate());
            ps.setTime  (6,  b.getStartTime());
            ps.setTime  (7,  b.getEndTime());
            ps.setBigDecimal(8,  b.getBookPrice());
            ps.setString(9,  b.getStatus());
            ps.setString(10, b.getRegisterName());
            ps.setString(11, b.getNote());

            ps.executeUpdate();

            /* ---- (TÙY CHỌN) Ghi luôn UserPackages nếu cần ---- */
            if (packageId > 0) {
                String sqlPkg = """
                    INSERT INTO UserPackages(UserID, PoolID, PackageID,
                                             StartDate, EndDate, IsActive, PaymentStatus)
                    VALUES (?,?,?,?,?,1,N'Pending')
                    """;
                // EndDate = StartDate + Duration
                // --> Bạn JOIN bảng Package để lấy DurationInDays
                try (PreparedStatement p2 = connection.prepareStatement(sqlPkg)) {
                    p2.setInt(1, b.getUserID());
                    p2.setInt(2, b.getPoolID());
                    p2.setInt(3, packageId);
                    p2.setDate(4, java.sql.Date.valueOf(startDate));
                    p2.setDate(5, java.sql.Date.valueOf(startDate.plusDays(getPackageDuration(packageId))));
                    p2.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Insert TrainerBooking failed: " + ex.getMessage(), ex);
        }
    }
    public void insertBooking(int userID, int poolID, int trainerID,
                              Date bookingDate, Time startTime, Time endTime,
                              BigDecimal price, String userName, String note) {
        String sql = "INSERT INTO TrainerBookings (UserID, PoolID, TrainerID, BookingDate, StartTime, EndTime, BookPrice, UserName, Note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, poolID);
            ps.setInt(3, trainerID);
            ps.setDate(4, bookingDate);
            ps.setTime(5, startTime);
            ps.setTime(6, endTime);
            ps.setBigDecimal(7, price);
            ps.setString(8, userName);
            ps.setString(9, note);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Lấy DurationInDays của package */
    private int getPackageDuration(int packageId) throws SQLException {
        String sql = "SELECT DurationInDays FROM Package WHERE PackageID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            var rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
     public void addBooking(TrainerBooking b) {
        String sql = "INSERT INTO TrainerBookings "
                   + "(UserID, TrainerID, PoolID, BookingDate, StartTime, EndTime, BookPrice, Status, UserName, Note) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, b.getUserID());
            ps.setInt(2, b.getTrainerID());
            ps.setInt(3, b.getPoolID());
            ps.setDate(4, b.getBookingDate());
            ps.setTime(5, b.getStartTime());
            ps.setTime(6, b.getEndTime());
            ps.setBigDecimal(7, b.getBookPrice());
            ps.setString(8, b.getStatus());
            ps.setString(9, b.getRegisterName());
            ps.setString(10, b.getNote());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     public int addClassBooking(TrainerBooking booking) {
        String sql = "INSERT INTO TrainerBookings (UserID, PoolID, TrainerID, ClassID, BookingDate, StartTime, EndTime, BookPrice, Status, RegisterName, Note) OUTPUT INSERTED.BookingID VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, booking.getUserID());
            ps.setInt(2, booking.getPoolID());
            ps.setInt(3, booking.getTrainerID());
            ps.setObject(4, booking.getClassID());
            ps.setDate(5, booking.getBookingDate());
            ps.setTime(6, booking.getStartTime());
            ps.setTime(7, booking.getEndTime());
            ps.setBigDecimal(8, booking.getBookPrice());
            ps.setString(9, booking.getStatus());
            ps.setString(10, booking.getRegisterName());
            ps.setString(11, booking.getNote());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
