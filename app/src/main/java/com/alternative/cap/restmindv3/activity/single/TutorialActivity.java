package com.alternative.cap.restmindv3.activity.single;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.MemberActivity;
import com.alternative.cap.restmindv3.manager.adapter.TutorialAdapter;
import com.alternative.cap.restmindv3.view.tutorial.Page4Tutorial;
import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;

public class TutorialActivity extends AppCompatActivity implements Page4Tutorial.Page4BtnClickListener {

    private ViewPager tutorialViewPager;
    private TutorialAdapter tutorialAdapter;
    private ViewPagerIndicator tutorialIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init(savedInstanceState);
        workbench(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        tutorialViewPager = findViewById(R.id.tutorialRecyclerView);
        tutorialAdapter = new TutorialAdapter(TutorialActivity.this);
        tutorialIndicator = findViewById(R.id.tutorialIndicator);
    }

    private void workbench(Bundle savedInstanceState) {
        tutorialViewPager.setAdapter(tutorialAdapter);
        tutorialIndicator.setViewPager(tutorialViewPager);
        tutorialIndicator.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onLoginBtnClicked() {
        startActivity(new Intent(TutorialActivity.this, MemberActivity.class));
        finish();
    }
}
