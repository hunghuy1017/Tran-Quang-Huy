package controllers;

import dal.RentalDAO;
import dal.SellerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import models.ProductRental;

@WebServlet(name = "RentalServlet", urlPatterns = {"/RentalManager"})
public class RentalServlet extends HttpServlet {
    private RentalDAO rentalDAO = new RentalDAO();
    private SellerDAO sellerDAO = new SellerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        Integer userID = (Integer) session.getAttribute("userID");
        int poolID = sellerDAO.getPoolIDFromUserID(userID);

        if (poolID == -1) {
            request.setAttribute("error", "Không thể xác định PoolID cho nhân viên.");
            request.getRequestDispatcher("/jsp/rentalManager.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action") != null ? request.getParameter("action") : "listRentals";

        switch (action) {
            case "createRental":
                showCreateRentalForm(request, response, poolID);
                break;
            case "completeRental":
                completeRental(request, response);
                break;
            default:
                listRentals(request, response, poolID);
        }
    }

    private void listRentals(HttpServletRequest request, HttpServletResponse response, int poolID)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("search");
        List<ProductRental> rentals = rentalDAO.getRentalsByPoolID(poolID, searchTerm);
        request.setAttribute("rentals", rentals);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/jsp/rentalManager.jsp").forward(request, response);
    }

    private void showCreateRentalForm(HttpServletRequest request, HttpServletResponse response, int poolID)
            throws ServletException, IOException {
        List<ProductRental> availableProducts = rentalDAO.getAvailableProductsForRental(poolID);
        request.setAttribute("availableProducts", availableProducts);
        request.getRequestDispatcher("/jsp/createRental.jsp").forward(request, response);
    }

    private void completeRental(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int rentalID = Integer.parseInt(request.getParameter("rentalID"));
        rentalDAO.updateRentalStatus(rentalID, "Completed");
        response.sendRedirect("RentalManager");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("roleID") == null || (Integer) session.getAttribute("roleID") != 3) {
            response.sendRedirect("ServletUsers?service=loginUser");
            return;
        }

        String action = request.getParameter("action");
        if ("createRental".equals(action)) {
            createRental(request, response);
        }
    }

    private void createRental(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        int employeeUserID = (Integer) session.getAttribute("userID");
        int poolID = sellerDAO.getPoolIDFromUserID(employeeUserID);

        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            String customerName = request.getParameter("customerName").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            // Xác thực đầu vào
            if (customerName.isEmpty()) {
                throw new IllegalArgumentException("Tên khách hàng không được để trống.");
            }
            if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10}")) {
                throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số.");
            }

            // Phân tích ngày tháng
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Timestamp startDate = new Timestamp(sdf.parse(startDateStr).getTime());
            Timestamp endDate = new Timestamp(sdf.parse(endDateStr).getTime());

            // Xác thực ngày tháng
            if (endDate.before(startDate) || endDate.equals(startDate)) {
                throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu.");
            }

            // Lấy RentalPrice từ bảng Product
            double rentalPrice = rentalDAO.getRentalPriceByProductID(productID);
            if (rentalPrice <= 0) {
                throw new IllegalArgumentException("Không tìm thấy giá thuê hợp lệ cho sản phẩm.");
            }

            // Tính tổng chi phí dựa trên thời gian thuê (giá mỗi giờ)
            long durationHours = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
            double totalCost = rentalPrice * durationHours;

            // Tạo đối tượng ProductRental
            ProductRental rental = new ProductRental();
            rental.setProductID(productID);
            rental.setPoolID(poolID);
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);
            rental.setRentalPrice(rentalPrice);
            rental.setTotalCost(totalCost);
            rental.setStatus("Active");
            rental.setCustomerName(customerName);
            rental.setPhoneNumber(phoneNumber);

            // Lưu vào cơ sở dữ liệu với userID
            rentalDAO.createRental(rental, employeeUserID);
            response.sendRedirect("RentalManager");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showCreateRentalForm(request, response, poolID);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            showCreateRentalForm(request, response, poolID);
        }
    }
}