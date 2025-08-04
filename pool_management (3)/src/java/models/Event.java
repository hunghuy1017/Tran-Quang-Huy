package models;

import java.sql.Date;

public class Event {

    private int eventID;
    private int poolID;
    private int createdBy;
    private Date eventDate;
    private String title;
    private String description;
    private String image;
    private String detailedDescription;
    private Date endDate;
    private int attend;

    public Event() {
    }

    public Event(int eventID, int poolID, int createdBy, Date eventDate, String title, String description, String image, String detailedDescription, Date endDate, int attend) {
        this.eventID = eventID;
        this.poolID = poolID;
        this.createdBy = createdBy;
        this.eventDate = eventDate;
        this.title = title;
        this.description = description;
        this.image = image;
        this.detailedDescription = detailedDescription;
        this.endDate = endDate;
        this.attend = attend;
    }

    public Event(int poolID, int createdBy, Date eventDate, String title, String description, String image, String detailedDescription, Date endDate) {
        this.poolID = poolID;
        this.createdBy = createdBy;
        this.eventDate = eventDate;
        this.title = title;
        this.description = description;
        this.image = image;
        this.detailedDescription = detailedDescription;
        this.endDate = endDate;
    }

    public Event(int eventID, int poolID, int createdBy, Date eventDate, String title, String description, String image) {
        this.eventID = eventID;
        this.poolID = poolID;
        this.createdBy = createdBy;
        this.eventDate = eventDate;
        this.title = title;
        this.description = description;
        this.image = image;
    }
    public Event(int eventID, int poolID, int createdBy, Date eventDate, String title, String description, String image, String detailedDescription, Date endDate) {
        this.eventID = eventID;
        this.poolID = poolID;
        this.createdBy = createdBy;
        this.eventDate = eventDate;
        this.title = title;
        this.description = description;
        this.image = image;
        this.detailedDescription = detailedDescription;
        this.endDate = endDate;
    }
    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getPoolID() {
        return poolID;
    }

    public void setPoolID(int poolID) {
        this.poolID = poolID;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public Date getendDate() {
        return endDate;
    }

    public void setendDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Event{" + "eventID=" + eventID + ", poolID=" + poolID + ", createdBy=" + createdBy + ", eventDate=" + eventDate + ", title=" + title + ", description=" + description + ", image=" + image + ", detailedDescription=" + detailedDescription + ", endDate=" + endDate + '}';
    }
}
