/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Hi
 */
public class SwimPackageResponse {
    // Lớp để lưu trữ thông tin gói bơi
   

        int remainingDays;
        String expiryDate;
        boolean isExpiringSoon;
        String packageName;

    public SwimPackageResponse() {
    }

    public SwimPackageResponse(int remainingDays, String expiryDate, boolean isExpiringSoon, String packageName) {
        this.remainingDays = remainingDays;
        this.expiryDate = expiryDate;
        this.isExpiringSoon = isExpiringSoon;
        this.packageName = packageName;
    }

        

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isIsExpiringSoon() {
        return isExpiringSoon;
    }

    public void setIsExpiringSoon(boolean isExpiringSoon) {
        this.isExpiringSoon = isExpiringSoon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return super.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
        
    }

