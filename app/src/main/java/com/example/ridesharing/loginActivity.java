package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mphone;
    private boolean loginBtnClicked;
    public static boolean bodi;
    private FirebaseAuth mAuth;
    private DatabaseReference usersLocationDb;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private Button verifyButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Hawk.init(this).build();
        loginBtnClicked = false;
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());
        mAuth = FirebaseAuth.getInstance();
        mLogin = findViewById(R.id.register);
        mphone = findViewById(R.id.otp);
        verifyButton = findViewById(R.id.verify_button);
        verifyButton.setEnabled(false);
        verifyButton.setAlpha(0.5f);
        TextView termsText = findViewById(R.id.terms_text);
        
        // Make Terms of Use and Privacy Policy clickable
        SpannableString spannableString = new SpannableString(termsText.getText());
        
        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle Terms of Use click
                // You can open terms of use webpage or activity
                Toast.makeText(loginActivity.this, "Terms of Use clicked", Toast.LENGTH_SHORT).show();
            }
        };
        
        ClickableSpan privacyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle Privacy Policy click
                // You can open privacy policy webpage or activity
                Toast.makeText(loginActivity.this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show();
            }
        };
        
        // Find the position of clickable text
        String fullText = termsText.getText().toString();
        int termsStart = fullText.indexOf("Terms of Use");
        int termsEnd = termsStart + "Terms of Use".length();
        int privacyStart = fullText.indexOf("Privacy Policy");
        int privacyEnd = privacyStart + "Privacy Policy".length();
        
        // Set clickable spans
        spannableString.setSpan(termsClickableSpan, termsStart, termsEnd, 
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(privacyClickableSpan, privacyStart, privacyEnd, 
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        // Make the spans appear as links
        termsText.setText(spannableString);
        termsText.setMovementMethod(LinkMovementMethod.getInstance());
        
        // Add phone number validation
        mphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isValidLength = s.length() == 10;
                verifyButton.setEnabled(isValidLength);
                verifyButton.setAlpha(isValidLength ? 1.0f : 0.5f);
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodi = true;
                loginBtnClicked = true;
                final String phone = mphone.getText().toString();
                if (isStringNull(phone)) {
                    Toast.makeText(loginActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    verifyButton.setEnabled(false);
                    verifyButton.setAlpha(0.5f);
                    startPhoneNumberVerification("+91"+phone);
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE);
        Boolean registered = sharedPreferences.getBoolean("registered", false);
        Boolean signed = sharedPreferences.getBoolean("signed", false);
        if(registered && signed){
            Intent intent = new Intent(loginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(signed && !registered){
            Intent intent = new Intent(loginActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        verifyButton.setEnabled(true);
                        verifyButton.setAlpha(1.0f);
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.i("error",e.getMessage());
                        verifyButton.setEnabled(true);
                        verifyButton.setAlpha(1.0f);
                        Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        verifyButton.setEnabled(true);
                        verifyButton.setAlpha(1.0f);
                        // Save the verification ID and resending token
                        loginActivity.this.verificationId = verificationId;
                        loginActivity.this.resendingToken = token;

                        // Navigate to OTP verification screen
                        Intent intent = new Intent(loginActivity.this, OTPVerificationActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phone",phoneNumber);
                        startActivity(intent);
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    fetchUserLocationsAndStartMainActivity();
                } else {
                    Toast.makeText(loginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchUserLocationsAndStartMainActivity() {
        usersLocationDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, UserLocation> userLocations = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserLocation userLocation = snapshot.getValue(UserLocation.class);
                    if (userLocation != null) {
                        userLocations.put(snapshot.getKey(), userLocation);
                    }
                }
                Hawk.put("userLocations", userLocations);
                startMainActivity();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(loginActivity.this, "Failed to fetch user locations, opening MainActivity.", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(loginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isStringNull(String email) {
        return email.equals("");
    }


}





