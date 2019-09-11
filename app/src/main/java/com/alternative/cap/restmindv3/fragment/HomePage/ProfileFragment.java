package com.alternative.cap.restmindv3.fragment.HomePage;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alternative.cap.restmindv3.R;
import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.Random;

public class ProfileFragment extends Fragment {

    private RadarView radarView;
    private ArrayList<RadarHolder> mDataList;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mDataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        radarView = rootView.findViewById(R.id.radarTest);
    }

    private void workbench(Bundle savedInstanceState){
        setUpData();
        radarView.setData(mDataList);
        radarView.setInteractive(false);
    }

    private void setUpData() {
        Random x = new Random();
        for (int i = 1; i <= 6 ; i++){
            mDataList.add(new RadarHolder(""+i, x.nextInt(5)));
        }
    }

}
