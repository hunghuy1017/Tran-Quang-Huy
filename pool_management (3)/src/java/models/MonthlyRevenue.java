package models;
public class MonthlyRevenue {
    private int month;
    private double revenue;
    private int year;
    public MonthlyRevenue() {
    }
    public MonthlyRevenue(int month, double revenue, int year) {
        this.month = month;
        this.revenue = revenue;
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public double getRevenue() {
        return revenue;
    }
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
