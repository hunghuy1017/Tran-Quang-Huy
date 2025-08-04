package controllers;

import dal.EventDAO;
import dal.EventFormDAO;
import models.Event;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import models.EventForm;

@WebServlet(name = "Event", urlPatterns = {"/Event"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class ServletEvent extends HttpServlet {

    private EventDAO dao = new EventDAO();
    private EventFormDAO formDao = new EventFormDAO();
    private static final int PAGE_SIZE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "EventDetail":
                handleEventDetail(request, response);
                break;
            case "delete":
                handleDeleteEvent(request, response);
                break;
            case "add":
                handleAddEvent(request, response);
                break;
            case "edit":
                handleEditEvent(request, response);
                break;
            case "registrationList":
                handleRegistrationList(request, response);
                break;
            default:
                handleEventList(request, response);
        }
    }

    private int getCurrentPage(HttpServletRequest request) {
        try {
            String pageParam = request.getParameter("page");
            return (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void handleEventDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Event event = dao.getEventById(id);
            if (event != null) {
                request.setAttribute("event", event);
                request.getRequestDispatcher("jsp/EventDetail.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Sự kiện không tồn tại.");
                request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sự kiện không hợp lệ.");
            request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
        }
    }

    private void handleDeleteEvent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int page = getCurrentPage(request);
        if (isAdmin(request)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                // Kiểm tra xem sự kiện có bản ghi Approved hay không
                if (dao.hasApprovedRegistrations(id)) {
                    request.getSession().setAttribute("error", "Không thể xóa sự kiện vì đã có người dùng được phê duyệt.");
                } else if (dao.deleteEvent(id)) {
                    request.getSession().setAttribute("message", "Xóa sự kiện thành công!");
                } else {
                    request.getSession().setAttribute("error", "Không thể xóa sự kiện.");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "ID không hợp lệ.");
            }
        } else {
            request.getSession().setAttribute("error", "Bạn không có quyền xóa sự kiện.");
        }
        response.sendRedirect("Event?page=" + page);
    }

    private void handleAddEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isAdmin(request)) {
            request.setAttribute("event", new Event());
            request.getRequestDispatcher("jsp/EventForm.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Bạn không có quyền thêm sự kiện.");
            request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
        }
    }

    private void handleEditEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isAdmin(request)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Event event = dao.getEventById(id);
                if (event != null) {
                    request.setAttribute("event", event);
                    request.getRequestDispatcher("jsp/EventForm.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Sự kiện không tồn tại.");
                    request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID sự kiện không hợp lệ.");
                request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Bạn không có quyền sửa sự kiện.");
            request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
        }
    }

    private void handleRegistrationList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isAdmin(request)) {
            String searchEvent = request.getParameter("searchEvent");
            String searchPhone = request.getParameter("searchPhone");
            String searchPool = request.getParameter("searchPool");
            String status = request.getParameter("status");

            try {
                EventFormDAO formDao = new EventFormDAO();
                List<EventForm> registrations = formDao.getAllRegistrationsFiltered(searchEvent, searchPhone, searchPool, status);

                request.setAttribute("registrations", registrations);
                request.setAttribute("searchEvent", searchEvent);
                request.setAttribute("searchPhone", searchPhone);
                request.setAttribute("searchPool", searchPool);
                request.setAttribute("status", status);
                request.getRequestDispatcher("jsp/EventRegistrationList.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("error", "Lỗi khi lấy danh sách đăng ký.");
                response.sendRedirect("Event");
            }
        } else {
            request.getSession().setAttribute("error", "Bạn không có quyền truy cập.");
            response.sendRedirect("Event");
        }
    }

    private void handleEventList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = getCurrentPage(request);
        int offset = (page - 1) * PAGE_SIZE;

        String eventSearch = request.getParameter("eventSearch");
        String eventMonth = request.getParameter("eventMonth");
        String eventYear = request.getParameter("eventYear");

        List<Event> eventList;
        int totalEvents;
        int totalPages;

        if (eventSearch != null || eventMonth != null || eventYear != null) {
            eventList = dao.searchEventsByKeyword(eventSearch, eventMonth, eventYear, offset, PAGE_SIZE);
            totalEvents = dao.getTotalSearchEvents(eventSearch, eventMonth, eventYear);
        } else {
            eventList = dao.getEventsByPage(offset, PAGE_SIZE);
            totalEvents = dao.getTotalEvents();
        }
        totalPages = (int) Math.ceil((double) totalEvents / PAGE_SIZE);

        request.setAttribute("events", eventList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("eventSearch", eventSearch);
        request.setAttribute("eventMonth", eventMonth);
        request.setAttribute("eventYear", eventYear);
        request.getRequestDispatcher("jsp/EventList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            // Xử lý đăng ký sự kiện (application/x-www-form-urlencoded)
            handleRegistration(request, response);
        } else if ("updateStatus".equals(action)) {
            handleUpdateStatus(request, response);
        } else {
            // Xử lý thêm/sửa sự kiện (multipart/form-data)
            handleEventForm(request, response);
        }
    }

    private void handleEventForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;
        Event existingEvent = (id > 0) ? dao.getEventById(id) : new Event();
        Event e = new Event();
        e.setEventID(id);

        // Giữ giá trị hiện tại trừ khi có giá trị mới
        e.setTitle(request.getParameter("title") != null && !request.getParameter("title").trim().isEmpty()
                ? request.getParameter("title") : existingEvent.getTitle());
        e.setDescription(request.getParameter("description") != null && !request.getParameter("description").trim().isEmpty()
                ? request.getParameter("description") : existingEvent.getDescription());
        e.setDetailedDescription(request.getParameter("detailedDescription") != null && !request.getParameter("detailedDescription").trim().isEmpty()
                ? request.getParameter("detailedDescription") : existingEvent.getDetailedDescription());
        e.setPoolID(request.getParameter("poolID") != null && !request.getParameter("poolID").trim().isEmpty()
                ? Integer.parseInt(request.getParameter("poolID")) : existingEvent.getPoolID());

        // Xử lý ngày
        String eventDateStr = request.getParameter("eventDate");
        e.setEventDate(eventDateStr != null && !eventDateStr.trim().isEmpty()
                ? Date.valueOf(eventDateStr) : existingEvent.getEventDate());
        String endDateStr = request.getParameter("endDate");
        e.setendDate(endDateStr != null && !endDateStr.trim().isEmpty()
                ? Date.valueOf(endDateStr) : existingEvent.getendDate());

        // Xử lý hình ảnh
        Part filePart = request.getPart("image");
        String fileName = "";
        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            if (!fileName.isEmpty()) {
                String uploadPath = getServletContext().getRealPath("/images/event");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);
                e.setImage(fileName);
            } else {
                e.setImage(existingEvent.getImage());
            }
        } else {
            e.setImage(existingEvent.getImage());
        }

        // Gán CreatedBy
        if (id == 0) {
            Integer userId = (Integer) request.getSession().getAttribute("userID");
            if (userId != null) {
                e.setCreatedBy(userId);
            } else {
                request.getSession().setAttribute("error", "Không thể xác định người tạo sự kiện.");
                request.setAttribute("event", e);
                request.getRequestDispatcher("jsp/EventForm.jsp").forward(request, response);
                return;
            }
        } else {
            e.setCreatedBy(existingEvent.getCreatedBy());
        }

        // Lưu sự kiện
        try {
            if (id == 0) {
                dao.addEvent(e);
                request.getSession().setAttribute("message", "Thêm sự kiện thành công!");
            } else {
                int rowsAffected = dao.updateEvent(e, existingEvent.getCreatedBy());
                if (rowsAffected > 0) {
                    request.getSession().setAttribute("message", "Cập nhật sự kiện thành công!");
                } else {
                    request.getSession().setAttribute("error", "Không thể cập nhật sự kiện: Không có thay đổi hoặc lỗi cơ sở dữ liệu.");
                }
            }
            response.sendRedirect("Event");
        } catch (Exception ex) {
            request.getSession().setAttribute("error", "Lỗi khi lưu sự kiện: " + ex.getMessage());
            request.setAttribute("event", e);
            request.getRequestDispatcher("jsp/EventForm.jsp").forward(request, response);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userID = (Integer) request.getSession().getAttribute("userID");
        String eventIDStr = request.getParameter("eventID");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String note = request.getParameter("note");

        if (userID == null || eventIDStr == null || fullName == null || phone == null || email == null || address == null) {
            response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + (eventIDStr != null ? eventIDStr : ""));
            return;
        }

        if (!phone.matches("\\d{10}")) {
            request.getSession().setAttribute("errorMessage", "Số điện thoại phải có đúng 10 chữ số!");
            response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.getSession().setAttribute("errorMessage", "Email không đúng định dạng!");
            response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
            return;
        }

        int eventID;
        try {
            eventID = Integer.parseInt(eventIDStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
            return;
        }
        // Kiểm tra ngày hiện tại có trong khoảng EventDate và EndDate không
        Event event = dao.getEventById(eventID);
        if (event == null) {
            request.getSession().setAttribute("errorMessage", "Sự kiện không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
            return;
        }

        java.util.Date currentDate = new java.util.Date();
        if (currentDate.after(event.getendDate())) {
            request.getSession().setAttribute("errorMessage", "Không thể đăng ký vì sự kiện không trong thời gian cho phép.");
            response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
            return;
        }
        EventFormDAO dao = new EventFormDAO();

        EventForm registration = new EventForm();
        registration.setEventID(eventID);
        registration.setUserID(userID);
        registration.setRegisteredAt(new Date(System.currentTimeMillis()));
        registration.setStatus("Pending");
        registration.setNote(note != null ? note : "");
        registration.setFullName(fullName);
        registration.setPhone(phone);
        registration.setEmail(email);
        registration.setAddress(address);

        try {
            if (dao.isPhoneRegisteredForEvent(eventID, phone)) {
                request.getSession().setAttribute("errorMessage", "Số điện thoại đã đăng ký sự kiện này rồi.");
                response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
                return;
            }
            dao.insertRegistration(registration);
            request.getSession().setAttribute("successMessage", "Đăng ký sự kiện thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Có lỗi xảy ra khi đăng ký.");
        }

        response.sendRedirect(request.getContextPath() + "/Event?action=EventDetail&id=" + eventIDStr);
    }

    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            request.getSession().setAttribute("error", "Bạn không có quyền thực hiện thao tác này.");
            response.sendRedirect("Event");
            return;
        }

        String regIDStr = request.getParameter("registrationID");
        String newStatus = request.getParameter("newStatus");
        String searchEvent = request.getParameter("searchEvent");
        String searchPhone = request.getParameter("searchPhone");
        String searchPool = request.getParameter("searchPool");
        String status = request.getParameter("status");
       

        // Xử lý null cho tham số tìm kiếm
        searchEvent = (searchEvent != null) ? searchEvent : "";
        searchPhone = (searchPhone != null) ? searchPhone : "";
        searchPool = (searchPool != null) ? searchPool : "";
        status = (status != null) ? status : "";
        

        // Kiểm tra tham số
        if (regIDStr == null || newStatus == null) {
            request.getSession().setAttribute("error", "Thiếu thông tin cập nhật (ID hoặc trạng thái).");
            reloadRegistrationList(request, response, searchEvent, searchPhone, searchPool, status);
            return;
        }

        // Kiểm tra trạng thái hợp lệ
        if (!newStatus.equals("Pending") && !newStatus.equals("Approved") && !newStatus.equals("Rejected")) {
            request.getSession().setAttribute("error", "Trạng thái không hợp lệ.");
            reloadRegistrationList(request, response, searchEvent, searchPhone, searchPool, status);
            return;
        }

        try {
            int regID = Integer.parseInt(regIDStr);
            EventFormDAO dao = formDao; // Sử dụng formDao đã khai báo ở cấp lớp
            boolean success = dao.updateRegistrationStatus(regID, newStatus);
            if (success) {
                request.getSession().setAttribute("message", "Cập nhật trạng thái thành công.");
            } else {
                request.getSession().setAttribute("error", "Không tìm thấy đăng ký hoặc lỗi cập nhật.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID đăng ký không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi hệ thống khi cập nhật trạng thái: " + e.getMessage());
        }

        // Tải lại danh sách đăng ký và chuyển tiếp về trang JSP
        reloadRegistrationList(request, response, searchEvent, searchPhone, searchPool, status);
    }

// Phương thức phụ để tải lại danh sách đăng ký và chuyển tiếp
    private void reloadRegistrationList(HttpServletRequest request, HttpServletResponse response,
            String searchEvent, String searchPhone, String searchPool, String status)
            throws ServletException, IOException {
        try {
            List<EventForm> registrations = formDao.getAllRegistrationsFiltered(searchEvent, searchPhone, searchPool, status);
            request.setAttribute("registrations", registrations);
            request.setAttribute("searchEvent", searchEvent);
            request.setAttribute("searchPhone", searchPhone);
            request.setAttribute("searchPool", searchPool);
            request.setAttribute("status", status);
            // Đặt roleID vào request để JSP kiểm tra
            Integer roleID = (Integer) request.getSession().getAttribute("roleID");
            request.setAttribute("roleID", roleID != null ? roleID : 0);
            request.getRequestDispatcher("jsp/EventRegistrationList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi tải danh sách đăng ký: " + e.getMessage());
            response.sendRedirect("Event");
        }
    }

    private boolean isAdmin(HttpServletRequest request) {
        Integer role = (Integer) request.getSession().getAttribute("roleID");
        return role != null && role == 1;
    }
}
