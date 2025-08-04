package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Roles;

public class RoleDAO extends DBContext {

    public Roles getRoleByID(int roleID) {
        String sql = "SELECT RoleID, RoleName, Status, Description FROM Roles WHERE RoleID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roleID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Roles(
                    rs.getInt("RoleID"),
                    rs.getString("RoleName"),
                    rs.getBoolean("Status"),
                    rs.getString("Description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
