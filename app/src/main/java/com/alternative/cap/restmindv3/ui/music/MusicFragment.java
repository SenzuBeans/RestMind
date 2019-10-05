package com.alternative.cap.restmindv3.ui.music;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MusicItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MusicFragment extends Fragment implements MusicAdapter.MediaListAdapterListener {

    private static final int REQUEST = 1;
    private ArrayList<MusicItem> mediaList;

    private MusicAdapter musicAdapter;
    private RecyclerView mediaListRecyclerView;
    private FrameLayout soundPlayerContentContainer;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference musicRef;

    public MusicFragment() {
    }

    public static MusicFragment newInstance() {

        Bundle args = new Bundle();

        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mediaList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        hideNavigationBar();
        mediaListRecyclerView = rootView.findViewById(R.id.mediaListRecyclerView);
        soundPlayerContentContainer = rootView.findViewById(R.id.soundPlayerContentContainer);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        musicRef = database.getReference().child("sound");
        
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        getMusic();
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

    public void doStuff() {
        musicAdapter = new MusicAdapter(MusicFragment.this, mediaList, getContext());
        mediaListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mediaListRecyclerView.setAdapter(musicAdapter);
    }

    public void getMusic() {
        Random random = new Random();
        int x = random.nextInt(1000);
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);

        musicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MusicItem item = ds.getValue(MusicItem.class);
                    mediaList.add(item);
                }
                doStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public void onItemClicked(ArrayList<MusicItem> passingDataList, int current) {
        mediaListRecyclerView.setVisibility(View.GONE);
        soundPlayerContentContainer.setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction()
                .add(R.id.soundPlayerContentContainer, MusicPlayerFragment.newInstance(passingDataList,current,getContext()))
                .addToBackStack(null)
                .commit();
    }
}