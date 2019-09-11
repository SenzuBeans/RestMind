package com.alternative.cap.restmindv3.ui.breath;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MutableValue;

import com.warkiz.widget.IndicatorSeekBar;

import io.github.deweyreed.scrollhmspicker.ScrollHmsPicker;


public class BreathSettingFragment extends Fragment {

    static BreathSettingListener listener;

    private Button breathSettingCancelBtn;
    private IndicatorSeekBar breathInhaleSeekBar;
    private IndicatorSeekBar breathHoldSeekBar;
    private IndicatorSeekBar breathExhaleSeekBar;
    private Button breathSettingSetBtn;
    private ScrollHmsPicker timePicker;

    private MutableValue breathData;
    //    private TimePickerDialog timePickerDialog;
    private long timer = 0;
    private long[] defaultBreathData;

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
        breathData = new MutableValue();
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
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        defaultBreathData = breathData.getBreathData();
        breathInhaleSeekBar.setProgress(defaultBreathData[0] / 1000);
        breathHoldSeekBar.setProgress(defaultBreathData[1] / 1000);
        breathExhaleSeekBar.setProgress(defaultBreathData[2] / 1000);

        breathSettingCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
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
    }

    public interface BreathSettingListener{
        void onSetSetting();
    }


}
