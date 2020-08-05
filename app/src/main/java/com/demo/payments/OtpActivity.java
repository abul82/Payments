package com.demo.payments;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OtpActivity extends AppCompatActivity implements PhoneCallbacksListener {
    public OtpViewModel otpViewModel;
    private String mAuthVerificationId, phn_number;
    private Button mVerifyBtn;
    private EditText mOtpText;
    private TextView mOtpFeedback;
    private ProgressBar mOtpProgress;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mVerifyBtn = findViewById(R.id.verify_btn);
        mOtpText = findViewById(R.id.otp_text_view);
        mOtpProgress = findViewById(R.id.otp_progress_bar);
        mOtpFeedback = findViewById(R.id.otp_form_feedback);
        database = FirebaseDatabase.getInstance();
        phn_number = getIntent().getExtras().getString("phn_num","defaultKey");
        otpViewModel = ViewModelProviders.of(OtpActivity.this).get(OtpViewModel.class);
        otpViewModel.setPhoneCallbacksListener(OtpActivity.this);
        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = mOtpText.getText().toString();
                mOtpFeedback.setVisibility(View.INVISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                updateUI(otp);
            }
        });
    }

    private void updateUI(String otp) {
        if (otp.isEmpty()) {
            mOtpFeedback.setVisibility(View.VISIBLE);
            mOtpFeedback.setText("Please fill in the form and try again.");

        } else {

            mOtpProgress.setVisibility(View.VISIBLE);
            mVerifyBtn.setEnabled(false);
            mVerifyBtn.setClickable(false);

            //Pasing the OTP and credentials for the Verification
            try{
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
//                signInWithPhoneAuthCredential(credential);
                otpViewModel.signInWithPhoneAuthCredential(credential,this);
            }catch (Exception e){
                Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onVerificationCompleted() {

    }

    @Override
    public void onVerificationCodeDetected(String code) {

    }

    @Override
    public void onVerificationFailed(String message) {
        Toast.makeText(this,"Number Verification Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken token) {
mAuthVerificationId = s;
        mOtpText.setEnabled(true);
        Toast.makeText(this,"OTP Sent",Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void signinCompleted(Task<AuthResult> task) {
if (task.isSuccessful()){
    String uid = task.getResult().getUser().getUid();
    database.getReference("/Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Intent intent = new Intent(OtpActivity.this,MainActivity.class);
                startActivity(intent);
                finishAfterTransition();
            }
            else {
                Intent intent = new Intent(OtpActivity.this,GatherDetails.class);
                startActivity(intent);
                finishAfterTransition();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}
    }

    @Override
    public void signinFailed(boolean b) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mOtpText.setEnabled(false);
        otpViewModel.sendVerificationCode("+91"+phn_number,OtpActivity.this);
    }
}