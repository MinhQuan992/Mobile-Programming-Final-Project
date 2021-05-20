package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;

public class ManageAccountActivity extends AppCompatActivity {

    ImageView imgAvatar;
    TextView txtFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        getSupportActionBar().hide();

        imgAvatar = findViewById(R.id.imgAvatar);
        txtFullName = findViewById(R.id.txtFullName);

        User user = (User) getIntent().getSerializableExtra("user");

        imgAvatar.setImageBitmap(DbBitmapUtility.getImage(user.getAvatar()));
        txtFullName.setText(user.getFullName());
    }
}