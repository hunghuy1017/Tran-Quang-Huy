package controllers;

import dal.EmployeeDAO;
import dal.UsersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import models.Employee;
import models.Users;

@MultipartConfig
@WebServlet(name = "EmployeeProfile", urlPatterns = {"/EmployeeProfile"})
public class EmployeeProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        // Chỉ cho phép nhân viên (RoleID = 3) truy cập
        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect("home");
            return;
        }

        EmployeeDAO empDAO = new EmployeeDAO();
        Employee employee = empDAO.getEmployeeById(user.getUserID());

        request.setAttribute("user", user);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("jsp/EmployeeProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect("home");
            return;
        }

        int userID = user.getUserID();
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        String mess = null;
        UsersDAO userDAO = new UsersDAO();

        // Regex kiểm tra định dạng số điện thoại Việt Nam
        String phoneRegex = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])[0-9]{7}$";
        if (!phone.matches(phoneRegex)) {
            mess = "Số điện thoại không đúng định dạng.";
        } else if (userDAO.isPhoneExists(phone, userID)) {
            mess = "Số điện thoại đã được sử dụng bởi tài khoản khác.";
        }

        // Regex kiểm tra định dạng email
        String emailRegex = "^[\\w.-]{8,}@(gmail\\.com|hotmail\\.com|fpt\\.edu\\.vn)$";
        Users existingUser = userDAO.getUserByEmail(email);
        if (existingUser != null && existingUser.getUserID() != userID) {
            mess = "Email đã tồn tại, vui lòng chọn email khác.";
        } else if (!email.matches(emailRegex)) {
            mess = "Email không đúng định dạng. Vui lòng dùng gmail.com, hotmail.com hoặc fpt.edu.vn và phần tên trước @ phải có ít nhất 8 ký tự.";
        }

        if (mess != null) {
            EmployeeDAO empDAO = new EmployeeDAO();
            Employee employee = empDAO.getEmployeeById(userID);

            request.setAttribute("mess", mess);
            request.setAttribute("user", user);
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("jsp/EmployeeProfile.jsp").forward(request, response);
            return;
        }

        // Nếu dữ liệu hợp lệ, cập nhật thông tin
        userDAO.updateUsers(userID, fullName, email, phone, address);
        
        // Cập nhật session và redirect
        Users updatedUser = userDAO.getUserById(userID);
        session.setAttribute("user", updatedUser);

        response.sendRedirect("EmployeeProfile");
    }

    private void handleAvatarUpload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userID = Integer.parseInt(request.getParameter("userID"));
        Part filePart = request.getPart("avatar");

        if (filePart != null && filePart.getSize() > 0) {
            // Lấy tên file
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Thư mục lưu ảnh trong webapp/images
            String uploadPath = getServletContext().getRealPath("/images");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Đường dẫn đầy đủ để lưu file
            String savePath = uploadPath + File.separator + fileName;
            filePart.write(savePath);

            // Cập nhật DB
            UsersDAO dao = new UsersDAO();
            dao.updateUserImage(userID, fileName);

            // Cập nhật lại session user
            Users updatedUser = dao.getUserById(userID);
            HttpSession session = request.getSession();
            session.setAttribute("user", updatedUser);
        }

        response.sendRedirect("EmployeeProfile");
    }
}
