package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import hcmute.edu.vn.mssv18110344.bean.User;

public class SeeInfoActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView txtFullName, txtSex, txtBirhDate, txtPhone, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_info);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtFullName = findViewById(R.id.txtFullName);
        txtSex = findViewById(R.id.txtSex);
        txtBirhDate = findViewById(R.id.txtBirthDate);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);

        User user = (User) getIntent().getSerializableExtra("user");

        txtFullName.setText(user.getFullName());
        txtSex.setText(user.getSex());
        txtBirhDate.setText(user.getDateOfBirth());
        txtPhone.setText(user.getPhone());
        txtEmail.setText(user.getEmail());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}