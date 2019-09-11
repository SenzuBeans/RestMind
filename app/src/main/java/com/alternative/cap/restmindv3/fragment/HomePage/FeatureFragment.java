package com.alternative.cap.restmindv3.fragment.HomePage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.manager.adapter.FeatureFragmentAdapter;

public class FeatureFragment extends Fragment {

    private RecyclerView featureFragmentRecyclerView;
    private float touchingPoint = 0;
    LinearSnapHelper snapHelper;

    public FeatureFragment() {
    }

    public static FeatureFragment newInstance() {
        Bundle args = new Bundle();
        FeatureFragment fragment = new FeatureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feature, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        snapHelper = new LinearSnapHelper();
        featureFragmentRecyclerView = rootView.findViewById(R.id.featureFragmentRecyclerView);
    }

    private void workbench(Bundle savedInstanceState) {
        featureFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        featureFragmentRecyclerView.setAdapter(new FeatureFragmentAdapter(getActivity()));
        featureFragmentRecyclerView.setLayoutFrozen(true)   ;
        snapHelper.attachToRecyclerView(featureFragmentRecyclerView);
    }
}
