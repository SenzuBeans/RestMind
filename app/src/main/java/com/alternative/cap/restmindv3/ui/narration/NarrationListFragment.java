package com.alternative.cap.restmindv3.ui.narration;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationListAdapter;
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationSubAdapter;
import com.alternative.cap.restmindv3.util.MediaItem;

import java.util.ArrayList;

public class NarrationListFragment extends Fragment{

    static NarrationListListener listener;
    static NarrationSubAdapter.NarrationSubListener subListener;

    private static ArrayList<MediaItem> dataList;
    private static String header;

    private RecyclerView narrationListRecyclerView;
    private TextView narrationListHeader;
    private RatingBar narrationListRatingBar;

    private NarrationListAdapter adapter;
    private FrameLayout narrationListContent;

    public NarrationListFragment() {
    }

    public static NarrationListFragment newInstance(ArrayList<MediaItem> passingDataList, String passingHeader, NarrationListListener narrationListListener , NarrationSubAdapter.NarrationSubListener passingSubListener) {

        Bundle args = new Bundle();
        listener = narrationListListener;
        dataList = passingDataList;
        header = passingHeader;
        subListener = passingSubListener;
        NarrationListFragment fragment = new NarrationListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_narration_list, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        narrationListRecyclerView = rootView.findViewById(R.id.narrationListRecyclerView);
        narrationListHeader = rootView.findViewById(R.id.narrationListHeader);
        narrationListContent = rootView.findViewById(R.id.narrationListContent);
        narrationListRatingBar = rootView.findViewById(R.id.narrationListRatingBar);
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        narrationListHeader.setText(header);
        adapter = new NarrationListAdapter(getContext(), dataList, header ,subListener);
        narrationListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        narrationListRecyclerView.setAdapter(adapter);
    }


    public interface NarrationListListener {
        void onDestroy();
    }
}
