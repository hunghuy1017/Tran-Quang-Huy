package models;

import java.sql.Date;

public class EventForm {
    private int registrationID;
    private int eventID;
    private int userID;
    private Date registeredAt;
    private String status;
    private String note;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String eventTitle;
private String poolName;


    public EventForm() {}

    public EventForm(int registrationID, int eventID, int userID, Date registeredAt, String status, String note, String fullName, String phone, String email, String address) {
        this.registrationID = registrationID;
        this.eventID = eventID;
        this.userID = userID;
        this.registeredAt = registeredAt;
        this.status = status;
        this.note = note;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public EventForm(int registrationID, int eventID, int userID, Date registeredAt, String status, String note, String fullName, String phone, String email, String address, String eventTitle, String poolName) {
        this.registrationID = registrationID;
        this.eventID = eventID;
        this.userID = userID;
        this.registeredAt = registeredAt;
        this.status = status;
        this.note = note;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.eventTitle = eventTitle;
        this.poolName = poolName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
    
    

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "EventForm{" + "registrationID=" + registrationID + ", eventID=" + eventID + ", userID=" + userID + ", registeredAt=" + registeredAt + ", status=" + status + ", note=" + note + ", fullName=" + fullName + ", phone=" + phone + ", email=" + email + ", address=" + address + '}';
    }

    

    
}
