package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class Order implements Serializable {
    private int orderId;
    private int cartId;
    private String orderStatus;
    private String paymentStatus;
    private int totalCost;
    private String orderDate;
    private String payDate;
    private int addressId;

    public Order(int orderId, int cartId, String orderStatus, String paymentStatus, int totalCost, String orderDate, String payDate, int addressId) {
        this.orderId = orderId;
        this.cartId = cartId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.totalCost = totalCost;
        this.orderDate = orderDate;
        this.payDate = payDate;
        this.addressId = addressId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
