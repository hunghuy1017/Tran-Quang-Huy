package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class TrainerBooking {

    private int bookingID;          // AUTO_ID (có thể bỏ nếu không dùng)

    /* === Các trường chính === */
    private int userID;
    private int poolID;
    private int trainerID;
    private Integer classID;        // nullable
    private Date bookingDate;
    private Time startTime;
    private Time endTime;
    private BigDecimal bookPrice;
    private String status;
    private String registerName;
    private String note;

    public TrainerBooking() {
    }

    public TrainerBooking(int userID, int poolID, int trainerID, Integer classID,
            Date bookingDate, Time startTime, Time endTime,
            BigDecimal bookPrice, String status,
            String registerName, String note) {
        this.userID = userID;
        this.poolID = poolID;
        this.trainerID = trainerID;
        this.classID = classID;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookPrice = bookPrice;
        this.status = status;
        this.registerName = registerName;
        this.note = note;
    }

    public TrainerBooking(int bookingID, int userID, int poolID, int trainerID, Date bookingDate, String userName, String note) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.poolID = poolID;
        this.trainerID = trainerID;
        this.bookingDate = bookingDate;
        this.registerName = userName;
        this.note = note;
    }

    public TrainerBooking(int userID, int poolID, int trainerID, Date bookingDate, String userName, String note) {
        this.userID = userID;
        this.poolID = poolID;
        this.trainerID = trainerID;
        this.bookingDate = bookingDate;
        this.registerName = userName;
        this.note = note;
    }

    public void setClassID(Integer classID) {
        this.classID = classID;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getClassID() {
        return classID;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public String getStatus() {
        return status;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPoolID() {
        return poolID;
    }

    public void setPoolID(int poolID) {
        this.poolID = poolID;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String userName) {
        this.registerName = userName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "TrainerBooking{"
                + "bookingID=" + bookingID
                + ", userID=" + userID
                + ", poolID=" + poolID
                + ", trainerID=" + trainerID
                + ", classID=" + classID
                + ", bookingDate=" + bookingDate
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + ", bookPrice=" + bookPrice
                + ", status='" + status + '\''
                + ", registerName='" + registerName + '\''
                + ", note='" + note + '\''
                + '}';
    }

}
