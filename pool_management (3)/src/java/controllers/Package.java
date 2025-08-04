package controllers;

import dal.PackageDAO;
import dal.UserPackageDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import models.Packages;
import models.PackagesEvent;
import models.Users;
import dal.SwimmingPoolDAO;
import models.SwimmingPool;

@WebServlet(name = "Package", urlPatterns = {"/Package"})
public class Package extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Package.class.getName());

    private PackageDAO packageDAO;
    private UserPackageDAO userPackageDAO;
    private SwimmingPoolDAO swimmingPoolDAO;

    @Override
    public void init() throws ServletException {
        packageDAO = new PackageDAO();
        userPackageDAO = new UserPackageDAO();
        swimmingPoolDAO = new SwimmingPoolDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");

        if (service == null) {
            service = "listPackages";
        }

        try {
            switch (service) {
                case "listPackages":
                    handleListPackages(request, response);
                    break;
                case "buyPackage":
                    handleBuyPackage(request, response, session);
                    break;
                default:
                    LOGGER.warning("Dịch vụ không xác định: " + service);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Dịch vụ không tìm thấy.");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("mess", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }

    private void handleListPackages(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        String sortOrder = request.getParameter("sort");
        String eventType = request.getParameter("eventType");
        String poolIdStr = request.getParameter("poolId");
        List<Packages> packagesToDisplay = new ArrayList<>();
        List<SwimmingPool> allPools = swimmingPoolDAO.getAllPools();
        request.setAttribute("allPools", allPools);

        int selectedPoolId = 0;
        if ("all".equals(eventType)) {
            selectedPoolId = 0;
            poolIdStr = null;
        } else if (poolIdStr != null && !poolIdStr.trim().isEmpty()) {
            try {
                selectedPoolId = Integer.parseInt(poolIdStr);
            } catch (NumberFormatException e) {
                LOGGER.warning("Tham số PoolID không hợp lệ: " + poolIdStr);
            }
        }
        request.setAttribute("selectedPoolId", selectedPoolId);

        if (selectedPoolId > 0) {
            packagesToDisplay.addAll(swimmingPoolDAO.getPackagesByPoolId(selectedPoolId));
            Vector<PackagesEvent> eventPackagesFromDB = packageDAO.getAllPackageEvent();
            if (eventPackagesFromDB != null) {
                for (PackagesEvent pe : eventPackagesFromDB) {
                }
            }
        } else {
            String regularPackageSql = "SELECT * FROM Package";
            Vector<Packages> regularPackagesFromDB = packageDAO.getAllPackage(regularPackageSql);
            if (regularPackagesFromDB != null) {
                packagesToDisplay.addAll(regularPackagesFromDB);
            }
            Vector<PackagesEvent> eventPackagesFromDB = packageDAO.getAllPackageEvent();
            if (eventPackagesFromDB != null) {
                for (PackagesEvent pe : eventPackagesFromDB) {
                    packagesToDisplay.add(new Packages(
                            pe.getPackageID(),
                            pe.getPackageName(),
                            0,
                            pe.getPrice(),
                            pe.getPackageDescription(),
                            pe.isAvailabilityStatus(),
                            null
                    ));
                }
            }
        }
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String lowerCaseSearchQuery = searchQuery.toLowerCase();
            packagesToDisplay.removeIf(p -> !(p.getPackageName().toLowerCase().contains(lowerCaseSearchQuery)
                    || (p.getDescription() != null && p.getDescription().toLowerCase().contains(lowerCaseSearchQuery))));
        }
        if ("regular".equals(eventType)) {
            packagesToDisplay.removeIf(p -> p.getDurationInDays() == 0);
        } else if ("event".equals(eventType)) {
            packagesToDisplay.removeIf(p -> p.getDurationInDays() > 0);
        }
        if ("asc".equals(sortOrder)) {
            Collections.sort(packagesToDisplay, Comparator.comparingDouble(Packages::getPrice));
        } else if ("desc".equals(sortOrder)) {
            Collections.sort(packagesToDisplay, Comparator.comparingDouble(Packages::getPrice).reversed());
        }
        int page = 1;
        int recordsPerPage = 6;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                LOGGER.warning("Tham số trang không hợp lệ: " + request.getParameter("page"));
                page = 1;
            }
        }
        int totalRecords = packagesToDisplay.size();
        int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Packages> packagesForCurrentPage = new ArrayList<>();
        if (start < totalRecords) {
            packagesForCurrentPage = packagesToDisplay.subList(start, end);
        }
        request.setAttribute("packagesDisplay", packagesForCurrentPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("jsp/Package.jsp").forward(request, response);
    }

    private void handleBuyPackage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        String packageId = request.getParameter("packageId");
        Packages selectedPackage = packageDAO.getPackageById(packageId);
        if (selectedPackage == null) {
            PackagesEvent selectedEventPackage = packageDAO.getPackageEventById(packageId);
            if (selectedEventPackage != null) {
                selectedPackage = new Packages(
                        selectedEventPackage.getPackageID(),
                        selectedEventPackage.getPackageName(),
                        0,
                        selectedEventPackage.getPrice(),
                        selectedEventPackage.getPackageDescription(),
                        selectedEventPackage.isAvailabilityStatus(),
                        null
                );
            }
        }
        if (selectedPackage == null) {
            request.setAttribute("mess", "Gói không tồn tại!");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
            return;
        }
        Users user = (Users) session.getAttribute("user");
        String userEmail = (user != null) ? user.getEmail() : null;
        request.setAttribute("selectedPackageId", selectedPackage.getPackageID());
        request.setAttribute("selectedPackageName", selectedPackage.getPackageName());
        request.setAttribute("selectedPackagePrice", selectedPackage.getPrice());
        request.setAttribute("selectedPackageDuration", selectedPackage.getDurationInDays());
        request.setAttribute("userEmail", userEmail);
        request.getRequestDispatcher("jsp/userPayment.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Mô tả ngắn gọn";
    }

}