package controllers;

import dal.ClassDAO;
import dal.TrainerBookingDAO;
import dal.TrainerDAO;
import models.Classes;
import models.Employee;
import models.TrainerBooking;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@WebServlet("/book-trainer")
public class BookTrainerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int trainerID = Integer.parseInt(request.getParameter("trainerID"));

        TrainerDAO trainerDAO = new TrainerDAO();
        Employee trainer = trainerDAO.getTrainerById1(trainerID);

        ClassDAO classDAO = new ClassDAO();
        List<Classes> classList = classDAO.getClassesByTrainerID(trainerID);

        request.setAttribute("trainer", trainer);
        request.setAttribute("classList", classList);

        request.getRequestDispatcher("jsp/BookTrainer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int trainerID = Integer.parseInt(request.getParameter("trainerID"));
        int poolID = Integer.parseInt(request.getParameter("poolID"));
        int userID = Integer.parseInt(request.getParameter("userID"));
        int classID = Integer.parseInt(request.getParameter("classID"));

        Date bookingDate = Date.valueOf(request.getParameter("ngayBatDau"));
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("09:00:00");

        BigDecimal price = new BigDecimal(request.getParameter("price"));
        String userName = request.getParameter("hoTen");
        String note = request.getParameter("mucTieu");

        TrainerBooking booking = new TrainerBooking();
        booking.setUserID(userID);
        booking.setTrainerID(trainerID);
        booking.setPoolID(poolID);
        booking.setClassID(classID);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setBookPrice(price);
        booking.setStatus("Pending");
        booking.setRegisterName(userName);
        booking.setNote(note);

        TrainerBookingDAO bookingDAO = new TrainerBookingDAO();
        int bookingID = bookingDAO.addClassBooking(booking);

        if (bookingID > 0) {
            response.sendRedirect("jsp/payment-page.jsp?bookingID=" + bookingID + "&amount=" + price.toPlainString());
        } else {
            request.setAttribute("errorMessage", "Đặt lịch thất bại. Vui lòng thử lại.");
            request.setAttribute("trainer", new TrainerDAO().getTrainerById1(trainerID));
            request.setAttribute("classList", new ClassDAO().getClassesByTrainerID(trainerID));
            request.getRequestDispatcher("jsp/BookTrainer.jsp").forward(request, response);
        }
    }
}
