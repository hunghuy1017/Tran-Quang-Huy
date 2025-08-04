package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.EventForm;

public class EventFormDAO extends DBContext {

    public List<EventForm> getAllRegistrations() {
        List<EventForm> list = new ArrayList<>();
        String sql = "SELECT * FROM EventRegistrations";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventForm er = new EventForm(
                        rs.getInt("RegistrationID"),
                        rs.getInt("EventID"),
                        rs.getInt("UserID"),
                        rs.getDate("RegisteredAt"),
                        rs.getString("Status"),
                        rs.getString("Note"),
                        rs.getString("FullName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address")
                );
                list.add(er);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertRegistration(EventForm er) {
        String sql = "INSERT INTO EventRegistrations (EventID, UserID, RegisteredAt, Status, Note, FullName, Phone, Email, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, er.getEventID());
            ps.setInt(2, er.getUserID());
            ps.setDate(3, er.getRegisteredAt());
            ps.setString(4, er.getStatus());
            ps.setString(5, er.getNote());
            ps.setString(6, er.getFullName());
            ps.setString(7, er.getPhone());
            ps.setString(8, er.getEmail());
            ps.setString(9, er.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPhoneRegisteredForEvent(int eventID, String phone) {
        String sql = "SELECT * FROM EventRegistrations WHERE EventID = ? AND Phone = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventID);
            ps.setString(2, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRegistrationStatus(int registrationID, String newStatus) {
        String sql = "UPDATE EventRegistrations SET status = ? WHERE registrationID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newStatus);
            ps.setInt(2, registrationID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {

        }
        return false;
    }

    public List<EventForm> getAllRegistrationsFiltered(String searchEvent, String searchPhone, String searchPool, String status) {
    List<EventForm> list = new ArrayList<>();
    String sql = "SELECT er.RegistrationID, er.EventID, e.Title, sp.Name AS PoolName, er.FullName, er.Phone, er.Email, er.Address, er.Note, er.RegisteredAt, er.Status " +
                 "FROM EventRegistrations er " +
                 "JOIN Events e ON er.EventID = e.EventID " +
                 "JOIN SwimmingPools sp ON e.PoolID = sp.PoolID ";
    
    List<Object> params = new ArrayList<>();
    boolean hasCondition = false;

    if (searchEvent != null && !searchEvent.trim().isEmpty()) {
        sql += (hasCondition ? "AND " : "WHERE ") + "e.Title LIKE ? ";
        params.add("%" + searchEvent + "%");
        hasCondition = true;
    }

    if (searchPhone != null && !searchPhone.trim().isEmpty()) {
        sql += (hasCondition ? "AND " : "WHERE ") + "er.Phone LIKE ? ";
        params.add("%" + searchPhone + "%");
        hasCondition = true;
    }

    if (searchPool != null && !searchPool.trim().isEmpty()) {
        sql += (hasCondition ? "AND " : "WHERE ") + "sp.Name LIKE ? ";
        params.add("%" + searchPool + "%");
        hasCondition = true;
    }

    if (status != null && !status.trim().isEmpty()) {
        sql += (hasCondition ? "AND " : "WHERE ") + "er.Status = ? ";
        params.add(status);
        hasCondition = true;
    }

    try {
        if (connection == null) {
            System.err.println("Kết nối cơ sở dữ liệu không được thiết lập!");
            return list;
        }

        System.out.println("SQL Query: " + sql);
        System.out.println("Parameters: " + params);

        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EventForm form = new EventForm();
                form.setRegistrationID(rs.getInt("RegistrationID"));
                form.setEventID(rs.getInt("EventID"));
                form.setFullName(rs.getString("FullName"));
                form.setPhone(rs.getString("Phone"));
                form.setEmail(rs.getString("Email"));
                form.setAddress(rs.getString("Address"));
                form.setNote(rs.getString("Note"));
                form.setStatus(rs.getString("Status"));
                form.setRegisteredAt(rs.getDate("RegisteredAt"));
                form.setEventTitle(rs.getString("Title"));
                form.setPoolName(rs.getString("PoolName"));
                list.add(form);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    public List<EventForm> getRegistrationsByUserId(int userId) {
        List<EventForm> registrations = new ArrayList<>();
        String sql = "SELECT * FROM EventRegistrations WHERE userid = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                EventForm eventForm = new EventForm();
                eventForm.setUserID(rs.getInt("userid"));
                eventForm.setEventID(rs.getInt("eventid"));
                // Set other EventForm properties as needed
                registrations.add(eventForm);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }
        
        return registrations;
    }
    public static void main(String[] args) {
        EventFormDAO dao = new EventFormDAO();
        List<EventForm> list = dao.getAllRegistrationsFiltered("", "", "", "");
        System.out.println("Tổng số bản ghi: " + list.size());
        for (EventForm e : list) {
            System.out.println(e.getFullName() + " | " + e.getPhone());
            
        }
        List<EventForm> list1 = dao.getRegistrationsByUserId(2);
        System.out.println(list1.size());
        for (EventForm eventForm : list1) {
            System.out.println(eventForm.getEventID() +"");
        }
    }

}
