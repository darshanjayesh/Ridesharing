package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mName, mSex;
    private FirebaseAuth mAuth;
    String mPhone;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        /*firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                spinner.setVisibility(View.VISIBLE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    spinner.setVisibility(View.GONE);
                    return;
                }
                spinner.setVisibility(View.GONE);
            }
        };*/
        /*existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });*/
        mRegister = findViewById(R.id.register);
        mName = findViewById(R.id.name);

        mPhone = getIntent().getStringExtra("phone");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // need.setAdapter(adapter);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserToDatabase();
            }
        });
    }


    private void saveUserToDatabase() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", mName.getText().toString());
        userInfo.put("profileImageUrl", "default");
      //  userInfo.put("sex", mSex.getText().toString());
        //userInfo.put("need", need.getSelectedItem().toString());
        currentUserDb.updateChildren(userInfo);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("registered",true);
        editor.apply();
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

}

