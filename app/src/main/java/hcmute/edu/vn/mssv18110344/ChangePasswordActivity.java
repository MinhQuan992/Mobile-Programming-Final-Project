package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText txtPassword, txtNewPassword, txtConfirmPassword;
    AppCompatButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().hide();

        txtPassword = findViewById(R.id.txtPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = txtPassword.getText().toString().trim();
                String newPassword = txtNewPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();

                if (password.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập tất cả thông tin!", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = (User) getIntent().getSerializableExtra("user");

                if (!password.equals(user.getPassword())) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu hiện tại không đúng.", Toast.LENGTH_LONG).show();
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                    txtNewPassword.setText("");
                    txtConfirmPassword.setText("");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp.", Toast.LENGTH_LONG).show();
                    txtNewPassword.setText("");
                    txtNewPassword.requestFocus();
                    txtConfirmPassword.setText("");
                    return;
                }

                if (!isPasswordValid(newPassword)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu của bạn phải chứa ít nhất " +
                            "1 ký tự thường, " +
                            "1 ký tự hoa, " +
                            "1 chữ số, " +
                            "và có độ dài từ 8 đến 20 ký tự!", Toast.LENGTH_LONG).show();
                    txtNewPassword.setText("");
                    txtNewPassword.requestFocus();
                    txtConfirmPassword.setText("");
                    return;
                }

                user.setPassword(newPassword);

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.updateUser(user);
                txtPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}