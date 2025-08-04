package controllers;

import dal.EventDAO;
import dal.NotificationDAO;
import dal.OrderDAO;
import dal.TrainerDAO;
import dal.UsersDAO;
import dal.UserReviewDAO; // Import DAO mới

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate; // Giữ lại cho doanh thu hôm nay

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UsersDAO userDAO;
    private EventDAO eventDAO;
    private OrderDAO orderDAO;
    private TrainerDAO trainerDAO;
    private NotificationDAO notificationDAO;
    private UserReviewDAO userReviewDAO; // Khai báo DAO mới

    public void init() {
        userDAO = new UsersDAO();
        eventDAO = new EventDAO();
        orderDAO = new OrderDAO();
        trainerDAO = new TrainerDAO();
        notificationDAO = new NotificationDAO();
        userReviewDAO = new UserReviewDAO(); // Khởi tạo DAO mới
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalUsers", userDAO.getTotalUsers());
        request.setAttribute("totalEmployees", userDAO.getTotalEmployees());
        request.setAttribute("totalEvents", eventDAO.getTotalEvents());
        request.setAttribute("totalOrders", orderDAO.getTotalOrders());
        request.setAttribute("todayRevenue", orderDAO.getTodayRevenue());

        String userNameFilter = request.getParameter("userName");
        String userDateFilter = request.getParameter("userDate");
        int userLimit = 5;
        if (userNameFilter != null || userDateFilter != null) {
            request.setAttribute("recentUsers", userDAO.getFilteredUsers(userNameFilter, userDateFilter, userLimit));
        } else {
            request.setAttribute("recentUsers", userDAO.getRecentUsers(userLimit));
        }

        String trainerNameFilter = request.getParameter("trainerName");
        int trainerLimit = 5;
        request.setAttribute("topTrainers", trainerDAO.getTopTrainers(trainerNameFilter, trainerLimit));

        int notifLimit = 5;
        request.setAttribute("recentNotifications", notificationDAO.getAllNotifications(notifLimit));

        // Thêm lại phần xử lý doanh thu theo tháng
        int currentYear = LocalDate.now().getYear();
        request.setAttribute("monthlyRevenue", orderDAO.getMonthlyRevenue(currentYear));

        // Thêm xử lý cho đánh giá hồ bơi trung bình
        request.setAttribute("averagePoolRatings", userReviewDAO.getAverageRatingByPool());

        request.getRequestDispatcher("/jsp/adminDashboard.jsp").forward(request, response);
    }
}
