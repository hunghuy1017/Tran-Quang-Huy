/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.NotificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import models.Notifications;
import models.Users;

/**
 *
 * @author THIS PC
 */
@WebServlet(name="ServletServletNotifications", urlPatterns={"/ServletNotification"})
public class ServletServletNotifications extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        Users user = (Users) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("ServletUsers?service=loginUser");
                return;
            }
            int id = user.getUserID();

        String service = request.getParameter("service");
        if (service == null) {
            service = "listNotification";
        }

        NotificationDAO NDao = new NotificationDAO();

        if (service.equals("CheckNotification")) {
            int NotificationID = Integer.parseInt(request.getParameter("nid"));
            boolean IsRead = !Boolean.parseBoolean(request.getParameter("IsRead"));
            NDao.changeIsRead(NotificationID, IsRead);
            Notifications N = NDao.searchNotification(NotificationID);

            String Mess = "";
            if (IsRead == true) {
                Mess = "Đánh dấu đã đọc";
            } else {
                Mess = "Bỏ đánh dấu";
            }
            request.setAttribute("N", N);
            request.setAttribute("Mess", Mess);
            request.getRequestDispatcher("jsp/NotificationDetail.jsp").forward(request, response);
        }

        if (service.equals("DeleteNotification")) {
            int NotificationID = Integer.parseInt(request.getParameter("nid"));
            NDao.deleteNotification(NotificationID);

            String mess = "Xóa thông báo thành công!";
            Vector<Notifications> list = NDao.getNotificationsByPage(id, "", "all", "desc", 0, 10);
            int total = NDao.countNotifications(id, "", "all");
            int totalPages = (int) Math.ceil((double) total / 10);

            request.setAttribute("data", list);
            request.setAttribute("page", 1);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("mess", mess);

            request.getRequestDispatcher("jsp/Notification.jsp").forward(request, response);
        }

        if (service.equals("NotificationDetail")) {
            int NotificationID = Integer.parseInt(request.getParameter("nid"));
            Notifications N = NDao.searchNotification(NotificationID);
            request.setAttribute("N", N);
            request.getRequestDispatcher("jsp/NotificationDetail.jsp").forward(request, response);
        }

        if (service.equals("listNotification")) {

            String Title = request.getParameter("Title");
            if (Title == null) {
                Title = "";
            }

            int page = 1;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            int offset = (page - 1) * pageSize;

            String status = request.getParameter("status");
            if (status == null) {
                status = "all";
            }

            String sort = request.getParameter("sort");
            if (sort == null) {
                sort = "desc";
            }

            Vector<Notifications> list = NDao.getNotificationsByPage(id, Title, status, sort, offset, pageSize);

            int total = NDao.countNotifications(id, Title, status);

            int totalPages = (int) Math.ceil((double) total / pageSize);//số trang sẽ hiển thị

            request.setAttribute("data", list);
            request.setAttribute("Title", Title);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("status", status);
            request.setAttribute("sort", sort);

            request.getRequestDispatcher("jsp/Notification.jsp").forward(request, response);
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
