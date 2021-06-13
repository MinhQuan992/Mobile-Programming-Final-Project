package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.AdministrativeUnitAdapter;
import hcmute.edu.vn.mssv18110344.bean.AdministrativeUnit;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class ChooseAdministrativeUnitActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView txtType;
    RecyclerView recyclerView;
    AdministrativeUnitAdapter adapter;
    List<AdministrativeUnit> units;

    private static final String TEXT_NOT_CHOOSE_PROVINCE = "Chọn tỉnh/thành";
    private static final String TEXT_NOT_CHOOSE_DISTRICT = "Chọn huyện/quận";
    private static final String TEXT_NOT_CHOOSE_WARD = "Chọn xã/phường";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_administrative_unit);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtType = findViewById(R.id.txtType);
        recyclerView = findViewById(R.id.recyclerView);

        String type = getIntent().getStringExtra("type");
        String id = getIntent().getStringExtra("id");

        DatabaseHandler db = new DatabaseHandler(this);

        switch (type) {
            case "province":
                units = db.getProvinces();
                txtType.setText(TEXT_NOT_CHOOSE_PROVINCE);
                break;
            case "district":
                units = db.getDistricts(id);
                txtType.setText(TEXT_NOT_CHOOSE_DISTRICT);
                break;
            default:
                units = db.getWards(id);
                txtType.setText(TEXT_NOT_CHOOSE_WARD);
        }

        adapter = new AdministrativeUnitAdapter(this, units, type, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}