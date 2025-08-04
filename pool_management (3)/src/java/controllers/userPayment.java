/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import com.vnpay.common.Config;
import dal.PackageDAO;
import dal.PaymentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import models.Payments;
import models.UserPackage;
import models.Users;
import org.apache.catalina.User;
import dal.UserPackageDAO;
import models.Packages;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "userPayment", urlPatterns = {"/userPayment"})
public class userPayment extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet userPayment</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet userPayment at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        // Lấy thông tin từ form
        PackageDAO padao = new PackageDAO();
        String email = request.getParameter("email");
        String paymentMethod = request.getParameter("paymentMethod");
        int packageId = Integer.parseInt(request.getParameter("packageId"));
        Packages pa = padao.getPackageById(request.getParameter("packageId"));
        double total = Double.parseDouble(request.getParameter("total"));

        // Lấy user từ session
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Date createTime = new Date();

        // Tạo đơn trong Payments status pending
        Payments payment = new Payments();
        payment.setUserID(user.getUserID());
        payment.setPackageID(packageId);
        payment.setPoolID(1); // Cố định PoolID, tùy dự án bạn có thể lấy động
        payment.setPaymentMethod(paymentMethod);
        payment.setTotal(total);
        payment.setStatus("Pending");
        payment.setPaymentTime(createTime);
        PaymentDAO dao = new PaymentDAO();
        int paymentId = dao.insertPayment(payment);

        if (paymentId <= 0) {
            request.setAttribute("error", "Không thể tạo giao dịch!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int purchaseDate = pa.getDurationInDays();
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_MONTH, purchaseDate);
        Date endDate = cal.getTime();

        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
        java.sql.Timestamp createTimeSql = new java.sql.Timestamp(createTime.getTime());
        //tạo đơn trong UserPackage status pending
        UserPackageDAO userPackageDAO = new UserPackageDAO();
        UserPackage pendingPackage = new UserPackage();
        pendingPackage.setUserID(user.getUserID());
        pendingPackage.setPoolID(1);
        pendingPackage.setPackageID(packageId);
        pendingPackage.setPurchaseDate(createTimeSql);
        pendingPackage.setStartDate(sqlStartDate);
        pendingPackage.setEndTime(sqlEndDate);
        pendingPackage.setIsActive(false);
        pendingPackage.setPaymentStatus("Pending");
        boolean insertPackageSuccess = userPackageDAO.insertUserPackage(pendingPackage);
        if (!insertPackageSuccess) {
            request.setAttribute("error", "Không thể tạo UserPackage!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        session.setAttribute("userId", user.getUserID());
        session.setAttribute("packageIđ", packageId);
        session.setAttribute("timeOfPayment", createTime);
        session.setAttribute("timeOfUserPackage", createTimeSql);

        if (paymentMethod.equals("vnpay")) {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "billpayment";
            long amount = (long) (total * 100);
            String vnp_TxnRef = String.valueOf(paymentId);
            String vnp_IpAddr = Config.getIpAddress(request);
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            String vnp_ReturnUrl = Config.vnp_ReturnUrl;
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan goi boi");
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (int i = 0; i < fieldNames.size(); i++) {
                String name = fieldNames.get(i);
                String value = vnp_Params.get(name);
                if (i > 0) {
                    hashData.append('&');
                    query.append('&');
                }

                hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            }
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String paymentUrl = Config.vnp_PayUrl + "?" + query.toString();
            // Redirect sang VNPay
            response.sendRedirect(paymentUrl);
        } else {
            request.setAttribute("error", "Phương thức thanh toán không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
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
