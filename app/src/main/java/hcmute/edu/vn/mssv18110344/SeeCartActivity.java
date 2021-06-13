package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.PurchaseAdapter;
import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.bean.ViewPurchase;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class SeeCartActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView, txtCost;
    AppCompatButton btnPurchase;
    ConstraintLayout layout;
    List<ViewPurchase> viewPurchases;
    PurchaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_cart);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        txtCost = findViewById(R.id.txtCost);
        btnPurchase = findViewById(R.id.btnPurchase);
        layout = findViewById(R.id.layout);

        Cart cart = (Cart) getIntent().getSerializableExtra("cart");
        DatabaseHandler db = new DatabaseHandler(this);

        if (db.isCartEmpty(cart)) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            viewPurchases = db.getProductsInCart(cart);
            adapter = new PurchaseAdapter(this, cart, viewPurchases, db, imageView, textView, txtCost, layout);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeeAddressesActivity.class);
                intent.putExtra("cart", cart);
                startActivity(intent);
            }
        });
    }
}