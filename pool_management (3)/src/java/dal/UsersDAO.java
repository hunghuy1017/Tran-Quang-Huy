package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import models.Roles;
import models.Users;

/**
 *
 * @author LENOVO
 */
public class UsersDAO extends DBContext {

    public Vector<Users> getAllUsers(String sql) {
        Vector<Users> list = new Vector<>();
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not open!");
                return list;
            }

            ptm = connection.prepareStatement(sql);
            rs = ptm.executeQuery();

            while (rs.next()) {
                Users u = new Users(
                        rs.getInt(1), // UserID
                        rs.getInt(2), // RoleID
                        rs.getString(3), // FullName
                        rs.getString(4), // Email
                        rs.getString(5), // Password
                        rs.getString(6), // Phone
                        rs.getString(7), // Address
                        rs.getString(8) // Image
                );
                list.add(u);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred in getAllUsers:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ptm != null) {
                    ptm.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources:");
                e.printStackTrace();
            }
        }
        return list;
    }

    public Users checkLoginOfData(String phone, String password) {
        PreparedStatement ptm = null;
        ResultSet rs = null;
        Users user = null;
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not open!");
                return null;
            }

            String sql = "SELECT [UserID], [RoleID], [FullName], [Email], [Password], [Phone], [Address], [Image] FROM [dbo].[Users] WHERE [Phone] = ? AND [Password] = ?";
            ptm = connection.prepareStatement(sql);
            ptm.setString(1, phone);
            ptm.setString(2, password);
            rs = ptm.executeQuery();

            if (rs.next()) {
                user = new Users(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)
                );

                // NEW: Lấy role và gán vào user
                RoleDAO roleDao = new RoleDAO();
                Roles role = roleDao.getRoleByID(user.getRoleID());
                user.setRole(role);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred in checkLoginOfData:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ptm != null) {
                    ptm.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources:");
                e.printStackTrace();
            }
        }
        return user;
    }

    public Users searchUser(String email) {
        PreparedStatement ptm = null;
        ResultSet rs = null;
        Users user = null;
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not open!");
                return null;
            }

            String sql = "SELECT [UserID], [RoleID], [FullName], [Email], [Password], [Phone], [Address], [Image] FROM [dbo].[Users] WHERE [Email] = ?";
            ptm = connection.prepareStatement(sql);
            ptm.setString(1, email);
            rs = ptm.executeQuery();

            if (rs.next()) {
                user = new Users(
                        rs.getInt(1), // UserID
                        rs.getInt(2), // RoleID
                        rs.getString(3), // FullName
                        rs.getString(4), // Email
                        rs.getString(5), // Password
                        rs.getString(6), // Phone
                        rs.getString(7), // Address
                        rs.getString(8) // Image
                );
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred in searchUser:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ptm != null) {
                    ptm.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources:");
                e.printStackTrace();
            }
        }
        return user;
    }

    public void insertUser(Users u) {
        PreparedStatement ptm = null;
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not open!");
                return;
            }

            String sql = "INSERT INTO [dbo].[Users] ([RoleID], [FullName], [Email], [Password], [Phone], [Address], [Image]) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, u.getRoleID());
            ptm.setString(2, u.getFullName());
            ptm.setString(3, u.getEmail());
            ptm.setString(4, u.getPassword());
            ptm.setString(5, u.getPhone());
            ptm.setString(6, u.getAddress());
            ptm.setString(7, u.getImage());
            ptm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred in insertUser:");
            e.printStackTrace();
        } finally {
            try {
                if (ptm != null) {
                    ptm.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources:");
                e.printStackTrace();
            }
        }
    }

    public void updateUser(Users u) {
        String sql = "UPDATE [dbo].[Users]\n"
                + "   SET [RoleID] = ?\n"
                + "      ,[FullName] = ?\n"
                + "      ,[Email] = ?\n"
                + "      ,[Password] = ?\n"
                + "      ,[Phone] = ?\n"
                + "      ,[Image] = ?\n"
                + " WHERE UserID =?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setInt(1, u.getRoleID());
            ptm.setString(2, u.getFullName());
            ptm.setString(3, u.getEmail());
            ptm.setString(4, u.getPassword());
            ptm.setString(5, u.getPhone());

            ptm.setString(6, u.getImage());
            ptm.setInt(7, u.getUserID());
            ptm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user:");
            e.printStackTrace();
        }
    }

    public int deleteUser(String email) {
        PreparedStatement ptm = null;
        int n = 0;
        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not open!");
                return n;
            }

            String sql = "DELETE FROM [dbo].[Users] WHERE [Email] = ?";
            ptm = connection.prepareStatement(sql);
            ptm.setString(1, email);
            n = ptm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred in deleteUser:");
            e.printStackTrace();
        } finally {
            try {
                if (ptm != null) {
                    ptm.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources:");
                e.printStackTrace();
            }
        }
        return n;
    }

    public Users searchTrainerIDOfUser(int userID) {
        Users u = null;
        String sql = "SELECT \n"
                + "tb.UserID,u.FullName,u.Email,u.Phone,u.Address,u.Image\n"
                + "FROM \n"
                + "    TrainerBookings tb\n"
                + "JOIN \n"
                + "    Users u ON tb.TrainerID = u.UserID\n"
                + "WHERE \n"
                + "    tb.UserID = ?;";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, userID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                u = new Users(userID,
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return u;

    }

    public Users searchTrainerIDOfUserByUserID(int userID) {
        Users u = null;
        String sql = "SELECT top 1 u.UserID,u.FullName,u.Email,u.Phone,u.Address,u.Image\n"
                + "FROM TrainerBookings tb JOIN Users u ON tb.TrainerID = u.UserID\n"
                + "WHERE tb.UserID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, userID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                u = new Users(userID,
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return u;

    }

    public void updateUsers(int userID, String fullName, String email, String phone, String address) {
        String sql = "UPDATE Users SET FullName = ?, Email = ?, Phone = ?, Address = ? WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Users getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    user.setAddress(rs.getString("Address"));
                    user.setImage(rs.getString("Image"));
                    user.setRoleID(rs.getInt("RoleID"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Users searchUserByPhone(String phone) {
        String sql = "SELECT [UserID], [RoleID], [FullName], [Email], [Password], [Phone], [Address], [Image] "
                + "FROM [dbo].[Users] WHERE Phone = ?";
        try (PreparedStatement ptm = connection.prepareStatement(sql)) {
            ptm.setString(1, phone);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    return new Users(
                            rs.getInt("UserID"),
                            rs.getInt("RoleID"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("Phone"),
                            rs.getString("Address"),
                            rs.getString("Image")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching user by phone: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Users searchUserByEmail(String email) {
        String sql = "SELECT * FROM [dbo].[Users] WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("userID"),
                        rs.getInt("roleID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("Image")
                );
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public Users searchUserByID(int usersID) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Users]\n"
                + "  where UserID = ?";

        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, usersID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                Users use = new Users(
                        rs.getInt(1), // UserID
                        rs.getInt(2), // RoleID
                        rs.getString(3), // FullName
                        rs.getString(4), // Email
                        rs.getString(5), // Password
                        rs.getString(6), // Phone
                        rs.getString(7), // Address
                        rs.getString(8)); // Image
                return use;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String checkPassword(String password, String confirmPassword) {
            if (password == null || !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)) {
                return "Mật khẩu phải dài ít nhất 8 ký tự, có ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt (@$!%*?&).";
            }

            if (!password.equals(confirmPassword)) {
                return "Mật khẩu và Xác nhận mật khẩu không giống nhau!";
            }

            return "Valid";
        }

        public String CheckFormatEmail(String Email) {
            if (!Email.endsWith("@gmail.com")) {
                return "Định dạng email không đúng(email phải có dang abc@gmail.com.";
            }
            return "Valid";
        }

    public void changePassword(Users u) {
        String sql = "UPDATE [dbo].[Users]\n"
                + "   SET [Password] = ?\n"
                + " WHERE UserID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, u.getPassword());
            ptm.setInt(2, u.getUserID());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public void updateUserImage(int userID, String imagePath) {
        String sql = "UPDATE Users SET Image = ? WHERE UserID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, imagePath);
            ps.setInt(2, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Users getUserByEmail(String email) {
        Users user = null;
        try {
            String sql = "SELECT * FROM Users WHERE email = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                user = new Users();
                user.setUserID(rs.getInt("userID"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setImage(rs.getString("image"));
                user.setRoleID(rs.getInt("roleID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isPhoneExists(String phone, int excludeUserID) {
        String sql = "SELECT COUNT(*) FROM Users WHERE phone = ? AND userID != ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phone);
            ps.setInt(2, excludeUserID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

public static boolean isValidPassword(String password) {
        if (password == null || password.length() <= 8) {
            return false;
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("!@#$%^&*()-_+=<>?/{}[]|\\~`.,:;'\"".contains(String.valueOf(c))) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public static void main(String[] args) {
        UsersDAO userDao = new UsersDAO();
        Users u = userDao.searchTrainerIDOfUserByUserID(5);

        System.out.println(u);

    }
    public int getTotalUsers() { 
        String sql = "SELECT COUNT(UserID) FROM Users";
        try (PreparedStatement ps = connection.prepareStatement(sql); // Dùng connection từ DBContext
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getTotalEmployees() { 
        String sql = "SELECT COUNT(UserID) FROM Users WHERE RoleID = (SELECT RoleID FROM Roles WHERE RoleName = N'Employee')";
        try (PreparedStatement ps = connection.prepareStatement(sql); // Dùng connection từ DBContext
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Users> getFilteredUsers(String name, String date, int limit) { 
        List<Users> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT UserID, RoleID, FullName, Email, Phone, Address, Image, CreatedAt FROM Users WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND FullName LIKE ? ");
            params.add("%" + name.trim() + "%");
        }
        if (date != null && !date.trim().isEmpty()) {
            sql.append(" AND CAST(CreatedAt AS DATE) = ? ");
            params.add(date.trim());
        }
        sql.append(" ORDER BY CreatedAt DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY");
        params.add(limit);

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
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setImage(rs.getString("Image"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public List<Users> getRecentUsers(int limit) { 
        List<Users> users = new ArrayList<>();
        String sql = "SELECT UserID, RoleID, FullName, Email, Phone, Address, Image, CreatedAt FROM Users ORDER BY CreatedAt DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = connection.prepareStatement(sql)) { // Dùng connection từ DBContext
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setImage(rs.getString("Image"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
