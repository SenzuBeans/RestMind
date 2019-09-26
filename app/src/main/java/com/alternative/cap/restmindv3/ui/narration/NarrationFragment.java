package com.alternative.cap.restmindv3.ui.narration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationAdapter;

import java.util.ArrayList;

public class NarrationFragment extends Fragment {

    private RecyclerView narrationRecyclerView;
    private NarrationAdapter adapter;
    private String[] headerMedia = new String[]{"test 1", "test 2"};
    private String[][] nameMedia = new String[][]{{"Song 1", "Song 2", "Song 3", "Song 4", "Song 5"},{"Song 6", "Song 7", "Song 8", "Song 9", "Song 10"}};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_narration, container, false);
        workbench(root,savedInstanceState);
        return root;
    }

    private void workbench(View root, Bundle savedInstanceState) {
        hideNavigationBar();
        narrationRecyclerView = root.findViewById(R.id.narrationRecyclerView);
        narrationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NarrationAdapter(getContext(), headerMedia, nameMedia);

        narrationRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    private void hideNavigationBar() {
        this.getActivity().getWindow().getDecorView()
                .setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}