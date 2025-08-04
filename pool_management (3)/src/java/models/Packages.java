/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;
import java.sql.Date;

/**
 *
 * @author LENOVO
 */
public class Packages {
    private int packageID;
        private String packageName;
    private int durationInDays;
    private double price;
    private String description;
    private boolean isActive;
    private Date createdAt;

    public Packages() {
    }

    public Packages(int packageID, String packageName, int durationInDays, double price, String description, boolean isActive, Date createdAt) {
        this.packageID = packageID;
        this.packageName = packageName;
        this.durationInDays = durationInDays;
        this.price = price;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Packages{" + "packageID=" + packageID + ", packageName=" + packageName + ", durationInDays=" + durationInDays + ", price=" + price + ", description=" + description + ", isActive=" + isActive + ", createdAt=" + createdAt + '}';
    }

}
