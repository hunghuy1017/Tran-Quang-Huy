/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author THIS PC
 */
public class RevenuePool {
    String swimmingpool;
    Double revenue;

    public RevenuePool() {
    }

    public RevenuePool(String swimmingpool, Double revenue) {
        this.swimmingpool = swimmingpool;
        this.revenue = revenue;
    }

    public String getSwimmingpool() {
        return swimmingpool;
    }

    public void setSwimmingpool(String swimmingpool) {
        this.swimmingpool = swimmingpool;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
