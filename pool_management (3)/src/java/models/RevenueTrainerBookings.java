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
public class RevenueTrainerBookings {
    String userName, trainerName, swimmingPool, className, paymentMethod;
    double total;
    Timestamp paymentDate;

    public RevenueTrainerBookings() {
    }

    public RevenueTrainerBookings(String userName, String trainerName, String swimmingPool, String className, String paymentMethod, double total, Timestamp paymentDate) {
        this.userName = userName;
        this.trainerName = trainerName;
        this.swimmingPool = swimmingPool;
        this.className = className;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.paymentDate = paymentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(String swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}
