package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.fragment.RegisterFragment;
import com.alternative.cap.restmindv3.util.BreathLogItem;
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
import java.util.List;
import java.util.Random;


public class ProfileFragment extends Fragment {


    static SettingListener listener;

    private TextView profileUserName;
    private TextView profileUserEmail;
    private BarChart breathChart;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;

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
        checkRegis(rootView, savedInstanceState);
        return rootView;
    }


    private void initInsance(View rootView, Bundle savedInstanceState) {
        reference.child(user.getUid()).child("temp_steam").setValue(x);

        breathChart = rootView.findViewById(R.id.breathChart);
        initChart();
        profileUserName = rootView.findViewById(R.id.profileUserName);
        profileUserEmail = rootView.findViewById(R.id.profileUserEmail);

    }

    private void checkRegis(View rootView, Bundle savedInstanceState) {
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

        if (rootView.findViewById(R.id.profileContentContainer).getVisibility() == View.GONE){
            workbench(rootView, savedInstanceState);
        }
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        backBtn(rootView);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.child(user.getUid()).getValue(UserDetails.class);

                profileUserName.setText(userDetails.name);
                profileUserEmail.setText(userDetails.email);

                ArrayList<BreathLogItem> log = userDetails.breath_log;
                setData(log);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void backBtn(View rootView) {
        rootView.findViewById(R.id.settingProfileBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void initChart() {
        breathChart.setBackgroundColor(Color.GREEN);
        breathChart.setGridBackgroundColor(Color.DKGRAY);
        breathChart.setDrawGridBackground(true);

        breathChart.setDrawBorders(true);
        breathChart.getDescription().setEnabled(false);
        breathChart.setPinchZoom(false);
        breathChart.getLegend().setEnabled(true);
    }

    private void setData(ArrayList<BreathLogItem> log) {

        ArrayList<BarEntry> yVels = new ArrayList<>();

        for (int i = 0 ; i < log.size() ; i++){
            yVels.add(new BarEntry(i, (float) (Integer.parseInt(log.get(i).totalTime)*10)));
        }

        BarDataSet barDataSet = new BarDataSet(yVels, "data 1");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColor(Color.YELLOW);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);

        breathChart.setScaleEnabled(false);
        breathChart.setData(barData);
    }


    @Override
    public void onDestroy() {
        listener.onClickedDestroy();
        super.onDestroy();
    }
}
