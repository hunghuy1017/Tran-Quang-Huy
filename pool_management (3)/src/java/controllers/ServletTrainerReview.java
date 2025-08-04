/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;


import dal.TrainerDAO;
import dal.TrainerReviewDAO;
import dal.UserReviewDAO;
import dal.UsersDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Vector;
import models.TrainerBooking;
import models.TrainerReviews;
import models.UserReviews;
import models.Users;

/**
 *
 * @author THIS PC
 */
@WebServlet(name="ServletTrainerReview", urlPatterns={"/ServletTrainerReview"})
public class ServletTrainerReview extends HttpServlet {
   
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
        UsersDAO UDao = new UsersDAO();
        TrainerDAO TDao = new TrainerDAO();
        TrainerReviewDAO dao = new TrainerReviewDAO();
        String service = request.getParameter("service");
        if (service == null) {
            service = "listUserReviewTrainer";
        }
        
        if (service.equals("addReview")) {
            int userID = Integer.parseInt(request.getParameter("userID"));
            int TrainerID = Integer.parseInt(request.getParameter("TrainerID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            Timestamp now = new Timestamp(System.currentTimeMillis());
            TrainerReviews review = new TrainerReviews(userID, TrainerID, rating, comment, now);
            new TrainerReviewDAO().insertReview(review);

            response.sendRedirect("ServletTrainerReview?service=listUserReviewTrainer&TrainerID=" + TrainerID);
        }
        
        if (service.equals("listUserReviewTrainer")) {
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("ServletUsers?service=loginUser");
                return;
            }

            int userId = user.getUserID();
            int TrainerID = Integer.parseInt(request.getParameter("TrainerID"));

            int star = 0;
            try {
                star = Integer.parseInt(request.getParameter("star"));
            } catch (Exception e) {
                // Default to 0
            }

            int page = 1;
            int pageSize = 5;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e) {
            }

            int offset = (page - 1) * pageSize;

            Users sp = UDao.searchUserByID(TrainerID);
            Vector<TrainerReviews> reviews = dao.getReviewsByTrainer(TrainerID, star, offset, pageSize);
            int total = dao.countReviewsByTrainer(TrainerID, star);
            int totalPages = (int) Math.ceil((double) total / pageSize);

            Vector<TrainerBooking> Booking = TDao.getByUserIDAndTrainerID(userId, TrainerID);
            String mess = null;
            if (Booking.isEmpty()) {
                mess = "Bạn phải Book huấn luyện viên để được đánh giá.";
            }

            double avg = dao.getAverageRatingByTrainer(TrainerID);
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

            request.getRequestDispatcher("jsp/ReviewTrainer.jsp").forward(request, response);
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
