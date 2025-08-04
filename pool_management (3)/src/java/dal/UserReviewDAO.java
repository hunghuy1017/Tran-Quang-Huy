/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.Vector;
import models.UserReviews;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.TrainerReviews;

/**
 *
 * @author THIS PC
 */
public class UserReviewDAO extends DBContext {

    public Vector<UserReviews> getAllReviews(String sql) {
        Vector<UserReviews> list = new Vector<>();
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                UserReviews r = new UserReviews(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getTimestamp(6),
                        rs.getString(7),
                        rs.getString(8)
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 2. Thêm đánh giá mới
    public void insertReview(UserReviews r) {
        String sql = "INSERT INTO UserReviews (UserID, PoolID, Rating, Comment, CreatedAt) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, r.getUserID());
            ptm.setInt(2, r.getPoolID());
            ptm.setInt(3, r.getRating());
            ptm.setString(4, r.getComment());
            ptm.setTimestamp(5, r.getCreatedAt());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Tìm đánh giá theo ID
    public UserReviews searchReview(int reviewID) {
        String sql = "SELECT * FROM UserReviews WHERE ReviewID = ?";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, reviewID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new UserReviews(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getTimestamp(6),
                        rs.getString(7),
                        rs.getString(8)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 4. Cập nhật đánh giá
    public void updateReview(UserReviews r) {
        String sql = "UPDATE UserReviews SET UserID = ?, PoolID = ?, Rating = ?, Comment = ?, CreatedAt = ? WHERE ReviewID = ?";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, r.getUserID());
            ptm.setInt(2, r.getPoolID());
            ptm.setInt(3, r.getRating());
            ptm.setString(4, r.getComment());
            ptm.setTimestamp(5, r.getCreatedAt());
            ptm.setInt(6, r.getReviewID());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Xoá đánh giá
    public int deleteReview(int reviewID) {
        String sql = "DELETE FROM UserReviews WHERE ReviewID = ?";
        int result = 0;

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, reviewID);
            result = ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Vector<UserReviews> getReviewsByPool(int poolId, int starFilter, int offset, int fetch) {
        Vector<UserReviews> list = new Vector<>();
        String sql = "SELECT ur.*, u.FullName, u.Image "
                + "FROM UserReviews ur "
                + "JOIN Users u ON ur.UserID = u.UserID "
                + "WHERE ur.PoolID = ?";
        if (starFilter > 0) {
            sql += " AND Rating = ?";
        }
        sql += " ORDER BY CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, poolId);
            int paramIndex = 2;
            if (starFilter > 0) {
                ptm.setInt(paramIndex++, starFilter);
            }
            ptm.setInt(paramIndex++, offset);
            ptm.setInt(paramIndex, fetch);

            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                UserReviews r = new UserReviews(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getTimestamp(6),
                        rs.getString(7),
                        rs.getString(8)
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countReviews(int poolId, int starFilter) {
        String sql = "SELECT COUNT(*) FROM UserReviews WHERE PoolID = ?";
        if (starFilter > 0) {
            sql += " AND Rating = ?";
        }

        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, poolId);
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

    public double getAverageRatingByPool(int poolId) {
        double avg = 0;
        String sql = "SELECT AVG(CAST(rating AS FLOAT)) FROM UserReviews WHERE poolID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, poolId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                avg = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avg;
    }
    public List<Map<String, Object>> getAverageRatingByPool() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
        SELECT sp.Name AS poolName, AVG(CAST(ur.Rating AS FLOAT)) AS averageRating
        FROM UserReviews ur
        JOIN SwimmingPools sp ON ur.PoolID = sp.PoolID
        GROUP BY sp.Name
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("poolName", rs.getString("poolName"));
                map.put("averageRating", rs.getDouble("averageRating"));
                result.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Test main
    public static void main(String[] args) {
        UserReviewDAO dao = new UserReviewDAO();
        String sql = "SELECT * FROM UserReviews";
        Vector<UserReviews> list = dao.getAllReviews(sql);
        for (UserReviews r : list) {
            System.out.println(r);
        }
    }
}
