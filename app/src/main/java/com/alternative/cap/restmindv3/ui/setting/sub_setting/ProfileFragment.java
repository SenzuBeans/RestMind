package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.fragment.RegisterFragment;
import com.alternative.cap.restmindv3.util.BreathLogItem;
import com.alternative.cap.restmindv3.util.SettingListener;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

    private RecyclerView profileRecyclerView;
    private TextView profileUserName;
    private TextView profileUserEmail;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;

    private ProfileAdapter adapter;

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

        profileUserName = rootView.findViewById(R.id.profileUserName);
        profileUserEmail = rootView.findViewById(R.id.profileUserEmail);
        profileRecyclerView = rootView.findViewById(R.id.profileRecyclerView);
    }

    private void checkRegis(View rootView, Bundle savedInstanceState) {
        if (user.getDisplayName().equals("VISITOR")) {
            rootView.findViewById(R.id.profileContentContainer).setVisibility(View.VISIBLE);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.profileContentContainer, RegisterFragment.newInstance(new RegisterFragment.RegisterListener() {
                        @Override
                        public void onRegis() {
                            rootView.findViewById(R.id.profileContentContainer).setVisibility(View.GONE);
                            getChildFragmentManager().popBackStack();
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
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.child(user.getUid()).getValue(UserDetails.class);
                profileUserName.setText(user.getDisplayName());
                profileUserEmail.setText(user.getEmail());
                ArrayList<BreathLogItem> log = userDetails.breath_log;


                if (profileRecyclerView.getAdapter() == null){
                    adapter = new ProfileAdapter(log);
                    profileRecyclerView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();

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



    @Override
    public void onDestroy() {
        listener.onClickedDestroy();
        super.onDestroy();
    }


    private class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{

        ArrayList<BreathLogItem> log;

        public ProfileAdapter(ArrayList<BreathLogItem> log) {
            this.log = log;
        }

        @NonNull
        @Override
        public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_chart, parent,false);
            return new ProfileViewHolder(root);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
            if (!holder.chartInit) {
                holder.setChart(position);
            }
            holder.updateData(log,position);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class ProfileViewHolder extends RecyclerView.ViewHolder {

            private View root;
            private BarChart chart;
            private TextView result;
            private boolean chartInit = false;

            public ProfileViewHolder(@NonNull View itemView) {
                super(itemView);
                root = itemView;
                chart = itemView.findViewById(R.id.chart);
                result = itemView.findViewById(R.id.result);
            }

            public void setChart(int i){
                chart.setNoDataText("Tap to refresh information");
                chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        if (i == 0) {
                            result.setText("เดือนนี้..วันที่ " + (int) e.getX() + " \nคุณได้ฝึกกำหนดลมหายใจ " + (int) e.getY() + " นาที");
                        }else{
                            result.setText("\tวันที่ "+(int)e.getX() +" \nคุณได้พลาดไปแล้ว "+(int)e.getY()+" นาที");
                        }
                    }
                    @Override
                    public void onNothingSelected() {

                    }
                });
                chart.getXAxis().setEnabled(true);
                chart.getAxisRight().setEnabled(false);
                chart.getAxisLeft().setEnabled(true);
                chart.getAxisLeft().setTextColor( Color.WHITE );
                chart.getXAxis().setTextColor( Color.WHITE );

                chart.setBackgroundColor(Color.BLACK);
                chart.setGridBackgroundColor(Color.DKGRAY);

                chart.setDrawGridBackground(true);

                chart.setDrawBorders(true);
                chart.getDescription().setEnabled(false);
                chart.setPinchZoom(false);
                chart.getLegend().setEnabled(false);
                this.chartInit = true;
            }

            public void updateData(ArrayList<BreathLogItem> log, int position) {

                if (log != null) {
                    ArrayList<BarEntry> yVels = new ArrayList<>();
                    ArrayList<BarEntry> yDismissVels = new ArrayList<>();

                    for (int i = 0; i < log.size(); i++) {
                        yVels.add(new BarEntry((float) (Integer.parseInt(log.get(i).date)), (float) (Integer.parseInt(log.get(i).totalTime))));
                        yDismissVels.add(new BarEntry((float) (Integer.parseInt(log.get(i).date)), (float) (Integer.parseInt(log.get(i).dismissTime))));
                    }
                    if (position == 0) {
                        setData(yVels,position);
                    }else {
                        setData(yDismissVels,position);
                    }
                }
            }

            private void setData(ArrayList<BarEntry> yVels , int i) {
                BarDataSet barDataSet = new BarDataSet(yVels, "");
                barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                if (i == 0)
                    barDataSet.setColor(Color.YELLOW);
                else
                    barDataSet.setColor(Color.RED);

                BarData barData = new BarData(barDataSet);
                barData.setDrawValues(false);

                chart.setScaleEnabled(false);
                chart.setData(barData);

                chart.notifyDataSetChanged();
                chart.invalidate();
            }
        }
    }
}
