/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class Users {
    private int  UserID,RoleID;
    private String  fullName, password;
    
    private String Address, Phone, Email, Image;
    private Roles role;
    private Timestamp createdAt;


    public Users() {
    }

    public Users(int UserID, String fullName, String password, String Address, String Phone, String Email) {
        this.UserID = UserID;
        this.fullName = fullName;
        this.password = password;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
    }

    
    public Users(int UserID, String fullName, String password, String Address, String Phone, String Email, String Image) {
        this.UserID = UserID;
        this.fullName = fullName;
        this.password = password;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
        this.Image = Image;
    }

   
    public Users(int UserID, int RoleID, String fullName, String Email, String password, String Phone, String Address, String Image) {
        this.UserID = UserID;
        this.RoleID = RoleID;
        this.fullName = fullName;
        this.Email = Email;
        this.password = password;
        this.Phone = Phone;
        this.Address = Address;
        this.Image = Image;
    }
     public Users(int UserID, int RoleID, String fullName, String password, String Address, String Phone, String Email, String Image, Roles role, Timestamp createdAt) {
        this.UserID = UserID;
        this.RoleID = RoleID;
        this.fullName = fullName;
        this.password = password;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
        this.Image = Image;
        this.role = role;
        this.createdAt = createdAt;
    }
     public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) { // Đảm bảo tham số là Timestamp
        this.createdAt = createdAt;
    }

    public Users(int UserID, String password) {
        this.UserID = UserID;
        this.password = password;
    }
    
    

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        this.RoleID = roleID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        this.UserID = userID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    @Override
    public String toString() {
        return "Users{" + "UserID=" + UserID + ", RoleID=" + RoleID + ", fullName=" + fullName + ", password=" + password + ", Address=" + Address + ", Phone=" + Phone + ", Email=" + Email + ", Image=" + Image + '}';
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    
    
    
    
    
    

    
}