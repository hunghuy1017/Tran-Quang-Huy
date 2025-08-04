package dal;

import models.Classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO extends DBContext {

    /**
     * Lấy tất cả lớp học
     */
    public List<Classes> getAllClasses() {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT * FROM Classes";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapToClass(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy lớp học theo trainer
     */
    public List<Classes> getClassesByTrainerID(int trainerID) {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT c.*, "
                + "(SELECT COUNT(*) FROM TrainerBookings tb WHERE tb.ClassID = c.ClassID) AS EnrolledCount "
                + "FROM Classes c WHERE c.TrainerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trainerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Classes c = mapToClass(rs);
                c.setEnrolledCount(rs.getInt("EnrolledCount")); // ✅ Set từ subquery
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Mapping ResultSet -> Classes object
     */
    private Classes mapToClass(ResultSet rs) throws SQLException {
        Classes c = new Classes();
        c.setClassID(rs.getInt("ClassID"));
        c.setTrainerID(rs.getInt("TrainerID"));
        c.setPoolID(rs.getInt("PoolID"));
        c.setClassName(rs.getString("ClassName"));
        c.setClassDate(rs.getDate("ClassDate"));
        c.setStartTime(rs.getTime("StartTime"));
        c.setEndTime(rs.getTime("EndTime"));
        c.setMaxParticipants(rs.getInt("MaxParticipants"));
        c.setDescription(rs.getString("Description"));
        c.setPrice(rs.getBigDecimal("Price")); // ✅
        return c;
    }

}
