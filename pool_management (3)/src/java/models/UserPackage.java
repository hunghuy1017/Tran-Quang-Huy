/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author LENOVO
 */
public class UserPackage {
    private int userPackage;
    private int userID;
    private int PoolID;

    private int packageID;
    private Timestamp purchaseDate;
    private Date startDate;
    private Date endTime;
    private boolean isActive;
    private String paymentStatus;

    public UserPackage() {
    }
    public UserPackage(int userPackage, int userID, int PoolID, int packageID, Timestamp purchaseDate, Date startDate, Date endTime, boolean isActive, String paymentStatus) {
        this.userPackage = userPackage;
        this.userID = userID;
        this.PoolID = PoolID;
        this.packageID = packageID;
        this.purchaseDate = purchaseDate;
        this.startDate = startDate;
        this.endTime = endTime;
        this.isActive = isActive;
        this.paymentStatus = paymentStatus;
    }

    public UserPackage(int userID, int PoolID, int packageID, Timestamp purchaseDate, Date startDate, Date endTime, boolean isActive, String paymentStatus) {
        this.userID = userID;
        this.PoolID = PoolID;
        this.packageID = packageID;
        this.purchaseDate = purchaseDate;
        this.startDate = startDate;
        this.endTime = endTime;
        this.isActive = isActive;
        this.paymentStatus = paymentStatus;
    }
    public UserPackage(int userPackage, int userID, int packageID, Date startDate, Date endTime) {
        this.userPackage = userPackage;
        this.userID = userID;
        this.packageID = packageID;
        this.startDate = startDate;
        this.endTime = endTime;
    }

    public int getPoolID() {
        return PoolID;
    }

    public void setPoolID(int PoolID) {
        this.PoolID = PoolID;
    }

    

    public int getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(int userPackage) {
        this.userPackage = userPackage;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "UserPackage{" + "userPackage=" + userPackage + ", userID=" + userID + ", packageID=" + packageID + ", purchaseDate=" + purchaseDate + ", startDate=" + startDate + ", endTime=" + endTime + ", isActive=" + isActive + ", paymentStatus=" + paymentStatus + '}';
    }
    
}
