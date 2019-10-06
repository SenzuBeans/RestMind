package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.fragment.RegisterFragment;
import com.alternative.cap.restmindv3.util.SettingListener;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class ProfileFragment extends Fragment {


    static SettingListener listener;

    private BarChart breathChart;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;
    private UserDetails userDetails;

    Random random = new Random();
    int x = random.nextInt(1000);

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(SettingListener passingListener) {

        Bundle args = new Bundle();
        listener = passingListener;
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initInsance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);

        return rootView;
    }


    private void initInsance(View rootView, Bundle savedInstanceState) {
        reference.child(user.getUid()).child("temp_steam").setValue(x);
        if (user.getDisplayName().equals("VISITOR")) {
            rootView.findViewById(R.id.profileContentContainer).setVisibility(View.VISIBLE);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.profileContentContainer, RegisterFragment.newInstance(new RegisterFragment.RegisterListener() {
                        @Override
                        public void onRegis() {
                            rootView.findViewById(R.id.profileContentContainer).setVisibility(View.GONE);
                        }
                    }))
                    .commit();
        }

        breathChart = rootView.findViewById(R.id.breathChart);


    }

    private void workbench(View rootView, Bundle savedInstanceState) {

        rootView.findViewById(R.id.settingProfileBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        breathChart.setBackgroundColor(Color.GREEN);
        breathChart.setGridBackgroundColor(Color.DKGRAY);
        breathChart.setDrawGridBackground(true);

        breathChart.setDrawBorders(true);
        breathChart.getDescription().setEnabled(false);
        breathChart.setPinchZoom(false);
        breathChart.getLegend().setEnabled(true);

        setData();
    }

    private void setData() {

        ArrayList<BarEntry> yVels = new ArrayList<>();

        for (int i = 0 ; i < 6 ; i++){
            yVels.add(new BarEntry(i, (float) (Math.random()*10)+10));
        }

        BarDataSet barDataSet = new BarDataSet(yVels, "data 1");

        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColor(Color.YELLOW);


        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(true);

        breathChart.setScaleEnabled(false);
        breathChart.setData(barData);

    }

    @Override
    public void onDestroy() {
        listener.onClickedDestroy();
        super.onDestroy();
    }
}
