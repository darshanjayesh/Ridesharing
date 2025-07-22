package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;




public class OTPVerificationActivity extends AppCompatActivity {

    private EditText[] otpDigits;
    private Button verifyButton;
    private String verificationId;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        // Initialize OTP digit EditTexts
        otpDigits = new EditText[6];
        otpDigits[0] = findViewById(R.id.otp_digit1);
        otpDigits[1] = findViewById(R.id.otp_digit2);
        otpDigits[2] = findViewById(R.id.otp_digit3);
        otpDigits[3] = findViewById(R.id.otp_digit4);
        otpDigits[4] = findViewById(R.id.otp_digit5);
        otpDigits[5] = findViewById(R.id.otp_digit6);

        verifyButton = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");

        // Set up TextWatcher for each OTP digit
        for (int i = 0; i < otpDigits.length; i++) {
            final int currentIndex = i;
            otpDigits[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && currentIndex < otpDigits.length - 1) {
                        otpDigits[currentIndex + 1].requestFocus();
                    } else if (s.length() == 0 && currentIndex > 0) {
                        otpDigits[currentIndex - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkOTPComplete();
                }
            });
        }

        // Show keyboard automatically
        otpDigits[0].requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(otpDigits[0], InputMethodManager.SHOW_IMPLICIT);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = getEnteredOTP();
                if (!code.isEmpty() && code.length() == 6) {
                    verifyCode(code);
                } else {
                    Toast.makeText(OTPVerificationActivity.this, "Enter the complete OTP code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getEnteredOTP() {
        StringBuilder otp = new StringBuilder();
        for (EditText digit : otpDigits) {
            otp.append(digit.getText().toString());
        }
        return otp.toString();
    }

    private void checkOTPComplete() {
        String otp = getEnteredOTP();
        verifyButton.setEnabled(otp.length() == 6);
        verifyButton.setAlpha(otp.length() == 6 ? 1.0f : 0.5f);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("signed", true);
                    editor.apply();
                    // User successfully verified, redirect to the main activity
                    Intent intent = new Intent(OTPVerificationActivity.this, RegisterActivity.class);
                    intent.putExtra("phone", getIntent().getStringExtra("phone"));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(OTPVerificationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
