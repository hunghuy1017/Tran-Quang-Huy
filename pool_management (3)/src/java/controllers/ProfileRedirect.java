package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.Users;

@WebServlet(name = "ProfileRedirect", urlPatterns = {"/ProfileRedirect"})
public class ProfileRedirect extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        switch (user.getRoleID()) {
            case 1: // Admin
                response.sendRedirect("AdminProfile");
                break;
            case 2: // Normal User
                response.sendRedirect("UserProfile");
                break;
            case 3: // Employee
                response.sendRedirect("EmployeeProfile");
                break;
            case 4: // Trainer
                response.sendRedirect("EmployeeProfile");
                break;
            default:
                response.sendRedirect("home");
        }
    }
}