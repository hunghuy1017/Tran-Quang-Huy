package models;

import java.sql.Time;

public class SwimmingPool {

    private int poolID;
    private String name;
    private String location;
    private String phone;
    private String fanpage;
    private Time openTime;  
    private Time closeTime; 
    private String description;
    private boolean status;
    private String image;

    public SwimmingPool() {
    }

    public SwimmingPool(int poolID, String name, String location, String phone, String fanpage, Time openTime, Time closeTime, String description, boolean status, String image) {
        this.poolID = poolID;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.fanpage = fanpage;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.description = description;
        this.status = status;
        this.image = image;
    }

    // Getter & Setter
    public int getPoolID() {
        return poolID;
    }

    public void setPoolID(int poolID) {
        this.poolID = poolID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFanpage() {
        return fanpage;
    }

    public void setFanpage(String fanpage) {
        this.fanpage = fanpage;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SwimmingPool{" + "poolID=" + poolID + ", name=" + name + ", location=" + location + ", phone=" + phone + ", fanpage=" + fanpage + ", openTime=" + openTime + ", closeTime=" + closeTime + ", description=" + description + ", status=" + status + ", image=" + image + '}';
    }
}