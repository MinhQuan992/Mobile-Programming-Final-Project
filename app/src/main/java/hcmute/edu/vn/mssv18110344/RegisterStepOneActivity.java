package hcmute.edu.vn.mssv18110344;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class RegisterStepOneActivity extends AppCompatActivity {

    ImageButton btnClose;
    EditText txtPhone;
    AppCompatButton btnNext;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);
        getSupportActionBar().hide();

        btnClose = findViewById(R.id.btnClose);
        txtPhone = findViewById(R.id.txtPhone);
        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.INVISIBLE);

                String phoneNumber = txtPhone.getText().toString().trim();

                if (!isPhoneValid(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập số điện thoại hợp lệ!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    return;
                }

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.createDataBase();
                db.openDataBase();

                if (db.phoneExisted(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Số điện thoại này đã tồn tại trong hệ thống.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    return;
                }

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + phoneNumber.substring(1),
                        60,
                        TimeUnit.SECONDS,
                        RegisterStepOneActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                btnNext.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                btnNext.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                btnNext.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(), RegisterStepTwoActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("verificationCode", verificationCode);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                );
            }
        });
    }

    private boolean isPhoneValid(String phone) {
        String expression = "^\\d{10}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}