package hcmute.edu.vn.mssv18110344;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;

public class HomeActivity extends AppCompatActivity {

    EditText txtFind;
    ImageButton btnCart, btnRice, btnNoodle, btnWater, btnSalad, btnSandwich, btnFastFood;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        User user = (User) getIntent().getSerializableExtra("user");

        txtFind = findViewById(R.id.txtFind);
        btnCart = findViewById(R.id.btnCart);
        btnRice = findViewById(R.id.btnRice);
        btnNoodle = findViewById(R.id.btnNoodle);
        btnWater = findViewById(R.id.btnWater);
        btnSalad = findViewById(R.id.btnSalad);
        btnSandwich = findViewById(R.id.btnSandwich);
        btnFastFood = findViewById(R.id.btnFastFood);
        bottomNav = findViewById(R.id.bottom_nav);

        btnRice.setOnClickListener(this::onClick);
        btnNoodle.setOnClickListener(this::onClick);
        btnWater.setOnClickListener(this::onClick);
        btnSalad.setOnClickListener(this::onClick);
        btnSandwich.setOnClickListener(this::onClick);
        btnFastFood.setOnClickListener(this::onClick);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_orders:
                        break;
                    case R.id.nav_account:
                        Intent intent = new Intent(getApplicationContext(), ManageAccountActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SeeProductsActivity.class);
        switch (v.getId()) {
            case R.id.btnRice:
                intent.putExtra("category", 1);
                break;
            case R.id.btnNoodle:
                intent.putExtra("category", 2);
                break;
            case R.id.btnWater:
                intent.putExtra("category", 3);
                break;
            case R.id.btnSandwich:
                intent.putExtra("category", 4);
                break;
            case R.id.btnFastFood:
                intent.putExtra("category", 5);
                break;
            case R.id.btnSalad:
                intent.putExtra("category", 6);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}