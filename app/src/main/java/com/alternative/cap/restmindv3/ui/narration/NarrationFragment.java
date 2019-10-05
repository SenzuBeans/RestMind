package com.alternative.cap.restmindv3.ui.narration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.ui.music.MusicPlayerFragment;
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationAdapter;
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationSubAdapter;
import com.alternative.cap.restmindv3.util.MusicItem;
import com.alternative.cap.restmindv3.util.NarrationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NarrationFragment extends Fragment implements NarrationSubAdapter.NarrationSubListener {

    private RecyclerView narrationRecyclerView;
    private FrameLayout narrationContentContainer;
    private NarrationAdapter adapter;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference narrationRef;
    private DatabaseReference musicRef;

    private ArrayList<String> header;// = new String[]{"test 1", "test 2"};
    private ArrayList<String> mediaId;// = new String[][]{{"Song 1", "Song 2", "Song 3", "Song 4", "Song 5"},{"Song 6", "Song 7", "Song 8", "Song 9", "Song 10"}};
    private ArrayList<MusicItem> tempMediaList;
    private ArrayList<ArrayList<MusicItem>> mediaList;

    Random random = new Random();
    int x = random.nextInt(1000);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        header = new ArrayList<>();
        mediaList = new ArrayList<ArrayList<MusicItem>>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_narration, container, false);
        initInstance(root, savedInstanceState);
        workbench(root, savedInstanceState);
        return root;
    }

    private void initInstance(View root, Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        narrationRef = database.getReference().child("narration");
        musicRef = database.getReference().child("sound");
        narrationRecyclerView = root.findViewById(R.id.narrationRecyclerView);
        narrationContentContainer = root.findViewById(R.id.narrationContentContainer);
    }

    private void workbench(View root, Bundle savedInstanceState) {
        hideNavigationBar();
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("narration").getChildren()) {
                    if (!ds.getKey().equals("Step")) {
                        Log.d("dodo", "onDataChange: " + ds.getKey());
                        header.add(ds.getKey());
                        NarrationItem item = ds.getValue(NarrationItem.class);

                        mediaId = new ArrayList(Arrays.asList(item.rawId.split(",")));
                        tempMediaList = new ArrayList<>();

                        for (String s : mediaId) {
                            tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MusicItem.class));
                        }
                        mediaList.add(tempMediaList);
                    }
                }
                updateAdapter(root);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateAdapter(View root) {
        narrationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NarrationAdapter(getContext(), header, mediaList, this);

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

    @Override
    public void onItemClicked(ArrayList<MusicItem> passingDataList, int current) {
        narrationRecyclerView.setVisibility(View.GONE);
        narrationContentContainer.setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction()
                .add(R.id.narrationContentContainer, MusicPlayerFragment.newInstance(passingDataList, current, getContext(), new MusicPlayerFragment.MusicListener() {
                    @Override
                    public void onDestory() {
                        narrationRecyclerView.setVisibility(View.VISIBLE);
                        narrationContentContainer.setVisibility(View.GONE);
                    }
                }))
                .addToBackStack(null)
                .commit();
    }
}