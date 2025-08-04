package controllers;

import dal.ProductDAO;
import dal.UsersDAO;
import models.Users;

import java.io.IOException;

import java.util.Vector;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Employee;
import models.GoogleForm;

@WebServlet(name = "ServletUsers", urlPatterns = {"/ServletUsers"})
public class ServletUsers extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ServletUsers.class.getName());

    private UsersDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new UsersDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");

        if (service == null) {
            service = "listUser";
        }

        try {
            switch (service) {
                case "logout":
                    handleLogout(request, response, session);
                    break;
                case "loginUser":
                    handleLoginUser(request, response, session);
                    break;

                case "addUser":
                    handleAddUser(request, response, session);
                    break;
                case "deleteUser":
                    handleDeleteUser(request, response, session);
                    break;
                case "listUser":
                    handleListUser(request, response, session);
                    break;
                case "showUserOrders":
                    handleShowUserOrders(request, response, session);
                    break;
                default:
                    LOGGER.warning("Unknown service: " + service);
                    response.sendRedirect("jsp/index.jsp");
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing request: " + e.getMessage());
            request.setAttribute("mess", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        session.invalidate();
        response.sendRedirect("jsp/LoginUser.jsp");
    }

    private void handleLoginUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        

        if (session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
            return;
        }

        String submit = request.getParameter("submit");
        if (submit == null) {
            request.getRequestDispatcher("jsp/LoginUser.jsp").forward(request, response);
            return;
        }
     
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        
        if (phone == null || password == null || phone.isEmpty() || password.isEmpty()) {
            request.setAttribute("mess", "Vui lòng nhập số điện thoại và mật khẩu!");
            request.getRequestDispatcher("jsp/LoginUser.jsp").forward(request, response);
            return;
        }

        try {
            Users user = dao.checkLoginOfData(phone, password);
            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("userID", user.getUserID());
                session.setAttribute("roleID", user.getRoleID());

                

                int roleID = user.getRoleID();
                switch (roleID) {
                    case 1: // Admin
                        response.sendRedirect("AdminDashboardServlet");
                        break;
                    case 2: // User
                        response.sendRedirect("home");
                        break;
                    case 3: // Employee
                        String position = "";
                        int UserID = (int)session.getAttribute("userID");
                        ProductDAO PDAO = new ProductDAO();
                        Employee employee = PDAO.getEmployeeInfoByUserID(UserID);
                        position = employee.getPosition();
                        if(position.equals("Manager")){
                            response.sendRedirect("jsp/adminDashboard.jsp");
                        }else{
                            response.sendRedirect("EmployeeDashboard");
                        }                       
                        break;

                    case 4: // Trainer
                        response.sendRedirect("EmployeeDashboard");
                        break;
                    default:
                        response.sendRedirect("jsp/LoginUser.jsp?error=invalid_role");
                        break;
                }
            } else {
                request.setAttribute("mess", "Sai số điện thoại hoặc mật khẩu!");
                request.getRequestDispatcher("jsp/LoginUser.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("mess", "Lỗi đăng nhập: " + e.getMessage());
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }

    private void handleAddUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Lấy các tham số từ form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String email = request.getParameter("email"); // Có thể rỗng
        String address = request.getParameter("address"); // Có thể rỗng
        String phone = request.getParameter("phone");

        // Ghép firstName và lastName thành fullName
        String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        if (fullName.trim().isEmpty()) {
            request.setAttribute("mess", "First name and last name are required!");
            request.getRequestDispatcher("jsp/insertUsers.jsp").forward(request, response);
            return;
        }

        if (password == null || phone == null || password.isEmpty() || phone.isEmpty()) {
            request.setAttribute("mess", "Password and phone are required!");
            request.getRequestDispatcher("jsp/insertUsers.jsp").forward(request, response);
            return;
        }

        // Kiểm tra nếu số điện thoại đã tồn tại
        Users existingUser = dao.searchUserByPhone(phone); // Giả sử có phương thức searchUserByPhone
        if (existingUser != null) {
            request.setAttribute("mess", "Số điện thoại đã tồn tại!");
            request.getRequestDispatcher("jsp/insertUsers.jsp").forward(request, response);
            return;
        }
        // Kiểm tra nếu email đã tồn tại
        Users existingUserByEmail = dao.searchUserByEmail(email);
        if (existingUserByEmail != null) {
            // Nếu email đã có, cập nhật thông tin mới (không thay đổi password)
            existingUserByEmail.setFullName(fullName.trim());
            existingUserByEmail.setPhone(phone);
            existingUserByEmail.setPassword(password);
            existingUserByEmail.setAddress(address != null ? address : "");
            dao.updateUser(existingUserByEmail);

            request.setAttribute("mess", "Thông tin người dùng đã được cập nhật!");
            response.sendRedirect("jsp/LoginUser.jsp");
        } else {
            // Nếu email chưa có, tạo người dùng mới
            Users newUser = new Users(0, 2, fullName.trim(), email != null ? email : "", password, phone, address != null ? address : "", "");
            dao.insertUser(newUser);

            request.setAttribute("mess", "Đăng ký thành công!");
            response.sendRedirect("jsp/LoginUser.jsp");
        }
    }
    

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        Users user = (Users) session.getAttribute("user");
        if (user == null || user.getRoleID() != 1) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        String uID = request.getParameter("id");
        if (uID != null && !uID.isEmpty()) {
            dao.deleteUser(uID);
        }
        response.sendRedirect("ServletUsers");
    }

    private void handleListUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        Users user = (Users) session.getAttribute("user");
        if (user == null || user.getRoleID() != 1) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        String sql = "SELECT * FROM [dbo].[tblUsers]";
        String submit = request.getParameter("submit");
        String fullName = request.getParameter("fullName");
        if (fullName == null) {
            fullName = "";
        }

        Vector<Users> list;
        if (submit == null) {
            list = dao.getAllUsers(sql);
        } else {
            list = dao.getAllUsers("SELECT * FROM [dbo].[Users] WHERE fullName LIKE N'%" + fullName + "%'");
        }

        request.setAttribute("data", list);
        request.setAttribute("fullName", fullName);
        request.setAttribute("pageTitle", "User Manager");
        request.setAttribute("tableTitle", "List of Users");
        request.getRequestDispatcher("jsp/Admin.jsp").forward(request, response);
    }

    private void handleShowUserOrders(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("message", "Lỗi: Vui lòng đăng nhập để xem thông tin người dùng.");
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        if (user.getRoleID() != 2) {
            session.setAttribute("message", "Lỗi: Bạn không có quyền truy cập trang này. Chỉ người dùng có vai trò Customer (roleID = 2) mới được phép.");
            response.sendRedirect("jsp/index.jsp");
            return;
        }

        request.getRequestDispatcher("jsp/Users.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing users in BluePool application";
    }
}
