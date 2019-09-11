package com.alternative.cap.restmindv3.activity.multi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.fragment.HomePage.Feature.MediaListFragment;
import com.alternative.cap.restmindv3.fragment.HomePage.FeatureFragment;
import com.alternative.cap.restmindv3.manager.adapter.FeatureFragmentAdapter;
import com.alternative.cap.restmindv3.util.MutableValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity implements
        FeatureFragmentAdapter.FeatureListener,
        MediaListFragment.MediaListListener {

    public static final String TAG_DODO = "dodo";

    //    private TextView userTextView;
    private Button testBtn;
    private MutableValue heightRecyclerViewValue;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Display display;
    private Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init(savedInstanceState);
        workbench(savedInstanceState);
        drawerWorkbench(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        heightRecyclerViewValue = new MutableValue();

        drawerLayout = findViewById(R.id.drawerLayout);


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                HomePageActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void workbench(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentContainerHomePageFeature, FeatureFragment.newInstance())
                .commit();

        display.getSize(size);
        heightRecyclerViewValue.setHeightRecyclerViewValue(size.y);
    }

    private void drawerWorkbench(Bundle savedInstanceState) {

        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, NavigationHomePageActivity.class));
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFeatureClick(int featurePosition) {
        getSupportActionBar().hide();
        if (featurePosition == 0) {
            findViewById(R.id.contentContainerHomePageFragment).setVisibility(View.VISIBLE);
            findViewById(R.id.homePageLinearLayout).setVisibility(View.GONE);
        }else if (featurePosition == 2) {
            findViewById(R.id.contentContainerHomePageFragment).setVisibility(View.VISIBLE);
            findViewById(R.id.homePageLinearLayout).setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainerHomePageFragment, MediaListFragment.newInstance())
                    .addToBackStack("mediaPlayer")
                    .commit();
        }
    }

//    @Override
//    public void onBreathDestroy() {
//        findViewById(R.id.homePageLinearLayout).setVisibility(View.VISIBLE);
//        findViewById(R.id.contentContainerHomePageFragment).setVisibility(View.GONE);
//        getSupportActionBar().show();
//    }

    @Override
    public void onMediaListDestroy() {
        findViewById(R.id.homePageLinearLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.contentContainerHomePageFragment).setVisibility(View.GONE);
        getSupportActionBar().show();
    }
}