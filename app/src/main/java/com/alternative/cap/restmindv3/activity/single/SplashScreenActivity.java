package com.alternative.cap.restmindv3.activity.single;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.HomePageActivity;
import com.alternative.cap.restmindv3.activity.multi.MemberActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private final long SPLASH_SCREEN_DELAYER = 3000;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user == null) {
                    startActivity(new Intent(SplashScreenActivity.this, TutorialActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreenActivity.this, HomePageActivity.class));
                    finish();
                }
            }
        }, SPLASH_SCREEN_DELAYER);
    }
}
