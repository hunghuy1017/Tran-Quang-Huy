package models;

public class OrderDetail {
    private int detailID;
    private double price;
    private int quantity;
    private boolean status;
    private int productID;
    private int orderID;
    private String productName;
    private String productImage;

    public OrderDetail(int detailID, double price, int quantity, boolean status, int productID, int orderID) {
        this.detailID = detailID;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.productID = productID;
        this.orderID = orderID;
    }

    // Getters and Setters
    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "detailID=" + detailID + ", price=" + price + ", quantity=" + quantity + ", status=" + status + ", productID=" + productID + ", orderID=" + orderID + ", productName=" + productName + ", productImage=" + productImage + '}';
    }
    
}