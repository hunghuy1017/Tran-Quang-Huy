/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.UsersDAO;
import dal.UserPackageDAO;
import dal.UsersDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import models.UserPackage;
import models.Users;

/**
 *
 * @author LENOVO
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)

@WebServlet(name = "UserProfile", urlPatterns = {"/UserProfile"})
public class UserProfile extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserProfile</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserProfile at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        UsersDAO userDao = new UsersDAO();
        UserPackageDAO userPackageDao = new UserPackageDAO();
        // Lấy trainer của user
        Users uT = userDao.searchTrainerIDOfUserByUserID(user.getUserID());
        // Lấy gói bơi của user (nếu có)
        UserPackage uPack = userPackageDao.searchLatestUserPackageByUserID(user.getUserID());
        request.setAttribute("uT", uT);
        request.setAttribute("uPack", uPack);
        request.setAttribute("user", user); // Đảm bảo vẫn có user
        request.getRequestDispatcher("jsp/userProfile.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        String mess = null;
        UsersDAO userDao = new UsersDAO();
        if (user == null) {
            response.sendRedirect("login_severlet");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        // Kiểm tra định dạng số điện thoại
        String phoneRegex = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])[0-9]{7}$";
        if (!phone.matches(phoneRegex)) {
            mess = "Số điện thoại không đúng định dạng.";
            request.setAttribute("mess", mess);
            Users uT = userDao.searchTrainerIDOfUserByUserID(user.getUserID());
            UserPackageDAO userPackageDao = new UserPackageDAO();
            UserPackage uPack = userPackageDao.searchLatestUserPackageByUserID(user.getUserID());
            request.setAttribute("uT", uT);
            request.setAttribute("uPack", uPack);
            request.getRequestDispatcher("jsp/userProfile.jsp").forward(request, response);
            return;
        }

// Kiểm tra số điện thoại đã tồn tại chưa
        if (userDao.isPhoneExists(phone, user.getUserID())) {
            mess = "Số điện thoại đã được sử dụng bởi tài khoản khác.";
            request.setAttribute("mess", mess);
            request.setAttribute("user", user);
            Users uT = userDao.searchTrainerIDOfUserByUserID(user.getUserID());
            UserPackageDAO userPackageDao = new UserPackageDAO();
            UserPackage uPack = userPackageDao.searchLatestUserPackageByUserID(user.getUserID());
            request.setAttribute("uT", uT);
            request.setAttribute("uPack", uPack);
            request.getRequestDispatcher("jsp/userProfile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email đã tồn tại chưa (ngoại trừ của user hiện tại)
        Users existingUser = userDao.getUserByEmail(email);
        if (existingUser != null && existingUser.getUserID() != user.getUserID()) {
            mess = "Email đã tồn tại, vui lòng chọn email khác.";
        } else {
            // Kiểm tra định dạng email
            String emailRegex = "^[\\w.-]{8,}@(gmail\\.com|hotmail\\.com|fpt\\.edu\\.vn)$";
            if (!email.matches(emailRegex)) {
                mess = "Email không đúng định dạng. Vui lòng dùng gmail.com, hotmail.com hoặc fpt.edu.vn và phần tên trước @ phải có ít nhất 8 ký tự.";
            }

        }

        // Nếu có lỗi, giữ lại dữ liệu và gửi về lại trang profile
        if (mess != null) {
            request.setAttribute("mess", mess);
            request.setAttribute("user", user);
            Users uT = userDao.searchTrainerIDOfUser(user.getUserID());
            UserPackageDAO userPackageDao = new UserPackageDAO();
            UserPackage uPack = userPackageDao.searchLatestUserPackageByUserID(user.getUserID());

            request.setAttribute("uT", uT);
            request.setAttribute("uPack", uPack);
            request.getRequestDispatcher("jsp/userProfile.jsp").forward(request, response);
            return;
        }

        Part filePart = request.getPart("avatar");
        String newImageFileName = user.getImage();

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/images"); // ✅ fix chỗ này
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // dùng mkdirs() an toàn hơn mkdir()
            }

            newImageFileName = "avatar_user_" + user.getUserID() + "_" + System.currentTimeMillis() + "_" + fileName;
            File file = new File(uploadPath, newImageFileName);
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Cập nhật thông tin người dùng
        Users updatedUser = new Users();
        updatedUser.setUserID(user.getUserID());
        updatedUser.setRoleID(user.getRoleID());
        updatedUser.setFullName(fullName);
        updatedUser.setEmail(email);
        updatedUser.setPassword(password);
        updatedUser.setPhone(phone);
        updatedUser.setImage(newImageFileName);

        userDao.updateUser(updatedUser);
        session.setAttribute("user", updatedUser);

        Users uT = userDao.searchTrainerIDOfUserByUserID(user.getUserID());
        UserPackageDAO userPackageDao = new UserPackageDAO();
        UserPackage uPack = userPackageDao.searchLatestUserPackageByUserID(user.getUserID());

        mess = "Cập nhật thành công.";
        request.setAttribute("mess", mess);
        request.setAttribute("uT", uT);
        request.setAttribute("uPack", uPack);
        request.setAttribute("user", updatedUser);
        request.getRequestDispatcher("jsp/userProfile.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
