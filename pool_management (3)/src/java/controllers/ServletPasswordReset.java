/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.SendEmailDAO;
import java.util.UUID;
import dal.UsersDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Users;

/**
 *
 * @author THIS PC
 */
@WebServlet(name = "ServletPasswordReset", urlPatterns = {"/ServletPasswordReset"})
public class ServletPasswordReset extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        SendEmailDAO Edao = new SendEmailDAO();
        HttpSession session = request.getSession(true);

        UsersDAO Udao = new UsersDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "PasswordReset";
        }

        if (service.equals("CheckReset")) {
            Integer UReset = (Integer) session.getAttribute("UReset");
            if (UReset != null) {
                request.getRequestDispatcher("jsp/ConfirmReset.jsp").forward(request, response);
                return;
            }
            request.getRequestDispatcher("jsp/ChangeSuccess.jsp").forward(request, response);
        }

        if (service.equals("ConfirmReset")) {
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmpassword");
            String checkPassword = Udao.checkPassword(password, confirmPassword);
            if (checkPassword.equals("Valid")) {
                Integer userId = (Integer) session.getAttribute("UReset");
                Users u = new Users(userId, password);
                Udao.changePassword(u);
                session.removeAttribute("UReset");
                request.getRequestDispatcher("jsp/ChangeSuccess.jsp").forward(request, response);
                return;
            }

            request.setAttribute("checkPassword", checkPassword);
            request.getRequestDispatcher("jsp/ConfirmReset.jsp").forward(request, response);
        }

        if (service.equals("PasswordReset")) {
            String emali = request.getParameter("email");

            String checkEmail = Udao.CheckFormatEmail(emali);
            if (checkEmail.equals("Valid")) {
                Users u = Udao.searchUser(emali);
                if (u == null) {
                    String mess = "Emali không tồn tại";
                    request.setAttribute("mess", mess);
                    request.getRequestDispatcher("jsp/PasswordReset.jsp").forward(request, response);
                    return;
                }
                int UserID = u.getUserID();
                session.setAttribute("UReset", UserID);
                String contextPath = request.getContextPath();
                Edao.sendEmail(emali, UserID, contextPath);
                String Wmess = "đã gửi link đặt lại mật khẩu về email của bạn";
                request.setAttribute("Wmess", Wmess);
                request.getRequestDispatcher("jsp/PasswordReset.jsp").forward(request, response);
            } else {
                request.setAttribute("checkEmail", checkEmail);
                request.getRequestDispatcher("jsp/PasswordReset.jsp").forward(request, response);
            }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
