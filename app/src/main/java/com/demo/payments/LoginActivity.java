package com.demo.payments;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private FirebaseUser firebaseUser;
EditText emailET,pwdET;
Button loginBtn;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.email_et);
        pwdET = findViewById(R.id.pwd_et);
        loginBtn = findViewById(R.id.login_btn);
        database = FirebaseDatabase.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String phn_num = emailET.getText().toString().trim();
                finishAfterTransition();
                Intent intent = new Intent(LoginActivity.this,OtpActivity.class);
                intent.putExtra("phn_num", phn_num);
                startActivity(intent);

            }
        });
    }
}