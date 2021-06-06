package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.ProductAdapter;
import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class SeeProductsActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView recyclerView;
    List<Product> products;
    ProductAdapter adapter;
    EditText txtFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_products);
        getSupportActionBar().hide();

        txtFind = findViewById(R.id.txtFind);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);

        int category = getIntent().getIntExtra("category", 1);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.createDataBase();
        db.openDataBase();

        products = db.getProducts(category);
        adapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}