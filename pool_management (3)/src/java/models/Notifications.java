/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author THIS PC
 */
public class Notifications {
    int NotificationID, UserID;
    String Title, Message;
    Boolean IsRead;
    Timestamp CreatedAt;

    public Notifications() {
    }

    public Notifications(int NotificationID, int UserID, String Title, String Message, Boolean IsRead, Timestamp CreatedAt) {
        this.NotificationID = NotificationID;
        this.UserID = UserID;
        this.Title = Title;
        this.Message = Message;
        this.IsRead = IsRead;
        this.CreatedAt = CreatedAt;
    }

    public Notifications(int UserID, String Title, String Message, Boolean IsRead, Timestamp CreatedAt) {
        this.UserID = UserID;
        this.Title = Title;
        this.Message = Message;
        this.IsRead = IsRead;
        this.CreatedAt = CreatedAt;
    }

    public int getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(int NotificationID) {
        this.NotificationID = NotificationID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Boolean getIsRead() {
        return IsRead;
    }

    public void setIsRead(Boolean IsRead) {
        this.IsRead = IsRead;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Timestamp CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    @Override
    public String toString() {
        return "Notifications{" + "NotificationID=" + NotificationID + ", UserID=" + UserID + ", Title=" + Title + ", Message=" + Message + ", IsRead=" + IsRead + ", CreatedAt=" + CreatedAt + '}';
    }  
}