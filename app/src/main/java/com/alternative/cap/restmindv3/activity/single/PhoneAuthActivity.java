package com.alternative.cap.restmindv3.activity.single;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alternative.cap.restmindv3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private String phoneCode;
    private String phoneNumber;
    private String phoneNumberWithCode;
    private String verifyId;

    private TextView phoneCountyTextView;
    private TextView phoneNumberTextView;
    private TextView resentTextView;
    private EditText codeEntryEditText;
    private Button loginBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        init(savedInstanceState);
        workbench(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        phoneCode = getIntent().getExtras().getString("phoneCode");
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        phoneCountyTextView = findViewById(R.id.phoneCountyTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        resentTextView = findViewById(R.id.resentTextView);
        codeEntryEditText = findViewById(R.id.codeEntryEditText);
        loginBtn = findViewById(R.id.loginBtn);
    }

    private void workbench(Bundle savedInstanceState) {
        phoneNumberWithCode = "+" + phoneCode + phoneNumber;
        phoneCountyTextView.setText(phoneCode);
        phoneNumberTextView.setText(phoneNumber);

        sendVerificationCode(phoneNumberWithCode);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationCode = codeEntryEditText.getText().toString().trim();
                if (verificationCode.isEmpty() || verificationCode.length() < 6){
                    codeEntryEditText.setError("Entry code");
                    codeEntryEditText.requestFocus();
                    return;
                }
                verifyCode(verificationCode);
            }
        });

        resentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumberWithCode);
            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra("userPhoneNumber", phoneNumber);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(PhoneAuthActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number){
        Toast.makeText(PhoneAuthActivity.this, number, Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                15,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifyId = s;
            Toast.makeText(PhoneAuthActivity.this, "Sms sent", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String verificationCode = phoneAuthCredential.getSmsCode();
            if (verificationCode != null) {
                verifyCode(verificationCode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneAuthActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}