
package controllers;

import dal.SellerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import models.Product;
import models.Category;
import models.Order;
import models.OrderDetail;

@WebServlet(name = "SellerServlet", urlPatterns = {"/Seller"})
public class SellerServlet extends HttpServlet {
    private SellerDAO sellerDAO = new SellerDAO();
    private static final int PRODUCTS_PER_PAGE = 6;

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
            request.getRequestDispatcher("/jsp/seller.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action") != null ? request.getParameter("action") : "viewProducts";

        switch (action) {
            case "viewCart":
                viewCart(request, response, userID);
                break;
            case "removeFromCart":
                removeFromCart(request, response, userID);
                break;
            case "increase":
                increaseQuantity(request, response, userID);
                break;
            case "decrease":
                decreaseQuantity(request, response, userID);
                break;
            case "checkout":
                checkout(request, response, userID);
                break;
            default:
                viewProducts(request, response, userID, poolID);
        }
    }

    private void viewProducts(HttpServletRequest request, HttpServletResponse response, int userID, int poolID)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("search");
        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int categoryID = request.getParameter("categoryID") != null ? Integer.parseInt(request.getParameter("categoryID")) : 0;
        int offset = (page - 1) * PRODUCTS_PER_PAGE;

        List<Product> products = sellerDAO.getProductsWithSearchAndPagination(poolID, searchTerm, categoryID, offset, PRODUCTS_PER_PAGE);
        int totalProducts = sellerDAO.getTotalProducts(poolID, searchTerm, categoryID);
        int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        List<Category> categories = sellerDAO.getAllCategories();
        int orderID = sellerDAO.getOrCreatePendingOrder(userID);
        int cartItemCount = sellerDAO.getCartItemCount(orderID);

        request.setAttribute("products", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("categoryID", categoryID);
        request.setAttribute("categories", categories);
        request.setAttribute("cartItemCount", cartItemCount);

        request.getRequestDispatcher("/jsp/seller.jsp").forward(request, response);
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response, int userID)
            throws ServletException, IOException {
        int orderID = sellerDAO.getOrCreatePendingOrder(userID);
        List<OrderDetail> cartItems = sellerDAO.getOrderDetails(orderID);
        double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        int cartItemCount = sellerDAO.getCartItemCount(orderID);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total", total);
        request.setAttribute("orderID", orderID);
        request.setAttribute("cartItemCount", cartItemCount);
        request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, int userID)
            throws IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        int orderID = sellerDAO.getOrCreatePendingOrder(userID);
        sellerDAO.removeFromOrderDetails(orderID, productID);
        response.sendRedirect("Seller?action=viewCart");
    }

    private void increaseQuantity(HttpServletRequest request, HttpServletResponse response, int userID)
            throws IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        int orderID = sellerDAO.getOrCreatePendingOrder(userID);
        Product product = sellerDAO.getProductByID(productID);
        if (product != null) {
            try {
                sellerDAO.addToOrderDetails(orderID, productID, product.getPrice(), 1);
                response.sendRedirect("Seller?action=viewCart");
            } catch (IllegalArgumentException e) {
                response.sendRedirect("Seller?action=viewCart&message=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
        } else {
            response.sendRedirect("Seller?action=viewCart&message=" + java.net.URLEncoder.encode("Sản phẩm không tồn tại.", "UTF-8"));
        }
    }

    private void decreaseQuantity(HttpServletRequest request, HttpServletResponse response, int userID)
            throws IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        int orderID = sellerDAO.getOrCreatePendingOrder(userID);
        sellerDAO.decreaseOrderDetailQuantity(orderID, productID);
        response.sendRedirect("Seller?action=viewCart");
    }

    private void checkout(HttpServletRequest request, HttpServletResponse response, int userID)
            throws IOException, ServletException {
        int orderID = sellerDAO.getOrCreatePendingOrder(userID);
        List<OrderDetail> cartItems = sellerDAO.getOrderDetails(orderID);
        if (cartItems.isEmpty()) {
            response.sendRedirect("Seller?action=viewCart&message=" + java.net.URLEncoder.encode("Giỏ hàng trống.", "UTF-8"));
            return;
        }

        double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double paidAmount = 0.0;
        String paidAmountStr = request.getParameter("paidAmount");
        try {
            paidAmount = paidAmountStr != null ? Double.parseDouble(paidAmountStr) : 0.0;
            if (paidAmount < total) {
                response.sendRedirect("Seller?action=viewCart&message=" + java.net.URLEncoder.encode("Số tiền khách trả không đủ.", "UTF-8"));
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("Seller?action=viewCart&message=" + java.net.URLEncoder.encode("Số tiền thanh toán không hợp lệ.", "UTF-8"));
            return;
        }

        double change = paidAmount - total;

        // Update product quantities
        for (OrderDetail item : cartItems) {
            sellerDAO.updateProductQuantity(item.getProductID(), item.getQuantity());
        }

        // Complete the order
        sellerDAO.completeOrder(orderID);
        Order order = sellerDAO.getOrderByID(orderID);

        request.setAttribute("order", order);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("paidAmount", paidAmount);
        request.setAttribute("change", change);
        request.getRequestDispatcher("/jsp/checkout.jsp").forward(request, response);
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
        if ("addToCart".equals(action)) {
            addToCart(request, response);
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        int userID = (Integer) session.getAttribute("userID");
        int productID = Integer.parseInt(request.getParameter("productID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String searchTerm = request.getParameter("search");
        String page = request.getParameter("page");
        String categoryID = request.getParameter("categoryID");

        Product product = sellerDAO.getProductByID(productID);
        if (product != null) {
            try {
                int orderID = sellerDAO.getOrCreatePendingOrder(userID);
                sellerDAO.addToOrderDetails(orderID, productID, product.getPrice(), quantity);
            } catch (IllegalArgumentException e) {
                response.sendRedirect("Seller?message=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8") + "&search=" + java.net.URLEncoder.encode(searchTerm != null ? searchTerm : "", "UTF-8") + "&page=" + (page != null ? page : "1") + "&categoryID=" + (categoryID != null ? categoryID : "0"));
                return;
            }
        }

        String redirectURL = "Seller?";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            redirectURL += "search=" + java.net.URLEncoder.encode(searchTerm, "UTF-8") + "&";
        }
        if (page != null) {
            redirectURL += "page=" + page + "&";
        }
        if (categoryID != null) {
            redirectURL += "categoryID=" + categoryID;
        }
        response.sendRedirect(redirectURL);
    }
}
