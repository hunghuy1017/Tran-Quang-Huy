package controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import dal.EmployeeDAO;
import java.util.List;
import models.Employee;
@WebServlet(name = "EmployeeDashboardServlet", urlPatterns = {"/EmployeeDashboard"})
public class EmployeeDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                
        // Bảo vệ: chỉ cho phép nhân viên (RoleID = 3)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("roleID") == null) {
            response.sendRedirect("ServletUsers?service=loginUser");
            return;
        }

        Integer roleID = (Integer) session.getAttribute("roleID");
        if (roleID != 3) {
            response.sendRedirect("ServletUsers?service=loginUser");
            return;
        }
        Integer userId = (Integer) session.getAttribute("userID");
        if (userId != null) {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            // Lấy vị trí của nhân viên
            String position = employeeDAO.getPositionByUserId(userId);
            // Đặt vị trí vào request attribute để sử dụng trong JSP
            request.setAttribute("position", position);
        }

        // Chuyển đến trang dashboard cho employee
        request.getRequestDispatcher("jsp/EmployeeDashboard.jsp").forward(request, response);
    }
}
