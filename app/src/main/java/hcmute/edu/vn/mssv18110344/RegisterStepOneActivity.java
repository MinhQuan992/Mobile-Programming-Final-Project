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
                String phoneNumber = txtPhone.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your phone number!", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    if (db.phoneExisted(phoneNumber)) {
                        Toast.makeText(getApplicationContext(), "This phone number already existed.", Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.INVISIBLE);

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
                }
            }
        });
    }
}