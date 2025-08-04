package controllers;

import dal.EventDAO;
import dal.EventFormDAO;
import dal.PackageDAO;
import dal.TrainerDAO;
import dal.UserPackageDAO;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import models.CalendarEvent;
import models.Event;
import models.EventForm;
import models.Packages;
import models.SwimPackageResponse;
import models.TrainerBooking;
import models.UserPackage;

@WebServlet(name = "CalendarServlet", urlPatterns = {"/CalendarServlet"})
public class CalendarServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CalendarServlet.class.getName());

    private TrainerDAO trainerDAO;
    private UserPackageDAO userPackageDAO;
    private EventDAO eventDAO;
    private EventFormDAO eventFormDAO;
    private PackageDAO packageDAO;

    @Override
    public void init() throws ServletException {
        trainerDAO = new TrainerDAO();
        userPackageDAO = new UserPackageDAO();
        eventDAO = new EventDAO();
        eventFormDAO = new EventFormDAO();
        packageDAO = new PackageDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");

        if (service == null) {
            service = "showCalendar";
        }

        try {
            switch (service) {
                case "showCalendar":
                    handleShowCalendar(request, response, session);
                    break;

                case "requestChangeLesson":
                    handleRequestChangeLesson(request, response, session);
                    break;
                case "rateTrainer":
                    handleRateTrainer(request, response, session);
                    break;
                default:
                    LOGGER.warning("Unknown service: " + service);
                    response.sendRedirect("jsp/eror.jsp");
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Error processing request: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mess", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("jsp/eor.jsp").forward(request, response);
        }
    }

    private void handleShowCalendar(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int userId = getUserIdFromSession(session);
        if (userId == 0) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        // Lấy tháng và năm từ request (nếu có)
        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");

        Calendar cal = Calendar.getInstance();
        int month = (monthStr != null) ? Integer.parseInt(monthStr) : cal.get(Calendar.MONTH);
        int year = (yearStr != null) ? Integer.parseInt(yearStr) : cal.get(Calendar.YEAR);
        if (month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR)) {
            System.out.println("Hiển thị tháng hiện tại: " + (month + 1) + "/" + year);
        }
        // Xử lý trường hợp chuyển tháng ngoài 0-11
        if (month < 0) {
            month = 11;
            year--;
        } else if (month > 11) {
            month = 0;
            year++;
        }

        // Tính lịch tháng
        cal.set(year, month, 1); // ngày đầu tháng
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Tạo list ngày trống đầu tháng (blank cells)
        List<Integer> blankDays = new ArrayList<>();
        for (int i = 0; i < firstDayOfWeek; i++) {
            blankDays.add(i);
        }

        // Tạo list ngày trong tháng
        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }

        // Danh sách sự kiện
        List<CalendarEvent> events = new ArrayList<>();

        // Lịch học với HLV
        Vector<TrainerBooking> trainerBookings = trainerDAO.getByUserIDAndTrainerID(userId, 0);
        if (trainerBookings != null) {
            for (TrainerBooking tb : trainerBookings) {
                events.add(new CalendarEvent(
                        formatDate(tb.getBookingDate()),
                        "Swim Lesson: " + tb.getRegisterName(),
                        "blue",
                        "lesson",
                        tb.getBookingID()
                ));
            }
        }

        List<EventForm> eventRegistrations = eventFormDAO.getRegistrationsByUserId(userId);
        System.out.println("UserID: " + userId + ", Số đăng ký sự kiện: " + eventRegistrations.size());
        for (EventForm er : eventRegistrations) {
            System.out.println("EventID: " + er.getEventID());
            Event event = eventDAO.getEventById(er.getEventID());
            if (event != null) {
                String formattedDate = formatDate(event.getEventDate());
                System.out.println("Sự kiện: " + event.getTitle() + ", Ngày: " + formattedDate);
                events.add(new CalendarEvent(
                        formattedDate,
                        event.getTitle(),
                        "red",
                        "party",
                        event.getEventID()
                ));
            } else {
                System.out.println("Không tìm thấy sự kiện với EventID: " + er.getEventID());
            }
        }
        System.out.println("Tổng số sự kiện thêm vào: " + events.size());

        // Gói bơi tự do
        UserPackageDAO userPackageDAO = new UserPackageDAO();
        UserPackage userPackage = userPackageDAO.searchLatestUserPackageByUserID(userId);
        SwimPackageResponse swimPackage = null;

        if (userPackage != null && userPackage.getIsActive()) {
            PackageDAO packageDAO = new PackageDAO();
            Packages pkg = packageDAO.getPackageByID1(userPackage.getPackageID());
            if (pkg != null) {
                long remainingDays = calculateRemainingDays(userPackage.getEndTime());
                boolean isExpiringSoon = remainingDays <= 7;
                swimPackage = new SwimPackageResponse(
                        (int) remainingDays,
                        formatDate(userPackage.getEndTime()),
                        isExpiringSoon,
                        pkg.getPackageName()
                );
            }
        }

        // Ngày trong tuần
        List<String> daysOfWeek = Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

        // Truyền dữ liệu về JSP
        request.setAttribute("month", month);
        request.setAttribute("year", year);
        request.setAttribute("blankDays", blankDays);
        request.setAttribute("days", days);
        request.setAttribute("daysOfWeek", daysOfWeek);
        request.setAttribute("events", events);
        request.setAttribute("swimPackage", swimPackage);

        request.getRequestDispatcher("jsp/Calendar.jsp").forward(request, response);
    }

// Hàm tiện ích format date
    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private long calculateRemainingDays(Date endDate) { // Đổi tên tham số
        if (endDate == null) {
            return 0;
        }
        long diff = endDate.getTime() - System.currentTimeMillis();
        return diff > 0 ? diff / (1000 * 60 * 60 * 24) : 0;
    }

    private void handleRequestChangeLesson(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int userId = getUserIdFromSession(session);
        if (userId == 0) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        String bookingId = request.getParameter("booking_id");
        String newDate = request.getParameter("new_date");

        if (bookingId == null || newDate == null) {
            request.setAttribute("mess", "Missing required fields!");
            handleShowCalendar(request, response, session);
            return;
        }

        // Giả lập, cần bảng LessonChangeRequests
        request.setAttribute("mess", "Lesson change request sent!");
        handleShowCalendar(request, response, session);
    }

    private void handleRateTrainer(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int userId = getUserIdFromSession(session);
        if (userId == 0) {
            response.sendRedirect("jsp/LoginUser.jsp");
            return;
        }

        String bookingId = request.getParameter("booking_id");
        String rating = request.getParameter("rating");
        String comment = request.getParameter("comment");

        if (bookingId == null || rating == null) {
            request.setAttribute("mess", "Missing required fields!");
            handleShowCalendar(request, response, session);
            return;
        }

        // Giả lập, cần bảng TrainerRatings
        request.setAttribute("mess", "Trainer rating submitted!");
        handleShowCalendar(request, response, session);
    }

    private int getUserIdFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userID");
        return userId != null ? userId : 0;
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
        return "Servlet for managing calendar in BluePool application";
    }

}
