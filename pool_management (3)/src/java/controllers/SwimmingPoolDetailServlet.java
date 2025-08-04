package controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import dal.SwimmingPoolDAO;
import models.SwimmingPool;
import models.Event;
import models.TrainerBooking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.UserReviews;

@WebServlet(name = "SwimmingPoolDetailServlet", urlPatterns = {"/SwimmingPoolDetailServlet"})
public class SwimmingPoolDetailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int poolId = Integer.parseInt(request.getParameter("id"));
            SwimmingPoolDAO dao = new SwimmingPoolDAO();

            // Lấy thông tin hồ bơi được chọn
            SwimmingPool pool = dao.getPoolById(poolId);
            List<Event> events = dao.getEventsByPoolId(poolId);
            List<TrainerBooking> bookings = dao.getTrainerBookingsByPoolId(poolId);
            List<UserReviews> reviews = dao.getUserReviewsByPoolId(poolId);

            // Lấy danh sách các hồ bơi khác (trừ hồ bơi hiện tại)
            List<SwimmingPool> otherPools = dao.getAllPools().stream()
                    .filter(p -> p.getPoolID() != poolId)
                    .collect(Collectors.toList());

            // Đặt các thuộc tính cho JSP
            request.setAttribute("pool", pool);
            request.setAttribute("events", events);
            request.setAttribute("bookings", bookings);
            request.setAttribute("reviews", reviews);
            request.setAttribute("otherPools", otherPools);

            request.getRequestDispatcher("jsp/poolDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Pool ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
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
        return "Displays details of a swimming pool and other pools";
    }
}