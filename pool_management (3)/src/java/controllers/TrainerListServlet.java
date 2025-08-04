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
@WebServlet("/trainer-list")
public class TrainerListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TrainerDAO trainerDAO = new TrainerDAO();
        String trainerSearch = request.getParameter("trainerSearch");

        // Lấy tất cả huấn luyện viên
        List<Employee> trainerList = trainerDAO.getAllTrainers();
        if (trainerSearch != null && !trainerSearch.trim().isEmpty()) {
            String searchLower = trainerSearch.toLowerCase();
            trainerList.removeIf(t -> !(t.getFullName().toLowerCase().contains(searchLower)
                    || (t.getDescription() != null && t.getDescription().toLowerCase().contains(searchLower))));
        }
        System.out.println("[TrainerListServlet] Tổng trainer lấy được: " + trainerList.size());

        request.setAttribute("trainerList", trainerList);
        request.setAttribute("trainerSearch", trainerSearch);


        // 🔧 Fix lỗi đường dẫn JSP
        request.getRequestDispatcher("/jsp/trainerList.jsp").forward(request, response);
    }
}
