package com.alternative.cap.restmindv3.ui.breath;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.BreathSongList;
import com.alternative.cap.restmindv3.util.BreathingMutableValue;

import com.alternative.cap.restmindv3.util.MediaItem;
import com.alternative.cap.restmindv3.util.NarrationItem;
//import com.azoft.carousellayoutmanager.CarouselLayoutManager;
//import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
//import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import io.github.deweyreed.scrollhmspicker.ScrollHmsPicker;


public class BreathSettingFragment extends Fragment {

    static BreathSettingListener listener;
    private FirebaseDatabase database;
    private FirebaseUser user;

    private RecyclerView breathSongSelectRecyclerView;
    private BreathSongSelectAdapter adapter;
    private Button breathSettingCancelBtn;
    private IndicatorSeekBar breathInhaleSeekBar;
    private IndicatorSeekBar breathHoldSeekBar;
    private IndicatorSeekBar breathExhaleSeekBar;
    private Button breathSettingSetBtn;
    private ScrollHmsPicker timePicker;

    private BreathingMutableValue breathData;
    //    private TimePickerDialog timePickerDialog;
    private long timer = 0;
    private long[] defaultBreathData;

    Random random = new Random();
    int x = random.nextInt(1000);

    public BreathSettingFragment() {
    }

    public static BreathSettingFragment newInstance(BreathSettingListener passingListener) {

        Bundle args = new Bundle();
        listener = passingListener;
        BreathSettingFragment fragment = new BreathSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        breathData = new BreathingMutableValue();
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_breath_setting, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        breathSettingCancelBtn = rootView.findViewById(R.id.breathSettingCancelBtn);
        breathInhaleSeekBar = rootView.findViewById(R.id.breathInhaleSeekBar);
        breathHoldSeekBar = rootView.findViewById(R.id.breathHoldSeekBar);
        breathExhaleSeekBar = rootView.findViewById(R.id.breathExhaleSeekBar);
        breathSettingSetBtn = rootView.findViewById(R.id.breathSettingSetBtn);
        timePicker = rootView.findViewById(R.id.timePicker);

        breathSongSelectRecyclerView = rootView.findViewById(R.id.breathSongSelectRecyclerView);
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        defaultBreathData = breathData.getBreathData();
        breathInhaleSeekBar.setProgress(defaultBreathData[0] / 1000);
        breathHoldSeekBar.setProgress(defaultBreathData[1] / 1000);
        breathExhaleSeekBar.setProgress(defaultBreathData[2] / 1000);
        timePicker.setHours((int) (defaultBreathData[3] / 3600000));
        timePicker.setMinutes((int) (defaultBreathData[3] / 60000));

        breathSettingCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                listener.onPopStack();
            }
        });
        breathSettingSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer = (timePicker.getHours() * 60 * 60 * 1000) + (timePicker.getMinutes() * 60 * 1000);
                long[] temp = {(long) (breathInhaleSeekBar.getProgress() * 1000)
                        , (long) (breathHoldSeekBar.getProgress() * 1000)
                        , (long) (breathExhaleSeekBar.getProgress() * 1000)
                        , timer};
                breathData.setBreathData(temp);

                listener.onSetSetting();
                getFragmentManager().popBackStack();
            }
        });
        breathSongSelectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new LinearSnapHelper().attachToRecyclerView(breathSongSelectRecyclerView);

        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NarrationItem narrationItem = dataSnapshot.child("LOG").child("BREATH").getValue(NarrationItem.class);
                ArrayList<String> narrationId = new ArrayList<String>(Arrays.asList(narrationItem.rawId.split(",")));
                ArrayList<MediaItem> tempMediaList = new ArrayList<>();

                for (String s : narrationId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                BreathSongList.dataList = tempMediaList;
                if (breathSongSelectRecyclerView.getAdapter() == null) {
                    adapter = new BreathSongSelectAdapter(getContext(), BreathSongList.dataList);
                    breathSongSelectRecyclerView.setAdapter(adapter);
                    breathSongSelectRecyclerView.getLayoutManager().scrollToPosition(BreathSongList.current);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public interface BreathSettingListener {
        void onSetSetting();

        void onPopStack();
    }


}
