package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class ResetPassStepThreeActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText txtPassword, txtConfirmPassword;
    AppCompatButton btnFinish;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_step_three);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.INVISIBLE);

                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();

                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.VISIBLE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "These passwords do not match.", Toast.LENGTH_LONG).show();
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                    txtConfirmPassword.setText("");
                    progressBar.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.VISIBLE);
                    return;
                }

                if (!isPasswordValid(password)) {
                    Toast.makeText(getApplicationContext(), "Your password must contain at least " +
                            "1 lowercase character, " +
                            "1 uppercase character, " +
                            "1 digit, " +
                            "and be between 8 and 20 characters in length.", Toast.LENGTH_LONG).show();
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                    txtConfirmPassword.setText("");
                    progressBar.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.VISIBLE);
                    return;
                }

                User user = (User) getIntent().getSerializableExtra("user");
                user.setPassword(password);

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.updateUser(user);

                Intent intent = new Intent(getApplicationContext(), ResetPassStepFourActivity.class);
                startActivity(intent);
                finish();
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

    private boolean isPasswordValid(String password) {
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,50}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}