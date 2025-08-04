package dal;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Vector;
import models.Event;
import models.Notifications;
import models.SwimmingPool;

public class EventDAO extends DBContext {

    // Lấy danh sách sự kiện theo trang
    public List<Event> getEventsByPage(int offset, int pageSize) {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Events] ORDER BY eventID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event e = new Event();
                e.setEventID(rs.getInt("eventID"));
                e.setPoolID(rs.getInt("poolID"));
                e.setCreatedBy(rs.getInt("createdBy"));
                e.setEventDate(rs.getDate("eventDate"));
                e.setTitle(rs.getString("title"));
                e.setDescription(rs.getString("description"));
                e.setImage(rs.getString("image"));
                e.setDetailedDescription(rs.getString("detailedDescription"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Tìm kiếm sự kiện theo từ khóa, tháng, năm với phân trang
    public List<Event> searchEventsByKeyword(String keyword, String month, String year, int offset, int pageSize) {
        List<Event> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM [dbo].[Events] WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(title) LIKE ? OR LOWER(description) LIKE ?)");
        }
        if (month != null && !month.isEmpty()) {
            sql.append(" AND MONTH(eventDate) = ?");
        }
        if (year != null && !year.isEmpty()) {
            sql.append(" AND YEAR(eventDate) = ?");
        }
        sql.append(" ORDER BY eventID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword.toLowerCase() + "%");
                ps.setString(paramIndex++, "%" + keyword.toLowerCase() + "%");
            }
            if (month != null && !month.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(month));
            }
            if (year != null && !year.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(year));
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event e = new Event();
                e.setEventID(rs.getInt("eventID"));
                e.setPoolID(rs.getInt("poolID"));
                e.setCreatedBy(rs.getInt("createdBy"));
                e.setEventDate(rs.getDate("eventDate"));
                e.setTitle(rs.getString("title"));
                e.setDescription(rs.getString("description"));
                e.setImage(rs.getString("image"));
                e.setDetailedDescription(rs.getString("detailedDescription"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Lấy tổng số sự kiện (cho phân trang)
    public int getTotalEvents() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM [dbo].[Events]";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return total;
    }

    // Lấy tổng số sự kiện theo tìm kiếm
    public int getTotalSearchEvents(String keyword, String month, String year) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM [dbo].[Events] WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(title) LIKE ? OR LOWER(description) LIKE ?)");
        }
        if (month != null && !month.isEmpty()) {
            sql.append(" AND MONTH(eventDate) = ?");
        }
        if (year != null && !year.isEmpty()) {
            sql.append(" AND YEAR(eventDate) = ?");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword.toLowerCase() + "%");
                ps.setString(paramIndex++, "%" + keyword.toLowerCase() + "%");
            }
            if (month != null && !month.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(month));
            }
            if (year != null && !year.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(year));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return total;
    }

    // Các phương thức khác giữ nguyên
    public Event getEventById(int id) {
        String sql = "SELECT * FROM [dbo].[Events] WHERE eventID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Event(
                        rs.getInt("eventID"),
                        rs.getInt("poolID"),
                        rs.getInt("createdBy"),
                        rs.getDate("eventDate"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("detailedDescription"),
                        rs.getDate("endDate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addEvent(Event e) {
        String sql = "INSERT INTO [dbo].[Events] (poolID, createdBy, eventDate, title, description, image, detailedDescription, EndDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, e.getPoolID());
            ps.setInt(2, e.getCreatedBy());
            ps.setDate(3, e.getEventDate());
            ps.setString(4, e.getTitle());
            ps.setString(5, e.getDescription());
            ps.setString(6, e.getImage());
            ps.setString(7, e.getDetailedDescription());
            ps.setDate(8, e.getendDate());
            ps.executeUpdate();
            
            
            String Usql = "SELECT UserID, FullName FROM Users";
            PreparedStatement userPtm = connection.prepareStatement(Usql);
            ResultSet userRs = userPtm.executeQuery();
            
            while (userRs.next()) {
                int userId = userRs.getInt("UserID");
                String fullName = userRs.getString("FullName");

                NotificationDAO NDao = new NotificationDAO();
                SwimmingPoolDAO SDao =new SwimmingPoolDAO();
                
                SwimmingPool s = SDao.searchSwimmingPool(e.getPoolID());
                
                Notifications n = new Notifications();
                n.setUserID(userId);
                n.setTitle("New event: " + e.getTitle());
                n.setMessage("Hello " + fullName + ", A new event is being held at "+s.getName()+" swimming pool. Hurry up and register to participate in the event to receive many attractive gifts.");
                n.setIsRead(false);
                n.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                NDao.insertNotification(n);}
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }

    public int updateEvent(Event e, int originalCreatedBy) {
        String sql = "UPDATE [dbo].[Events] SET poolID=?, eventDate=?, title=?, description=?, image=?, detailedDescription=?, EndDate=? WHERE eventID=? AND createdBy=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, e.getPoolID());
            ps.setDate(2, e.getEventDate());
            ps.setString(3, e.getTitle());
            ps.setString(4, e.getDescription());
            ps.setString(5, e.getImage());
            ps.setString(6, e.getDetailedDescription());
            ps.setDate(7, e.getendDate());
            ps.setInt(8, e.getEventID());
            ps.setInt(9, originalCreatedBy); // Giữ nguyên CreatedBy từ bản ghi hiện tại
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public boolean deleteEvent(int id) {
        try {
            connection.setAutoCommit(false);
            String sqlDeleteRelated = "DELETE FROM [dbo].[EventRegistrations] WHERE eventID = ?";
            try (PreparedStatement psRelated = connection.prepareStatement(sqlDeleteRelated)) {
                psRelated.setInt(1, id);
                psRelated.executeUpdate();
            }
            String sql = "DELETE FROM [dbo].[Events] WHERE eventID = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                if (connection == null || connection.isClosed()) {
                    throw new SQLException("Kết nối cơ sở dữ liệu không khả dụng");
                }
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();
                connection.commit();
                System.out.println("Xóa sự kiện id=" + id + ", số hàng ảnh hưởng=" + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.err.println("Lỗi khi xóa sự kiện id=" + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM Events";

        try (
                PreparedStatement ps = connection.prepareStatement(sql); // dùng biến connection đã có
                 ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Event e = new Event();
                e.setEventID(rs.getInt("EventID"));
                e.setPoolID(rs.getInt("PoolID"));
                e.setCreatedBy(rs.getInt("CreatedBy"));
                e.setEventDate(rs.getDate("EventDate"));
                e.setTitle(rs.getString("Title"));
                e.setDescription(rs.getString("Description"));
                e.setImage(rs.getString("Image"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Event> getLatestEvents(int limit) {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Events ORDER BY EventDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event e = new Event();
                    e.setEventID(rs.getInt("EventID"));
                    e.setPoolID(rs.getInt("PoolID"));
                    e.setCreatedBy(rs.getInt("CreatedBy"));
                    e.setEventDate(rs.getDate("EventDate"));
                    e.setTitle(rs.getString("Title"));
                    e.setDescription(rs.getString("Description"));
                    e.setImage(rs.getString("Image"));
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean hasApprovedRegistrations(int eventID) {
        String sql = "SELECT COUNT(*) FROM EventRegistrations WHERE EventID = ? AND Status = 'Approved'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args) {
        EventDAO e = new EventDAO();
        Event E = new Event(2, 1, new Date(2025, 1, 20),
   "bala", "rat hay do","jpd","chi tiet su kien", new Date(2025, 1, 30));
        
        e.addEvent(E);
    }
    
}
