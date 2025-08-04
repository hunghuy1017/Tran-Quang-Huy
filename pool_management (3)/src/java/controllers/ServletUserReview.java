/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.SwimmingPoolDAO;
import dal.UserPackageDAO;
import dal.UserReviewDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import models.UserReviews;
import java.sql.Timestamp;
import models.SwimmingPool;
import models.UserPackage;
import models.Users;

/**
 *
 * @author THIS PC
 */
@WebServlet(name = "ServletUserReview", urlPatterns = {"/ServletUserReview"})
public class ServletUserReview extends HttpServlet {

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

        HttpSession session = request.getSession(true);

        UserPackageDAO UPDao = new UserPackageDAO();
        UserReviewDAO dao = new UserReviewDAO();
        SwimmingPoolDAO Spdao = new SwimmingPoolDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "listUserReview";
        }

        if (service.equals("addReview")) {
            int userID = Integer.parseInt(request.getParameter("userID"));
            int poolID = Integer.parseInt(request.getParameter("poolID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            Timestamp now = new Timestamp(System.currentTimeMillis());
            UserReviews review = new UserReviews(userID, poolID, rating, comment, now);
            new UserReviewDAO().insertReview(review);

            response.sendRedirect("ServletUserReview?service=listUserReview&poolID=" + poolID);
        }

        if (service.equals("listUserReview")) {
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("ServletUsers?service=loginUser");
                return;
            }
            int userId = user.getUserID();

            int poolId = Integer.parseInt(request.getParameter("poolID"));
            int star = 0;
            try {
                star = Integer.parseInt(request.getParameter("star"));
            } catch (Exception e) {
            }

            int page = 1;
            int pageSize = 5;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e) {
            }

            int offset = (page - 1) * pageSize;

            SwimmingPool sp = Spdao.searchSwimmingPool(poolId);

            Vector<UserReviews> reviews = dao.getReviewsByPool(poolId, star, offset, pageSize);
            int total = dao.countReviews(poolId, star);
            int totalPages = (int) Math.ceil((double) total / pageSize);

            Vector<UserPackage> packages = UPDao.getByUserIDAndPoolID(userId, poolId);
            String mess = null;
            if (packages.isEmpty()) {
                mess = "Bạn phải mua gói bơi của bể bơi này để được đánh giá.";
            }

            double avg = dao.getAverageRatingByPool(poolId);
            int fullStars = (int) avg;
            boolean hasHalfStar = (avg - fullStars) >= 0.25 && (avg - fullStars) < 0.75;
            int emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

            request.setAttribute("avg", avg);
            request.setAttribute("fullStars", fullStars);
            request.setAttribute("hasHalfStar", hasHalfStar);
            request.setAttribute("emptyStars", emptyStars);
            request.setAttribute("reviews", reviews);
            request.setAttribute("star", star);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("sp", sp);
            request.setAttribute("mess", mess);

            request.getRequestDispatcher("jsp/ReviewPool.jsp").forward(request, response);
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
