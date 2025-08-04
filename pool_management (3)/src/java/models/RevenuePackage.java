/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;

/**
 *
 * @author THIS PC
 */
public class RevenuePackage {
    String userName, packageName, swimmingpool, paymentMethod;
    double total;
    String status;
    Timestamp paymentTime;

    public RevenuePackage() {
    }

    public RevenuePackage(String userName, String packageName, String swimmingpool, String paymentMethod, double total, String status, Timestamp paymentTime) {
        this.userName = userName;
        this.packageName = packageName;
        this.swimmingpool = swimmingpool;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.status = status;
        this.paymentTime = paymentTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSwimmingpool() {
        return swimmingpool;
    }

    public void setSwimmingpool(String swimmingpool) {
        this.swimmingpool = swimmingpool;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    
}
