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
public class Product {
    private int productID, poolID;
    private String productName, description;
    private Double price;
    private Timestamp addedDate;
    private int quantity;
    private String image;
    private Boolean status;
    private int categoryID;
    Boolean IsRentable;
    Double RentalPrice;
    
    public Product() {
    }

    public Product(int productID, int poolID, String productName, String description, Double price, Timestamp addedDate, int quantity, String image, Boolean status, int categoryID, Boolean IsRentable, Double RentalPrice) {
        this.productID = productID;
        this.poolID = poolID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.addedDate = addedDate;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
        this.categoryID = categoryID;
        this.IsRentable = IsRentable;
        this.RentalPrice = RentalPrice;
    }  

    public Product(int poolID, String productName, String description, Double price, Timestamp addedDate, int quantity, String image, Boolean status, int categoryID, Boolean IsRentable, Double RentalPrice) {
        this.poolID = poolID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.addedDate = addedDate;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
        this.categoryID = categoryID;
        this.IsRentable = IsRentable;
        this.RentalPrice = RentalPrice;
    }
    
    
    

    public Product(int productID, int poolID, String productName, String description, Double price, Timestamp addedDate, int quantity, String image, Boolean status, int categoryID) {
        this.productID = productID;
        this.poolID = poolID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.addedDate = addedDate;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
        this.categoryID = categoryID;
    }
    
    public Product(int productID, int poolID, String productName, String description, Double price, Timestamp addedDate, int quantity, String image, Boolean status) {
        this.productID = productID;
        this.poolID = poolID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.addedDate = addedDate;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
    }

    public Product(int poolID, String productName, String description, Double price, Timestamp addedDate, int quantity, String image, Boolean status) {
        this.poolID = poolID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.addedDate = addedDate;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
    }

    public Product(int productID, int poolID, String productName, Double price, int quantity, String image, Boolean status) {
        this.productID = productID;
        this.poolID = poolID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getPoolID() {
        return poolID;
    }

    public void setPoolID(int poolID) {
        this.poolID = poolID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public Boolean getIsRentable() {
        return IsRentable;
    }

    public void setIsRentable(Boolean IsRentable) {
        this.IsRentable = IsRentable;
    }

    public Double getRentalPrice() {
        return RentalPrice;
    }

    public void setRentalPrice(Double RentalPrice) {
        this.RentalPrice = RentalPrice;
    }
   
    
    
}
