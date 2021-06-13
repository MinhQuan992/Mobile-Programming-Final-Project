package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class RegisterStepThreeActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText txtFullName, txtDateOfBirth, txtEmail, txtPassword, txtConfirmPassword;
    RadioGroup radioGroup;
    AppCompatButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_three);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtFullName = findViewById(R.id.txtFullName);
        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        radioGroup = findViewById(R.id.radioGroup);
        btnNext = findViewById(R.id.btnNext);

        String phone = getIntent().getStringExtra("phoneNumber");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = txtFullName.getText().toString().trim();
                String dateOfBirth = txtDateOfBirth.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();
                String sex;

                if (fullName.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập tất cả thông tin!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isEmailValid(email)) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập địa chỉ email hợp lệ!", Toast.LENGTH_LONG).show();
                    txtEmail.requestFocus();
                    return;
                }

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                if (db.emailExisted(email)) {
                    Toast.makeText(getApplicationContext(), "Địa chỉ email này đã tồn tại trong hệ thống.", Toast.LENGTH_LONG).show();
                    txtEmail.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp.", Toast.LENGTH_LONG).show();
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                    txtConfirmPassword.setText("");
                    return;
                }

                if (!isPasswordValid(password)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu của bạn phải chứa ít nhất " +
                            "1 ký tự thường, " +
                            "1 ký tự hoa, " +
                            "1 chữ số, " +
                            "và có độ dài từ 8 đến 20 ký tự!", Toast.LENGTH_LONG).show();
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                    txtConfirmPassword.setText("");
                    return;
                }

                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                sex = radioButton.getText().toString();

                Intent intent = new Intent(getApplicationContext(), RegisterStepFourActivity.class);

                intent.putExtra("fullName", fullName);
                intent.putExtra("dateOfBirth", dateOfBirth);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("sex", sex);
                intent.putExtra("phone", phone);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}