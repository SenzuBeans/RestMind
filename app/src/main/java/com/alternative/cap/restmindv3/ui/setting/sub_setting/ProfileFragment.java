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

    private TextView profileUserName;
    private TextView profileUserEmail;
    private BarChart barChart;
    private TextView barResult;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;

    private ArrayList<BreathLogItem> log;
    private ArrayList<BarEntry> yVels;

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

        profileUserName = rootView.findViewById( R.id.profileUserName );
        profileUserEmail = rootView.findViewById( R.id.profileUserEmail );

        barChart = rootView.findViewById( R.id.barChart );
        barResult = rootView.findViewById( R.id.barResult );

        initBarChart();
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

        //Get Data
        getData();

    }

    private void getData() {
        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.child( user.getUid() ).getValue( UserDetails.class );
                profileUserName.setText( user.getDisplayName() );
                profileUserEmail.setText( user.getEmail() );
                if (log == null){
                    log = new ArrayList<>(  );
                }
                log = userDetails.breath_log;
                setBarChartData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void initBarChart() {
        barChart.setNoDataText( "Tap to refresh information" );
        barChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                barResult.setText( "เดือนนี้..วันที่ " + (int) e.getX() + " \nคุณได้ฝึกกำหนดลมหายใจ " + (int) e.getY() + " นาที" );
            }

            @Override
            public void onNothingSelected() {

            }
        } );
        barChart.getXAxis().setEnabled( true );
        barChart.getAxisRight().setEnabled( false );
        barChart.getAxisLeft().setEnabled( true );
        barChart.getAxisLeft().setTextColor( Color.WHITE );
        barChart.getXAxis().setTextColor( Color.WHITE );

        barChart.setBackgroundColor( Color.BLACK );
        barChart.setGridBackgroundColor( Color.DKGRAY );

        barChart.setDrawGridBackground( true );

        barChart.setDrawBorders( true );
        barChart.getDescription().setEnabled( false );
        barChart.setPinchZoom( false );
        barChart.getLegend().setEnabled( false );

        barChart.getXAxis().setLabelCount( 7, true );
        barChart.getXAxis().setGranularityEnabled( true );
    }

    private void setBarChartData() {
        if (log != null) {
            yVels = new ArrayList<>();

            for (int i = 0; i < log.size(); i++) {
                yVels.add( new BarEntry( (float) (Integer.parseInt( log.get( i ).date )), (float) (Integer.parseInt( log.get( i ).totalTime )) ) );
            }

            Log.d( "dodo", "onDataChange: " + log.get( 0 ).date );


            BarDataSet barDataSet = new BarDataSet( yVels, "" );
            barDataSet.setAxisDependency( YAxis.AxisDependency.LEFT );
            barDataSet.setColor( Color.YELLOW );

            BarData barData = new BarData( barDataSet );
            barData.setDrawValues( false );

            barChart.setScaleEnabled( false );
            barChart.setData( barData );

            barChart.notifyDataSetChanged();
            barChart.invalidate();
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
