package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.fragment.RegisterFragment;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.Profile.RankingActivity;
import com.alternative.cap.restmindv3.util.BreathLogItem;
import com.alternative.cap.restmindv3.util.SettingListener;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.Random;


public class ProfileFragment extends Fragment {


    static SettingListener listener;

    private ImageView profileImage;
    private TextView profileUserName;
    private TextView profileUserEmail;
    private ValueLineChart valueLineChart;
    private PieChart pieChart;
//    private BarChart barChart;
//    private TextView barResult;
//    private PieChart pieChart;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;
    private UserDetails userDetails;

    private ArrayList<BreathLogItem> log;

    Random random = new Random();
    int x = random.nextInt( 1000 );

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(SettingListener passingListener) {

        Bundle args = new Bundle();
        listener = passingListener;
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        init( savedInstanceState );
    }

    private void init(Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child( "users" );
        user = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_profile, container, false );
        initInsance( rootView, savedInstanceState );
        checkRegis( rootView, savedInstanceState );
        return rootView;
    }


    private void initInsance(View rootView, Bundle savedInstanceState) {
            reference.child( user.getUid() ).child( "temp_steam" ).setValue( x );

        profileImage = rootView.findViewById(R.id.profilePicture);
        profileUserName = rootView.findViewById( R.id.profileUserName );
        profileUserEmail = rootView.findViewById( R.id.profileUserEmail );

        valueLineChart = rootView.findViewById(R.id.timeLineChart);
        pieChart = rootView.findViewById(R.id.pieChart);

    }

    private void checkRegis(View rootView, Bundle savedInstanceState) {
        if (user.getDisplayName().equals( "VISITOR" )) {
            rootView.findViewById( R.id.profileContentContainer ).setVisibility( View.VISIBLE );
            getChildFragmentManager().beginTransaction()
                    .add( R.id.profileContentContainer, RegisterFragment.newInstance( new RegisterFragment.RegisterListener() {
                        @Override
                        public void onRegis() {
                            rootView.findViewById( R.id.profileContentContainer ).setVisibility( View.GONE );
                            getChildFragmentManager().popBackStack();
                        }
                    } ) )
                    .commit();
        }

        if (rootView.findViewById( R.id.profileContentContainer ).getVisibility() == View.GONE) {
            workbench( rootView, savedInstanceState );
        }
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        backBtn( rootView );
        getData();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RankingActivity.class));
            }
        });
    }

    private void getData() {
        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.child( user.getUid() ).getValue( UserDetails.class );
                profileUserName.setText( user.getDisplayName() );
                profileUserEmail.setText( user.getEmail() );
                if (log == null){
                    log = new ArrayList<>(  );
                }
                log = userDetails.breath_log;
//                setBarChartData();
                if (userDetails.totalTime == null){
                    userDetails.updateTotalTime(0);
                    userDetails.updateMissTime(0);
                }
//                setPieChartData();
                setChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void setChart() {
        if (log != null){

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xFF56B7F1);


            for (BreathLogItem bi : log){
                series.addPoint(new ValueLinePoint(bi.date, Float.parseFloat(bi.totalTime)));
            }
            valueLineChart.addSeries(series);
            valueLineChart.startAnimation();
            Log.d("dodo", "setChart: " + log.size());


            pieChart.addPieSlice(new PieModel("Success (Minutes)" , Float.parseFloat(userDetails.totalTime), Color.parseColor("#408DD2")));
            pieChart.addPieSlice(new PieModel("Fail (Minutes)" , Float.parseFloat(userDetails.missTime), Color.parseColor("#FC3F4D")));

            pieChart.startAnimation();

        }
    }


    private void backBtn(View rootView) {
        rootView.findViewById( R.id.settingProfileBackBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        } );
    }


    @Override
    public void onDestroy() {
        listener.onClickedDestroy();
        super.onDestroy();
    }
}
