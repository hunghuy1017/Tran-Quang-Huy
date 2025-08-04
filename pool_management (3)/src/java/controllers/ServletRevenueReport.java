/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.ProductDAO;
import dal.RevenueReportDAO;
import dal.SwimmingPoolDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import models.Employee;
import models.RevenuePackage;
import models.RevenuePool;
import models.RevenueProduct;
import models.RevenueTrainerBookings;
import models.SwimmingPool;
import models.Users;

/**
 *
 * @author THIS PC
 */
@WebServlet(name = "ServletRevenueReport", urlPatterns = {"/ServletRevenueReport"})
public class ServletRevenueReport extends HttpServlet {

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

        String service = request.getParameter("service");
        if (service == null) {
            service = "listRevenue";
        }
        
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        int UserID = user.getUserID();
        int RoleID = user.getRoleID();

        RevenueReportDAO pmDAO = new RevenueReportDAO();
        SwimmingPoolDAO SWPDAO = new SwimmingPoolDAO();
        ProductDAO PDAO = new ProductDAO();
        
        int poolID = 0;
        String position = "";

        if (RoleID != 1 && RoleID != 3) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        if (RoleID == 3) {
            Employee employee = PDAO.getEmployeeInfoByUserID(UserID);
            poolID = employee.getPoolID();
            position = employee.getPosition().trim();

            if (!position.equals("Manager")) {
                response.sendRedirect("jsp/LoginUser.jsp");
                return;
            }
        }

        
        if (service.equals("listRevenue")) {
            String PoolName = request.getParameter("PoolName");
            if (PoolName == null) {
                PoolName = "";
            }

            String productName = request.getParameter("productName");
             String Status = request.getParameter("Status");
            String OrderDate = request.getParameter("OrderDate");

            String Packagename = request.getParameter("Packagename");
            String paymentTime = request.getParameter("paymentTime");

            String Trainername = request.getParameter("Trainername");
            if (Trainername == null) {
                Trainername = "";
            }
            String PaymentDate = request.getParameter("PaymentDate");
            if (PaymentDate == null) {
                PaymentDate = "";
            }
            
            String Username = request.getParameter("Username");
            if (Username == null) {
                Username = "";
            }

            Vector<RevenueProduct> listP = pmDAO.getRevenueProduct(productName, OrderDate, Status,poolID);
            Vector<RevenuePackage> listPK = pmDAO.getRevenuePackage(Packagename, paymentTime,poolID);
            Vector<RevenueTrainerBookings> listRB = pmDAO.getRevenueTrainerBookings(Username, Trainername,PaymentDate,poolID);
            double total = pmDAO.getTotalRevenue(poolID);
            Vector<RevenuePool> listSP = pmDAO.getRevenueByPool(PoolName);

            request.setAttribute("listP", listP);
            request.setAttribute("productName", productName);
            request.setAttribute("OrderDate", OrderDate);
            request.setAttribute("Status", Status);
            request.setAttribute("listPK", listPK);
            request.setAttribute("Packagename", Packagename);
            request.setAttribute("paymentTime", paymentTime);
            request.setAttribute("listRB", listRB);
            request.setAttribute("Trainername", Trainername);
            request.setAttribute("PaymentDate", PaymentDate);   
            request.setAttribute("Username", Username);
            request.setAttribute("total", total);
            request.setAttribute("listSP", listSP);
            request.setAttribute("PoolName", PoolName);
             request.setAttribute("RoleID", RoleID);

            request.getRequestDispatcher("jsp/RevenueReport.jsp").forward(request, response);
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
