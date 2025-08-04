/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author LENOVO
 */
public class PackagesEvent {

    private int PackageID;
    private String EventTitle;
    private String PackageName;
    private String PackageDescription;
    private double Price;
    private boolean AvailabilityStatus;

    public PackagesEvent() {
    }

    public PackagesEvent(int PackageID, String EventTitle, String PackageName, String PackageDescription, double Price, boolean AvailabilityStatus) {
        this.PackageID = PackageID;
        this.EventTitle = EventTitle;
        this.PackageName = PackageName;
        this.PackageDescription = PackageDescription;
        this.Price = Price;
        this.AvailabilityStatus = AvailabilityStatus;
    }

    public int getPackageID() {
        return PackageID;
    }

    public void setPackageID(int PackageID) {
        this.PackageID = PackageID;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String EventTitle) {
        this.EventTitle = EventTitle;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String PackageName) {
        this.PackageName = PackageName;
    }

    public String getPackageDescription() {
        return PackageDescription;
    }

    public void setPackageDescription(String PackageDescription) {
        this.PackageDescription = PackageDescription;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public boolean isAvailabilityStatus() {
        return AvailabilityStatus;
    }

    public void setAvailabilityStatus(boolean AvailabilityStatus) {
        this.AvailabilityStatus = AvailabilityStatus;
    }

    @Override
    public String toString() {
        return "PackagesEvent{" + "PackageID=" + PackageID + ", EventTitle=" + EventTitle + ", PackageName=" + PackageName + ", PackageDescription=" + PackageDescription + ", Price=" + Price + ", AvailabilityStatus=" + AvailabilityStatus + '}';
    }

}
