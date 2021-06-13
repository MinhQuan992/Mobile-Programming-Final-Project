package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class Cart implements Serializable {
    private int cartId;
    private int userId;

    public Cart(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
