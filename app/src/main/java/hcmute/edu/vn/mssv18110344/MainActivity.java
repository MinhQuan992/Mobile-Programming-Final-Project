package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    AppCompatButton btnSignIn;
    TextView forgotPassword;
    TextView register;
    ProgressBar progressBar;
    CheckBox chkRemember;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    boolean saveLogin;
    boolean doubleBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        forgotPassword = findViewById(R.id.forgotPassword);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        chkRemember = findViewById(R.id.chkRemember);

        progressBar.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.VISIBLE);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            txtEmail.setText(loginPreferences.getString("email", ""));
            txtPassword.setText(loginPreferences.getString("password", ""));
            chkRemember.setChecked(true);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập tất cả thông tin!", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.INVISIBLE);

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.createDataBase();
                db.openDataBase();
                User user = db.getUserByEmail(email);

                if (user != null && password.equals(user.getPassword())) {
                    if (chkRemember.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("email", email);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Email hoặc mật khẩu sai!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSignIn.setVisibility(View.VISIBLE);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPassStepOneActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterStepOneActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            finish();
            System.exit(0);
        }

        doubleBack = true;
        Toast.makeText(this, "Nhấn BACK lần nữa để thoát!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }
}