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
public class RevenueProduct {
    private String userName, productName;
    private int quantity;
    private Boolean status;
    private double total;
    private Timestamp orderDate;
    private String swimmingpool;

    public RevenueProduct() {
    }

    public RevenueProduct(String userName, String productName, int quantity, Boolean status, double total, Timestamp orderDate, String swimmingpool) {
        this.userName = userName;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
        this.total = total;
        this.orderDate = orderDate;
        this.swimmingpool = swimmingpool;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getSwimmingpool() {
        return swimmingpool;
    }

    public void setSwimmingpool(String swimmingpool) {
        this.swimmingpool = swimmingpool;
    }
    
    
}
