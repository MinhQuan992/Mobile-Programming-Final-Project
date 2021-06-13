package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.bean.Purchase;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;

public class SeeProductDetailsActivity extends AppCompatActivity {

    ImageView img;
    TextView txtName, txtPrice;
    ImageButton btnBack, btnCart;
    AppCompatButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_product_details);
        getSupportActionBar().hide();

        img = findViewById(R.id.img);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);
        btnAdd = findViewById(R.id.btnAdd);

        Product product = (Product) getIntent().getSerializableExtra("product");
        User user = (User) getIntent().getSerializableExtra("user");

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cart cart = db.getCart(user);

        Bitmap picture = BitmapFactory.decodeResource(getResources(), product.getPicture());

        img.setImageBitmap(picture);
        txtName.setText(product.getName());
        txtPrice.setText(standardizeProductPrice(product.getPrice()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeeCartActivity.class);
                intent.putExtra("cart", cart);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Purchase purchase = new Purchase(cart.getCartId(), product.getId(), 1, product.getPrice());
                db.addPurchase(purchase);
                Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private String standardizeProductPrice(int price) {
        String priceInString = String.valueOf(price);
        String result = "";
        int i = priceInString.length() - 1, count = 0;
        while (i >= 0) {
            result += priceInString.substring(i, i + 1);
            count++;
            if (count == 3 && i != 0) {
                result += ".";
                count = 0;
            }
            i--;
        }
        return new StringBuilder(result).reverse().toString() + "đ";
    }
}