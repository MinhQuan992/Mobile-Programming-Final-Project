package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;

public class HomeActivity extends AppCompatActivity {

    ImageView imgAvatar;
    TextView txtId, txtFullName, txtSex, txtDateOfBirth, txtPhone, txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        imgAvatar = findViewById(R.id.imgAvatar);
        txtId = findViewById(R.id.txtId);
        txtFullName = findViewById(R.id.txtFullName);
        txtSex = findViewById(R.id.txtSex);
        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        User user = (User) getIntent().getSerializableExtra("user");
        byte[] avt = user.getAvatar();
        int id = user.getId();
        String fullName = user.getFullName();
        String sex = user.getSex();
        String dateOfBirth = user.getDateOfBirth();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();

        imgAvatar.setImageBitmap(DbBitmapUtility.getImage(avt));
        txtId.setText(String.valueOf(id));
        txtFullName.setText(fullName);
        txtSex.setText(sex);
        txtDateOfBirth.setText(dateOfBirth);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtPassword.setText(password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}