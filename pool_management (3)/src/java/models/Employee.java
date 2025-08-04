// --- FILE: models/Employee.java (UPDATED) ---
package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time; // Cần thiết nếu bạn dùng Time.valueOf()

public class Employee {

    private int userID;
    private String Position;
    private String description;
    private Date startDate;
    private Date endDate;
    private int attendance;
    private BigDecimal salary;
    private BigDecimal HourlyRate; // Giá giờ của Trainer
    // Thông tin bổ sung từ bảng Users
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String image;
    // Thông tin bổ sung từ bảng SwimmingPools (nếu JOIN)
    private int poolID;
    private String poolName;
    private int bookingCount;      // Số lần thuê
    private Date lastBookingDate;
    // Các thuộc tính tính toán (ví dụ: AverageRating)
    private BigDecimal averageRating; // THÊM: Sẽ được tính toán từ TrainerReviews

    public Employee() {
    }

    public Employee(String Position, int poolID) {
        this.Position = Position;
        this.poolID = poolID;
    }
    
    public Employee(int userID, String description, Date startDate, Date endDate, int attendance, BigDecimal salary, String fullName, String email, String phone, String address, String image) {
        this.userID = userID;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendance = attendance;
        this.salary = salary;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public Employee(int userID, String description, Date startDate, Date endDate, int attendance, BigDecimal salary,
            String fullName, String email, String phone, String address, String image, int poolID) {
        this.userID = userID;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendance = attendance;
        this.salary = salary;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.poolID = poolID;
    }

    // Constructor đã được cập nhật để bao gồm tất cả các trường bạn có
    // và các trường bổ sung như Position, HourlyRate, PoolName, AverageRating.
    // Nếu bạn không muốn constructor quá dài, có thể dùng setters sau khi khởi tạo mặc định.
    public Employee(int userID, String Position, String description, Date startDate, Date endDate,
            int attendance, BigDecimal salary, int poolID, BigDecimal HourlyRate,
            String fullName, String email, String phone, String address, String image,
            String poolName, BigDecimal averageRating) {
        this.userID = userID;
        this.Position = Position;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendance = attendance;
        this.salary = salary;
        this.poolID = poolID;
        this.HourlyRate = HourlyRate;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.poolName = poolName;
        this.averageRating = averageRating;
    }

    // Getters và Setters (đã sắp xếp lại và thêm cái bị thiếu)
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public int getPoolID() {
        return poolID;
    }

    public void setPoolID(int poolID) {
        this.poolID = poolID;
    }

    public BigDecimal getHourlyRate() {
        return HourlyRate;
    }

    public void setHourlyRate(BigDecimal HourlyRate) {
        this.HourlyRate = HourlyRate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    // --- THÊM GETTERS VÀ SETTERS CHO CÁC TRƯỜNG MỚI NÀY ---
    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    public Date getLastBookingDate() {
        return lastBookingDate;
    }

    public void setLastBookingDate(Date lastBookingDate) {
        this.lastBookingDate = lastBookingDate;
    }

    @Override
    public String toString() {
        return "Employee{"
                + "userID=" + userID
                + ", Position='" + Position + '\''
                + ", description='" + description + '\''
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", attendance=" + attendance
                + ", salary=" + salary
                + ", poolID=" + poolID
                + ", HourlyRate=" + HourlyRate
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", address='" + address + '\''
                + ", image='" + image + '\''
                + ", poolName='" + poolName + '\''
                + ", averageRating=" + averageRating
                + '}';
    }
}
