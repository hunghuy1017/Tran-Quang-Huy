/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Hi
 */
public class CalendarEvent {
    // Lớp Event để lưu trữ dữ liệu sự kiện

    private String eventDate;
    private String eventTitle;
    private String eventTheme;
    private String eventType;
    private int eventId;

    public CalendarEvent() {
    }

    public CalendarEvent(String eventDate, String eventTitle, String eventTheme, String eventType, int eventId) {
        this.eventDate = eventDate;
        this.eventTitle = eventTitle;
        this.eventTheme = eventTheme;
        this.eventType = eventType;
        this.eventId = eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTheme() {
        return eventTheme;
    }

    public void setEventTheme(String eventTheme) {
        this.eventTheme = eventTheme;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" + "eventDate=" + eventDate + ", eventTitle=" + eventTitle + ", eventTheme=" + eventTheme + ", eventType=" + eventType + ", eventId=" + eventId + '}';
    }

}
