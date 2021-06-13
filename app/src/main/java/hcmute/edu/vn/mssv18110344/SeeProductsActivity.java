package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.ProductAdapter;
import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class SeeProductsActivity extends AppCompatActivity {

    ImageButton btnBack, btnCart;
    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
    List<Product> products;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_products);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        int category = getIntent().getIntExtra("category", 1);
        User user = (User) getIntent().getSerializableExtra("user");
        String type = getIntent().getStringExtra("type");

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cart cart = db.getCart(user);

        if (type.equals("from category")) {
            products = db.getProducts(category);
        } else {
            String textFind = getIntent().getStringExtra("text");
            products = db.getProductsByText(textFind);
        }

        if (products.size() != 0) {
            adapter = new ProductAdapter(this, products, user);
            recyclerView.setAdapter(adapter);
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);

            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

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
    }
}