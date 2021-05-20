package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String name;
    int price;
    int amount;
    int picture;
    int category;

    public Product(int id, String name, int price, int amount, int picture, int category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.picture = picture;
        this.category = category;
    }

    public Product(String name, int price, int amount, int picture, int category) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.picture = picture;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
