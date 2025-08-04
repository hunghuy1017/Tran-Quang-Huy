/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.vnpay.common;

import dal.PaymentDAO;
import dal.SendEmailDAO;
import dal.UserPackageDAO;
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
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import models.Payments;
import models.UserPackage;
import models.Users;

/**
 *
 * @author HP
 */
@WebServlet("/vnpayReturn")
public class VnpayReturn extends HttpServlet {

    PaymentDAO orderDao = new PaymentDAO();
    UserPackageDAO upackDao = new UserPackageDAO();
    SendEmailDAO sendEDao= new SendEmailDAO();
    UsersDAO uDao = new UsersDAO();
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
        try {
            
            HttpSession session = request.getSession();
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = (String) params.nextElement();
                String fieldValue = request.getParameter(fieldName);

                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            String signValue = Config.hashAllFields(fields);
            boolean signatureValid = signValue.equals(vnp_SecureHash); // Biến mới để kiểm tra tính hợp lệ của chữ ký

            // Đặt tất cả các tham số VNPay vào request attribute để JSP dễ dàng truy cập
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = (String) params.nextElement();
                request.setAttribute(fieldName, request.getParameter(fieldName));
            }

            // Xóa secure hash và secure hash type khỏi request attribute nếu đã đặt
            // (Chỉ xóa nếu bạn đã đặt tất cả các tham số vào attributes ở trên)
            request.removeAttribute("vnp_SecureHash");
            request.removeAttribute("vnp_SecureHashType");
            request.setAttribute("signatureValid", signatureValid); // Gán biến này vào request attribute
            if (signatureValid) {
                String payId = request.getParameter("vnp_TxnRef");
                Payments pay = new Payments();
                int pid = Integer.parseInt(payId);
                pay.setPaymentID(pid);
                boolean transSuccess = false;
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    pay.setStatus("Completed");
                    transSuccess = true;

                    Integer userId = (Integer) session.getAttribute("userId");
                    Integer packageId = (Integer) session.getAttribute("packageIđ");
                    Date timeOfPayment = (Date) session.getAttribute("timeOfPayment");
                    Timestamp timeOfUserPackage = (Timestamp)session.getAttribute("timeOfUserPackage");
                    if(userId !=null && packageId!=null && timeOfPayment !=null && timeOfUserPackage!=null && timeOfPayment.equals(timeOfUserPackage)){
                        UserPackage upack = upackDao.findPendingUserPackage(userId, packageId, timeOfUserPackage);
                    if(upack !=null){
                    upackDao.updateUserPackageStatus(upack.getUserID(),upack.getPackageID(), upack.getPurchaseDate());
                    }
                     else {
                            System.err.println("Không tìm thấy UserPackage đang chờ xử lý với UserID: " + userId + ", PackageID: " + packageId + ", PurchaseTime: " + timeOfUserPackage);
                     }
                    
                        session.removeAttribute("currentUserID");
                        session.removeAttribute("currentPackageID");
                        session.removeAttribute("currentPurchaseTime");
                    
                    }
                } else {
                    pay.setStatus("Failed");
                }
                Users u = uDao.searchUserByID((Integer) session.getAttribute("userId"));
                sendEDao.sendEmail1(u.getEmail(), "Thanh toán Thành công", "<p>Bạn đã mua thành công gói bơi.</p>");
                orderDao.updateStatusOnly(pay);
                
                request.setAttribute("transResult", transSuccess); // Gán kết quả giao dịch vào request attribute
            } else {
                System.out.println("GD KO HOP LE (invalid signature)");
                request.setAttribute("transResult", false); // Nếu chữ ký không hợp lệ, coi như giao dịch thất bại
            }

            // Forward đến JSP để hiển thị kết quả
            request.getRequestDispatcher("jsp/paymentResult.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error in VnpayReturn servlet: " + e.getMessage());
            // Xử lý lỗi chung, có thể forward đến trang lỗi
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình xử lý giao dịch.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }

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
