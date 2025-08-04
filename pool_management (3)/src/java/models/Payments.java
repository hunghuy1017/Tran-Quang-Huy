/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


import java.util.Date;/**
 *
 * @author LENOVO
 */
public class Payments {
    private int PaymentID;
    private int UserID;
    private int PackageID;
    private int PoolID;
    private String PaymentMethod;
    private double Total;
    private String Status;
    private Date PaymentTime;

    public Payments() {
    }

    public Payments(int PaymentID, int UserID, int PackageID, String PaymentMethod, double Total, String Status, Date PaymentTime) {
        this.PaymentID = PaymentID;
        this.UserID = UserID;
        this.PackageID = PackageID;
        this.PaymentMethod = PaymentMethod;
        this.Total = Total;
        this.Status = Status;
        this.PaymentTime = PaymentTime;
    }

    public Payments(int UserID, int PackageID, String PaymentMethod, double Total, String Status, Date PaymentTime) {
        this.UserID = UserID;
        this.PackageID = PackageID;
        this.PaymentMethod = PaymentMethod;
        this.Total = Total;
        this.Status = Status;
        this.PaymentTime = PaymentTime;
    }
    
   
    public Payments(int PaymentID, int UserID, int PackageID, int PoolID, String PaymentMethod, double Total, String Status, Date PaymentTime) {
        this.PaymentID = PaymentID;
        this.UserID = UserID;
        this.PackageID = PackageID;
        this.PoolID = PoolID;
        this.PaymentMethod = PaymentMethod;
        this.Total = Total;
        this.Status = Status;
        this.PaymentTime = PaymentTime;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int PaymentID) {
        this.PaymentID = PaymentID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public int getPackageID() {
        return PackageID;
    }

    public void setPackageID(int PackageID) {
        this.PackageID = PackageID;
    }

    public int getPoolID() {
        return PoolID;
    }

    public void setPoolID(int PoolID) {
        this.PoolID = PoolID;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String PaymentMethod) {
        this.PaymentMethod = PaymentMethod;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Date getPaymentTime() {
        return PaymentTime;
    }

    public void setPaymentTime(Date PaymentTime) {
        this.PaymentTime = PaymentTime;
    }

    @Override
    public String toString() {
        return "Payments{" + "PaymentID=" + PaymentID + ", UserID=" + UserID + ", PackageID=" + PackageID + ", PoolID=" + PoolID + ", PaymentMethod=" + PaymentMethod + ", Total=" + Total + ", Status=" + Status + ", PaymentTime=" + PaymentTime + '}';
    }

}
