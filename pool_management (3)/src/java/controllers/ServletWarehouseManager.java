/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.ProductDAO;
import dal.SellerDAO;
import dal.SwimmingPoolDAO;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import models.Employee;
import models.Product;
import models.SwimmingPool;
import models.Users;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;

/**
 *
 * @author THIS PC
 */
// cần nếu sử lý file
//1024	Là số byte trong 1 kilobyte (KB)
//1024 * 1024 Là số byte trong 1 megabyte (MB), tức là 1,048,576 byte
@MultipartConfig

@WebServlet(name = "ServletWarehouseManager", urlPatterns = {"/ServletWarehouseManager"})
public class ServletWarehouseManager extends HttpServlet {

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
            service = "listProduct";
        }

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        int UserID = user.getUserID();
        int RoleID = user.getRoleID();

        SwimmingPoolDAO swDAO = new SwimmingPoolDAO();
        ProductDAO PDAO = new ProductDAO();
        SellerDAO sellDAO = new SellerDAO();

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

            if (!position.equals("Manager") && !position.equals("Warehouse Management")) {
                response.sendRedirect("jsp/LoginUser.jsp");
                return;
            }
        }

        if (service.equals("DeleteProduct")) {
            int productID = Integer.parseInt(request.getParameter("pID"));
            PDAO.deleteProduct(productID);
            response.sendRedirect("ServletWarehouseManager");
        }

        if (service.equals("UpdateProduct")) {
            String submit = request.getParameter("submit");

            if (submit == null) {
                // Lấy ID sản phẩm cần cập nhật
                int productID = Integer.parseInt(request.getParameter("pID"));
                Product product = PDAO.getProductByID(productID);

                request.setAttribute("poolID", poolID);
                request.setAttribute("position", position);
                request.setAttribute("product", product);
                request.setAttribute("pools", swDAO.getAllPools());
                request.setAttribute("Categories", sellDAO.getAllCategories());

                request.getRequestDispatcher("jsp/UpdateProduct.jsp").forward(request, response);
            } else {
                int productID = Integer.parseInt(request.getParameter("pID"));
                poolID = Integer.parseInt(request.getParameter("poolID"));
                String name = request.getParameter("productName");
                String desc = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String addedDateStr = request.getParameter("addedDate");
                LocalDateTime ldt = LocalDateTime.parse(addedDateStr);
                Timestamp addedDate = Timestamp.valueOf(ldt);
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int CategorieID = Integer.parseInt(request.getParameter("Categories"));
                boolean status = Boolean.parseBoolean(request.getParameter("status"));
                double RentalPrice = Double.parseDouble(request.getParameter("RentalPrice"));
                boolean IsRentable = Boolean.parseBoolean(request.getParameter("IsRentable"));

                
                String uploadPath = getServletContext().getRealPath("/images/products");
                String imagePath = null;

                Part filePart = request.getPart("image");
                if (filePart != null && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isEmpty()) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    filePart.write(uploadPath + File.separator + fileName);
                    imagePath = fileName;
                } else {
                    
                    imagePath = request.getParameter("imageOld");
                }

                Product updatedProduct = new Product(productID, poolID, name, desc, price, addedDate, quantity, imagePath, status, CategorieID, IsRentable, RentalPrice);
                PDAO.updateProduct(updatedProduct);

                response.sendRedirect("ServletWarehouseManager");
            }
        }

        if (service.equals("AddProduct")) {
            String submit = request.getParameter("submit");

            if (submit == null) {
                request.setAttribute("poolID", poolID);
                request.setAttribute("position", position);
                request.setAttribute("pools", swDAO.getAllPools());
                request.setAttribute("Categories", sellDAO.getAllCategories());
                request.getRequestDispatcher("jsp/AddProduct.jsp").forward(request, response);
            } else {

                String uploadPath = getServletContext().getRealPath("/images");

                Part filePart = request.getPart("image");
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
                String imagePath = fileName; 

                
                filePart.write(uploadPath + File.separator + fileName);

                poolID = Integer.parseInt(request.getParameter("poolID"));
                String name = request.getParameter("productName");
                String desc = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String addedDateStr = request.getParameter("addedDate");
                LocalDateTime ldt = LocalDateTime.parse(addedDateStr);
                Timestamp addedDate = Timestamp.valueOf(ldt);
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                boolean status = Boolean.parseBoolean(request.getParameter("status"));
                int CategorieID = Integer.parseInt(request.getParameter("Categories"));
                double RentalPrice = Double.parseDouble(request.getParameter("RentalPrice"));
                boolean IsRentable = Boolean.parseBoolean(request.getParameter("IsRentable"));
                Product p = new Product(poolID, name, desc, price, addedDate, quantity, imagePath, status, CategorieID, IsRentable, RentalPrice);

                PDAO.insertProduct(p);
                response.sendRedirect("ServletWarehouseManager"); // hoặc forward lại trang nào đó
            }
        }

        if (service.equals("listProduct")) {

            String poolParam = request.getParameter("poolID");
            if (poolParam != null && !poolParam.isEmpty()) {
                poolID = Integer.parseInt(poolParam);
            }

            String name = request.getParameter("name");
            if (name == null) {
                name = "";
            }

            String status = request.getParameter("status");
            if (status == null) {
                status = "all";
            }

            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }

            int pageSize = 10;
            int offset = (page - 1) * pageSize;

            String managerName = PDAO.getManagerNameByPoolID(poolID);

            ProductDAO dao = new ProductDAO();
            Vector<Product> list = dao.getProductsByFilter(poolID, name, status, offset, pageSize);
            int total = dao.countProductsByFilter(poolID, name, status);
            int totalPages = (int) Math.ceil((double) total / pageSize);

            int totalEmployees = PDAO.countWarehouseEmployees(poolID);
            int totalProducts = PDAO.countProducts(poolID);

            request.setAttribute("products", list);
            request.setAttribute("poolID", poolID);
            request.setAttribute("name", name);
            request.setAttribute("status", status);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("managerName", managerName);
            request.setAttribute("totalEmployees", totalEmployees);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("position", position);
            request.setAttribute("RoleID", RoleID);
            request.setAttribute("Categories", sellDAO.getAllCategories());

            // Danh sách các bể bơi để select
            request.setAttribute("pools", swDAO.getAllPools());

            request.getRequestDispatcher("jsp/WarehouseManager.jsp").forward(request, response);
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
     * quas Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
