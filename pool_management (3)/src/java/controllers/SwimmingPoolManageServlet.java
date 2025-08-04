package controllers;

import dal.SwimmingPoolDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import models.SwimmingPool;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.List;

@WebServlet(name = "SwimmingPoolManageServlet", urlPatterns = {"/SwimmingPoolManageServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 3, // 3MB
    maxFileSize = 1024 * 1024 * 40, // 40MB
    maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class SwimmingPoolManageServlet extends HttpServlet {

    private SwimmingPoolDAO swimmingPoolDAO;
    private static final String UPLOAD_DIRECTORY = "images/pools";

    @Override
    public void init() throws ServletException {
        swimmingPoolDAO = new SwimmingPoolDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("Action received: " + action);

        try {
            if (action == null) {
                String keyword = request.getParameter("keyword");
                String status = request.getParameter("status");
                System.out.println("Keyword: " + keyword + ", Status: " + status);
                List<SwimmingPool> poolList = swimmingPoolDAO.searchPools(keyword, status);
                request.setAttribute("poolList", poolList);
                request.getRequestDispatcher("jsp/swimmingPoolManage.jsp").forward(request, response);
            } else if (action.equals("toggleStatus")) {
                int poolID = Integer.parseInt(request.getParameter("poolID"));
                System.out.println("Toggling status for PoolID: " + poolID);
                swimmingPoolDAO.toggleStatus(poolID);
                response.sendRedirect(request.getContextPath() + "/SwimmingPoolManageServlet");
            } else if (action.equals("delete")) {
                int poolID = Integer.parseInt(request.getParameter("poolID"));
                System.out.println("Deleting PoolID: " + poolID);
                swimmingPoolDAO.deletePool(poolID);
                response.sendRedirect(request.getContextPath() + "/SwimmingPoolManageServlet");
            } else {
                System.out.println("Unknown action: " + action);
                request.setAttribute("error", "Invalid action requested.");
                request.getRequestDispatcher("jsp/swimmingPoolManage.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("jsp/swimmingPoolManage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                System.out.println("Created upload directory: " + uploadPath);
            }

            SwimmingPool pool = new SwimmingPool();
            String action = null;
            String poolID = null;

            for (Part part : request.getParts()) {
                String fieldName = part.getName();
                if (part.getContentType() == null) { // Form field
                    String fieldValue = request.getParameter(fieldName);
                    System.out.println("Field: " + fieldName + ", Value: " + fieldValue);
                    switch (fieldName) {
                        case "action":
                            action = fieldValue;
                            break;
                        case "poolID":
                            poolID = fieldValue;
                            break;
                        case "name":
                            pool.setName(fieldValue);
                            break;
                        case "location":
                            pool.setLocation(fieldValue);
                            break;
                        case "phone":
                            pool.setPhone(fieldValue);
                            break;
                        case "fanpage":
                            pool.setFanpage(fieldValue);
                            break;
                        case "openTime":
                            pool.setOpenTime(fieldValue != null && !fieldValue.isEmpty() ? Time.valueOf(fieldValue + ":00") : null);
                            break;
                        case "closeTime":
                            pool.setCloseTime(fieldValue != null && !fieldValue.isEmpty() ? Time.valueOf(fieldValue + ":00") : null);
                            break;
                        case "description":
                            pool.setDescription(fieldValue);
                            break;
                        case "status":
                            pool.setStatus("1".equals(fieldValue));
                            break;
                    }
                } else if (fieldName.equals("image") && part.getSize() > 0) {
                    String fileName = part.getSubmittedFileName();
                    String filePath = uploadPath + File.separator + fileName;
                    System.out.println("Uploading file: " + fileName + " to " + filePath);
                    part.write(filePath);
                    pool.setImage(fileName);
                }
            }

            if (action != null) {
                if (action.equals("add")) {
                    System.out.println("Adding new pool: " + pool.getName());
                    swimmingPoolDAO.addPool(pool);
                } else if (action.equals("update")) {
                    pool.setPoolID(Integer.parseInt(poolID));
                    System.out.println("Updating pool ID: " + poolID);
                    swimmingPoolDAO.updatePool(pool);
                } else {
                    System.out.println("Unknown action in POST: " + action);
                }
            }
            response.sendRedirect(request.getContextPath() + "/SwimmingPoolManageServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error uploading file or saving data: " + e.getMessage());
            request.getRequestDispatcher("jsp/swimmingPoolManage.jsp").forward(request, response);
        }
    }
}