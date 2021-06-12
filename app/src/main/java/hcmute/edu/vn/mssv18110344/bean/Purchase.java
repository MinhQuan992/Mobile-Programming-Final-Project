package hcmute.edu.vn.mssv18110344.bean;

public class Purchase {
    private int cartId;
    private int productId;
    private int amount;
    private int price;

    public Purchase(int cartId, int productId, int amount, int price) {
        this.cartId = cartId;
        this.productId = productId;
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
