package dal;

import models.SwimmingPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Event;
import models.Packages;
import models.TrainerBooking;
import models.UserReviews;

public class SwimmingPoolDAO extends DBContext {

    // Lấy danh sách tất cả hồ bơi
    public List<SwimmingPool> getAllPools() {
        List<SwimmingPool> list = new ArrayList<>();
        String sql = "SELECT PoolID, Name, Location, Phone, Fanpage, OpenTime, CloseTime, Description, Status, Image FROM SwimmingPools";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SwimmingPool sp = new SwimmingPool(
                        rs.getInt("PoolID"),
                        rs.getString("Name"),
                        rs.getString("Location"),
                        rs.getString("Phone"),
                        rs.getString("Fanpage"),
                        rs.getTime("OpenTime"),
                        rs.getTime("CloseTime"),
                        rs.getString("Description"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getAllPools: " + e.getMessage());
        }
        return list;
    }

    // Tìm kiếm hồ bơi theo từ khóa và trạng thái
    public List<SwimmingPool> searchPools(String keyword, String status) {
        List<SwimmingPool> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT PoolID, Name, Location, Phone, Fanpage, OpenTime, CloseTime, Description, Status, Image FROM SwimmingPools WHERE 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (Name LIKE ? OR Location LIKE ?)");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ?");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setBoolean(paramIndex, status.equals("active"));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SwimmingPool sp = new SwimmingPool(
                            rs.getInt("PoolID"),
                            rs.getString("Name"),
                            rs.getString("Location"),
                            rs.getString("Phone"),
                            rs.getString("Fanpage"),
                            rs.getTime("OpenTime"),
                            rs.getTime("CloseTime"),
                            rs.getString("Description"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                    list.add(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in searchPools: " + e.getMessage());
        }
        return list;
    }

    // Thêm hồ bơi mới
    public void addPool(SwimmingPool pool) {
        String sql = "INSERT INTO SwimmingPools (Name, Location, Phone, Fanpage, OpenTime, CloseTime, Description, Status, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pool.getName());
            ps.setString(2, pool.getLocation());
            ps.setString(3, pool.getPhone());
            ps.setString(4, pool.getFanpage());
            ps.setTime(5, pool.getOpenTime());
            ps.setTime(6, pool.getCloseTime());
            ps.setString(7, pool.getDescription());
            ps.setBoolean(8, pool.isStatus());
            ps.setString(9, pool.getImage());
            int rows = ps.executeUpdate();
            System.out.println("Added pool: " + pool.getName() + ", Rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in addPool: " + e.getMessage());
        }
    }

    // Cập nhật thông tin hồ bơi
    public void updatePool(SwimmingPool pool) {
        String sql = "UPDATE SwimmingPools SET Name = ?, Location = ?, Phone = ?, Fanpage = ?, OpenTime = ?, CloseTime = ?, Description = ?, Status = ?, Image = ? WHERE PoolID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pool.getName());
            ps.setString(2, pool.getLocation());
            ps.setString(3, pool.getPhone());
            ps.setString(4, pool.getFanpage());
            ps.setTime(5, pool.getOpenTime());
            ps.setTime(6, pool.getCloseTime());
            ps.setString(7, pool.getDescription());
            ps.setBoolean(8, pool.isStatus());
            ps.setString(9, pool.getImage());
            ps.setInt(10, pool.getPoolID());
            int rows = ps.executeUpdate();
            System.out.println("Updated pool ID: " + pool.getPoolID() + ", Rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in updatePool: " + e.getMessage());
        }
    }

    // Xóa hồ bơi
    public void deletePool(int poolID) {
        try {
            // Xóa các bản ghi liên quan trước để tránh lỗi khóa ngoại
            String[] relatedTables = {"Events", "TrainerBookings", "UserReviews", "UserPackages", "PoolPackages"};
            for (String table : relatedTables) {
                String sql = "DELETE FROM " + table + " WHERE PoolID = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, poolID);
                    int rows = ps.executeUpdate();
                    System.out.println("Deleted " + rows + " rows from " + table + " for PoolID: " + poolID);
                }
            }
            // Sau đó xóa hồ bơi
            String sql = "DELETE FROM SwimmingPools WHERE PoolID = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, poolID);
                int rows = ps.executeUpdate();
                System.out.println("Deleted pool ID: " + poolID + ", Rows affected: " + rows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in deletePool: " + e.getMessage());
        }
    }

    // Đổi trạng thái (kích hoạt/hủy kích hoạt)
    public void toggleStatus(int poolID) {
        String sql = "UPDATE SwimmingPools SET Status = NOT Status WHERE PoolID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, poolID);
            int rows = ps.executeUpdate();
            System.out.println("Toggled status for PoolID: " + poolID + ", Rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in toggleStatus: " + e.getMessage());
        }
    }

    // Các phương thức khác giữ nguyên
    public SwimmingPool searchSwimmingPool(int PoolID) {
        String sql = "SELECT PoolID, Name, Location, Phone, Fanpage, OpenTime, CloseTime, Description, Status, Image FROM SwimmingPools WHERE PoolID = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, PoolID);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    return new SwimmingPool(
                            rs.getInt("PoolID"),
                            rs.getString("Name"),
                            rs.getString("Location"),
                            rs.getString("Phone"),
                            rs.getString("Fanpage"),
                            rs.getTime("OpenTime"),
                            rs.getTime("CloseTime"),
                            rs.getString("Description"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Error in searchSwimmingPool: " + ex.getMessage());
        }
        return null;
    }

    public int getTotalPools() {
        String sql = "SELECT COUNT(*) AS Total FROM SwimmingPools";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("Total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getTotalPools: " + e.getMessage());
        }
        return 0;
    }

    public SwimmingPool getPoolById(int id) {
        String sql = "SELECT PoolID, Name, Location, Phone, Fanpage, OpenTime, CloseTime, Description, Status, Image FROM SwimmingPools WHERE PoolID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SwimmingPool(
                            rs.getInt("PoolID"),
                            rs.getString("Name"),
                            rs.getString("Location"),
                            rs.getString("Phone"),
                            rs.getString("Fanpage"),
                            rs.getTime("OpenTime"),
                            rs.getTime("CloseTime"),
                            rs.getString("Description"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getPoolById: " + e.getMessage());
        }
        return null;
    }

    public List<Event> getEventsByPoolId(int poolId) {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT EventID, PoolID, CreatedBy, EventDate, Title, Description, Image FROM Events WHERE PoolID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, poolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Event(
                            rs.getInt("EventID"),
                            rs.getInt("PoolID"),
                            rs.getInt("CreatedBy"),
                            rs.getDate("EventDate"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Image")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getEventsByPoolId: " + e.getMessage());
        }
        return list;
    }
 public List<Packages> getPackagesByPoolId(int poolId) {
        List<Packages> list = new ArrayList<>();
        String sql = "SELECT p.PackageID, p.PackageName, p.DurationInDays, p.Price, p.Description, p.IsActive, p.CreatedAt " +
                     "FROM Package p " +
                     "JOIN PoolPackages pp ON p.PackageID = pp.PackageID " +
                     "WHERE pp.PoolID = ? AND pp.AvailabilityStatus = 1"; // Chỉ lấy các gói khả dụng tại bể bơi đó
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, poolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Packages(
                            rs.getInt("PackageID"),
                            rs.getString("PackageName"),
                            rs.getInt("DurationInDays"),
                            rs.getDouble("Price"),
                            rs.getString("Description"),
                            rs.getBoolean("IsActive"),
                            rs.getDate("CreatedAt")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getPackagesByPoolId: " + e.getMessage());
        }
        return list;
    }
    public List<TrainerBooking> getTrainerBookingsByPoolId(int poolId) {
        List<TrainerBooking> list = new ArrayList<>();
        String sql = "SELECT BookingID, UserID, PoolID, BookingDate, UserName, Note FROM TrainerBookings WHERE PoolID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, poolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TrainerBooking(
                            rs.getInt("BookingID"),
                            rs.getInt("UserID"),
                            rs.getInt("PoolID"),
                            rs.getDate("BookingDate"),
                            rs.getString("UserName"),
                            rs.getString("Note")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getTrainerBookingsByPoolId: " + e.getMessage());
        }
        return list;
    }

    public List<UserReviews> getUserReviewsByPoolId(int poolId) {
        List<UserReviews> list = new ArrayList<>();
        String sql = "SELECT ReviewID, UserID, PoolID, Rating, Comment, CreatedAt FROM UserReviews WHERE PoolID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, poolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new UserReviews(
                            rs.getInt("ReviewID"),
                            rs.getInt("UserID"),
                            rs.getInt("PoolID"),
                            rs.getInt("Rating"),
                            rs.getString("Comment"),
                            rs.getTimestamp("CreatedAt")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error in getUserReviewsByPoolId: " + e.getMessage());
        }
        return list;
    }
}