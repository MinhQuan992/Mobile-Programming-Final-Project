package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.EditText;

import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.ProductAdapter;
import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class SeeProductsActivity extends AppCompatActivity {

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
    }
}