package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;
import hcmute.edu.vn.mssv18110344.utility.ImagePickerUtility;
import hcmute.edu.vn.mssv18110344.utility.JavaMailAPI;

public class RegisterStepFourActivity extends AppCompatActivity {

    ImageButton btnBack, btnResetAvatar;
    ImageView imgAvatar;
    AppCompatButton btnFinish;
    ProgressBar progressBar;
    Bitmap avt;
    String fullName, dateOfBirth, email, password, sex, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_four);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        btnResetAvatar = findViewById(R.id.btnResetAvatar);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressBar);

        avt = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);

        fullName = getIntent().getStringExtra("fullName");
        dateOfBirth = getIntent().getStringExtra("dateOfBirth");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        sex = getIntent().getStringExtra("sex");
        phone = getIntent().getStringExtra("phone");

        btnResetAvatar.setVisibility(View.INVISIBLE);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                backIntent.putExtra("phoneNumber", phone);
                setResult(RESULT_OK, backIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnResetAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAvatar.setImageResource(R.drawable.ic_baseline_account_box_24);
                avt = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                btnResetAvatar.setVisibility(View.INVISIBLE);
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePickerUtility.getPickImageIntent(getApplicationContext());
                startActivityForResult(chooseImageIntent, 0);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(getApplicationContext(), RegisterStepFiveActivity.class);
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                int id = db.generateId();
                byte[] avatar = DbBitmapUtility.getBytes(avt);
                User user = new User(id, fullName, sex, dateOfBirth, phone, email, password, avatar);
                db.addUser(user);

                String subject = "Welcome to Ministop!";
                String msg = "Your account has been created successfully. Now you can sign in to your account and enjoy great moments with us!\nMinistop Team";
                JavaMailAPI javaMailAPI = new JavaMailAPI(getApplicationContext(), email, subject, msg);
                javaMailAPI.execute();

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent();
        backIntent.putExtra("phoneNumber", phone);
        setResult(RESULT_OK, backIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                avt = ImagePickerUtility.getImageFromResult(this, resultCode, data);
                imgAvatar.setImageBitmap(avt);
                btnResetAvatar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Load image successfully!", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}