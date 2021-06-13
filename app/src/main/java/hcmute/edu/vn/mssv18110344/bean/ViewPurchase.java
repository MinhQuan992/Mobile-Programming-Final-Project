package hcmute.edu.vn.mssv18110344.bean;

public class ViewPurchase {
    private int cartId;
    private int productId;
    private int picture;
    private String name;
    private int amount;
    private int price;

    public ViewPurchase(int cartId, int productId, int picture, String name, int amount, int price) {
        this.cartId = cartId;
        this.productId = productId;
        this.picture = picture;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
