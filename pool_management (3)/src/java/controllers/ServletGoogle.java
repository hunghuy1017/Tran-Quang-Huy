/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.UsersDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;
import models.GoogleForm;
import models.Users;


/**
 *
 * @author Hi
 */
@WebServlet(name="ServletGoogle", urlPatterns={"/ServletGoogle"})
public class ServletGoogle extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GoogleForm.class.getName());
    private UsersDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new UsersDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            request.setAttribute("mess", "Lỗi: Không nhận được mã xác thực từ Google!");
            request.getRequestDispatcher("jsp/LoginUser.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy access token từ Google
            String accessToken = GoogleLogin.getToken(code);
            
            // Lấy thông tin người dùng từ Google
            GoogleForm googleUser = GoogleLogin.getUserInfo(accessToken);
            if (googleUser.getEmail() == null || googleUser.getEmail().trim().isEmpty()) {
                throw new IllegalStateException("Email từ Google không hợp lệ!");
            }

            // Kiểm tra xem người dùng đã tồn tại dựa trên email
            Users existingUser = dao.searchUserByEmail(googleUser.getEmail());

            if (existingUser == null) {
                // Nếu người dùng chưa tồn tại, tạo người dùng mới
                Users newUser = new Users(
                    0, // userID (auto-increment)
                    2, // roleID (giả sử 2 là Customer)
                    googleUser.getName() != null ? googleUser.getName() : "Google User", // fullName
                    googleUser.getEmail(), // email
                    "", // password
                    "", // phone
                    "", // address
                    googleUser.getPicture() != null ? googleUser.getPicture() : "" // avatar
                );

                dao.insertUser(newUser);
                existingUser = dao.searchUserByEmail(googleUser.getEmail());
                if (existingUser == null) {
                    throw new IllegalStateException("Không thể lấy người dùng vừa tạo với email: " + googleUser.getEmail());
                }
            }

            // Lưu thông tin người dùng vào session
            session.setAttribute("user", existingUser);
            session.setAttribute("userID", existingUser.getUserID());
            session.setAttribute("roleID", existingUser.getRoleID());
            session.setAttribute("googleUser", googleUser);

            // Chuyển hướng đến trang home
            response.sendRedirect("home");
        } catch (Exception e) {
            LOGGER.severe("Lỗi khi xử lý đăng nhập Google: " + e.getMessage());
            request.setAttribute("mess", "Lỗi đăng nhập Google: " + e.getMessage());
            request.getRequestDispatcher("jsp/LoginUser.jsp").forward(request, response);
        }
    }

    

    



    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
