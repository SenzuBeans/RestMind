package com.alternative.cap.restmindv3.ui.narration;

import android.os.Bundle;
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
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationAdapter;
import com.alternative.cap.restmindv3.ui.narration.adapter.NarrationSubAdapter;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.alternative.cap.restmindv3.util.NarrationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    private ArrayList<String> narrationHeader;// = new String[]{"test 1", "test 2"};
    private ArrayList<String> narrationId;// = new String[][]{{"Song 1", "Song 2", "Song 3", "Song 4", "Song 5"},{"Song 6", "Song 7", "Song 8", "Song 9", "Song 10"}};
    private ArrayList<MediaItem> tempMediaList;
    private ArrayList<ArrayList<MediaItem>> narrationList;

    Random random = new Random();
    int x = random.nextInt(1000);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        narrationHeader = new ArrayList<>();
        narrationList = new ArrayList<ArrayList<MediaItem>>();
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
        narrationRecyclerView = root.findViewById(R.id.narrationRecyclerView);
        narrationContentContainer = root.findViewById(R.id.narrationContentContainer);
    }

    private void workbench(View root, Bundle savedInstanceState) {
        hideNavigationBar();
        narrationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("narration").getChildren()) {
                    narrationHeader.add(ds.getKey());
                    NarrationItem item = ds.getValue(NarrationItem.class);

                    narrationId = new ArrayList<String>(Arrays.asList(item.rawId.split(",")));
                    tempMediaList = new ArrayList<>();

                    for (String s : narrationId) {
                        tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                    }
                    narrationList.add(tempMediaList);
                }
                if (narrationRecyclerView.getAdapter() == null) {
                    adapter = new NarrationAdapter( getContext(), narrationHeader, narrationList, NarrationFragment.this );
                    narrationRecyclerView.setAdapter( adapter );
                }else{
                    adapter.notifyDataSetChanged();
                }

                database.getReference().removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void hideNavigationBar() {
        this.getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClicked(ArrayList<MediaItem> passingDataList, int current, String passingHeader) {
        narrationRecyclerView.setVisibility(View.GONE);
        narrationContentContainer.setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction()
                .add(R.id.narrationContentContainer, NarrationPlayerFragment.newInstance(passingHeader, passingDataList, current, getContext(), new NarrationPlayerFragment.MusicListener() {
                    @Override
                    public void onDestroy() {
                        narrationRecyclerView.setVisibility(View.VISIBLE);
                        narrationContentContainer.setVisibility(View.GONE);
                    }
                }))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMoreClicked(ArrayList<MediaItem> passingDataList, String passingHeader) {
        narrationRecyclerView.setVisibility(View.GONE);
        narrationContentContainer.setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.narrationContentContainer, NarrationListFragment.newInstance(passingDataList, passingHeader, new NarrationListFragment.NarrationListListener(){
                    @Override
                    public void onDestroy(){
                        narrationRecyclerView.setVisibility(View.VISIBLE);
                        narrationContentContainer.setVisibility(View.GONE);
                    }
                }, this))
                .commit();
    }

}