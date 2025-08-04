/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import java.sql.Timestamp;
/**
 *
 * @author Nguyễn Quang Thương
 */
public class Order {
    private int orderID;
    private int userID;
    private int total;
    private Timestamp orderDate;
    private boolean status;
    
    public Order(){
    }

    public Order(int orderID, int userID, int total, Timestamp orderDate, boolean status) {
        this.orderID = orderID;
        this.userID = userID;
        this.total = total;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", userID=" + userID + ", total=" + total + ", orderDate=" + orderDate + ", status=" + status + '}';
    }
    
}
