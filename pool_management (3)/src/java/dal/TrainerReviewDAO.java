/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import models.TrainerReviews;

/**
 *
 * @author THIS PC
 */
public class TrainerReviewDAO extends DBContext {

    public Vector<TrainerReviews> getReviewsByTrainer(int trainerId, int starFilter, int offset, int pageSize) {
        Vector<TrainerReviews> list = new Vector<>();
        String sql = "SELECT tr.*, u.FullName, u.Image "
                + "FROM TrainerReviews tr "
                + "JOIN Users u ON tr.UserID = u.UserID "
                + "WHERE tr.TrainerID = ?";
        if (starFilter > 0) {
            sql += " AND tr.Rating = ?";
        }
        sql += " ORDER BY tr.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            ptm.setInt(paramIndex++, trainerId);
            if (starFilter > 0) {
                ptm.setInt(paramIndex++, starFilter);
            }
            ptm.setInt(paramIndex++, offset);
            ptm.setInt(paramIndex, pageSize);

            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                TrainerReviews review = new TrainerReviews(
                        rs.getInt("ReviewID"),
                        rs.getInt("UserID"),
                        rs.getInt("TrainerID"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("CreatedAt"),
                        rs.getString("FullName"),
                        rs.getString("Image")
                );
                list.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countReviewsByTrainer(int trainerId, int starFilter) {
        String sql = "SELECT COUNT(*) FROM TrainerReviews WHERE TrainerID = ?";
        if (starFilter > 0) {
            sql += " AND Rating = ?";
        }

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, trainerId);
            if (starFilter > 0) {
                ptm.setInt(2, starFilter);
            }

            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getAverageRatingByTrainer(int trainerId) {
        String sql = "SELECT AVG(CAST(Rating AS FLOAT)) FROM TrainerReviews WHERE TrainerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void insertReview(TrainerReviews t) {
        String sql = "INSERT INTO [dbo].[TrainerReviews]\n"
                + "           ([UserID]\n"
                + "           ,[TrainerID]\n"
                + "           ,[Rating]\n"
                + "           ,[Comment]\n"
                + "           ,[CreatedAt])\n"
                + "     VALUES\n"
                + "           (?,?,?,?,?)";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, t.getUserID());
            ptm.setInt(2, t.getTrainerID());
            ptm.setInt(3, t.getRating());
            ptm.setString(4, t.getComment());
            ptm.setTimestamp(5, t.getCreatedAt());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
