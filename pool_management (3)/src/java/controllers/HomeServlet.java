package controllers;

import dal.TrainerDAO;
import dal.EventDAO;
import dal.SwimmingPoolDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Employee;
import models.Event;
import models.SwimmingPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TrainerDAO trainerDAO = new TrainerDAO();
        EventDAO eventDAO = new EventDAO();
        SwimmingPoolDAO poolDAO = new SwimmingPoolDAO();
       

        // Lấy dữ liệu từ form
        String eventSearch = request.getParameter("eventSearch");
        String eventMonth = request.getParameter("eventMonth");
        String eventYear = request.getParameter("eventYear");
        String poolSearch = request.getParameter("poolSearch");
        String poolStatus = request.getParameter("poolStatus");
        String trainerSearch = request.getParameter("trainerSearch");

        // Lấy tất cả sự kiện
        List<Event> eventList = eventDAO.getLatestEvents(4);

        // Filter theo từ khóa tìm kiếm
        if (eventSearch != null && !eventSearch.trim().isEmpty()) {
            String searchLower = eventSearch.toLowerCase();
            eventList.removeIf(e -> !(e.getTitle().toLowerCase().contains(searchLower)
                    || e.getDescription().toLowerCase().contains(searchLower)));
        }

        // Filter theo tháng và năm
        if ((eventMonth != null && !eventMonth.isEmpty()) || (eventYear != null && !eventYear.isEmpty())) {
            eventList.removeIf(e -> {
                java.sql.Date d = e.getEventDate();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(d);
                int month = cal.get(java.util.Calendar.MONTH) + 1; // tháng từ 1-12
                int year = cal.get(java.util.Calendar.YEAR);
                boolean monthMatch = eventMonth == null || eventMonth.isEmpty() || Integer.parseInt(eventMonth) == month;
                boolean yearMatch = eventYear == null || eventYear.isEmpty() || Integer.parseInt(eventYear) == year;
                return !(monthMatch && yearMatch);
            });
        }

        // Lấy tất cả bể bơi
        List<SwimmingPool> poolList = poolDAO.getAllPools();
        if (poolList == null) {
            poolList = new ArrayList<>();
        }
        request.setAttribute("poolList", poolList);
        if (poolSearch != null && !poolSearch.trim().isEmpty()) {
            String searchLower = poolSearch.toLowerCase();
            poolList.removeIf(p -> !p.getName().toLowerCase().contains(searchLower));
        }
        if (poolStatus != null && !poolStatus.trim().isEmpty()) {
            boolean statusFilter = poolStatus.equals("active");
            poolList.removeIf(p -> p.isStatus() != statusFilter);
        }

        // Lấy tất cả huấn luyện viên
        List<Employee> trainerList = trainerDAO.getAllTrainers();
        if (trainerSearch != null && !trainerSearch.trim().isEmpty()) {
            String searchLower = trainerSearch.toLowerCase();
            trainerList.removeIf(t -> !(t.getFullName().toLowerCase().contains(searchLower)
                    || (t.getDescription() != null && t.getDescription().toLowerCase().contains(searchLower))));
        }

        // Gán dữ liệu cho JSP
        request.setAttribute("eventList", eventList);
        request.setAttribute("poolList", poolList);
        request.setAttribute("trainerList", trainerList);

        // Truyền lại các giá trị lọc để giữ trạng thái form
        request.setAttribute("eventSearch", eventSearch);
        request.setAttribute("eventMonth", eventMonth);
        request.setAttribute("eventYear", eventYear);
        request.setAttribute("poolSearch", poolSearch);
        request.setAttribute("poolStatus", poolStatus);
        request.setAttribute("trainerSearch", trainerSearch);

        request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
    }

}
