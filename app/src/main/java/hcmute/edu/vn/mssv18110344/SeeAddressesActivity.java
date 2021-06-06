package hcmute.edu.vn.mssv18110344;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.AddressAdapter;
import hcmute.edu.vn.mssv18110344.bean.Address;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class SeeAddressesActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
    ExtendedFloatingActionButton btnAddAddress;
    AddressAdapter adapter;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_addresses);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        btnAddAddress = findViewById(R.id.btnAddAddress);

        User user = (User) getIntent().getSerializableExtra("user");

        DatabaseHandler db = new DatabaseHandler(this);
        db.openDataBase();
        addresses = db.getAddresses(user.getId());

        adapter = new AddressAdapter(this, this, addresses, user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (addresses == null) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddAndEditAddressActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle args = data.getBundleExtra("bundle");
                List<Address> addressList = (List<Address>) args.getSerializable("addressList");

                if (addresses == null) {
                    addresses = new ArrayList<>();
                    imageView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                addresses.clear();
                addresses.addAll(addressList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}