package com.alternative.cap.restmindv3.ui.background;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.alternative.cap.restmindv3.util.NarrationItem;
import com.alternative.cap.restmindv3.util.VideoItem;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BackgroundFragment extends Fragment{

    private ArrayList<MediaItem> mediaList1;
    private ArrayList<MediaItem> mediaList2;
    private ArrayList<MediaItem> mediaList3;

    private int mediaSelect1 = -1;
    private int mediaSelect2 = -1;
    private int mediaSelect3 = -1;

    private RecyclerView bgRecyclerView1;
    private RecyclerView bgRecyclerView2;
    private RecyclerView bgRecyclerView3;
    private BackgroundAdapter adapter1;
    private BackgroundAdapter adapter2;
    private BackgroundAdapter adapter3;

    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mediaList1 = new ArrayList<>();
        mediaList2 = new ArrayList<>();
        mediaList3 = new ArrayList<>();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_background, container, false);
        initInstance(root, savedInstanceState);
        return root;
    }

    private void initInstance(View root, Bundle savedInstanceState) {
        hideNavigationBar();
        getData();
        bgRecyclerView1 = root.findViewById(R.id.bgRecyclerView1);
        bgRecyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        bgRecyclerView2 = root.findViewById(R.id.bgRecyclerView2);
        bgRecyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        bgRecyclerView3 = root.findViewById(R.id.bgRecyclerView3);
        bgRecyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getData() {
        Random random = new Random();
        int x = random.nextInt(1000);
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);

        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NarrationItem bgSoundItem = dataSnapshot.child("LOG").child("BGSOUND1").getValue(NarrationItem.class);
                ArrayList<String> bgSoundId = new ArrayList(Arrays.asList(bgSoundItem.rawId.split(",")));
                ArrayList<MediaItem> tempMediaList = new ArrayList<>();

                for (String s : bgSoundId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                mediaList1 = tempMediaList;
                adapter1 = new BackgroundAdapter(getContext(), mediaList1, mediaSelect1);
              
                bgSoundItem = dataSnapshot.child("LOG").child("BGSOUND2").getValue(NarrationItem.class);
                bgSoundId = new ArrayList(Arrays.asList(bgSoundItem.rawId.split(",")));
                tempMediaList = new ArrayList<>();

                for (String s : bgSoundId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                mediaList2 = tempMediaList;
                adapter2 = new BackgroundAdapter(getContext(), mediaList2, mediaSelect2);


                bgSoundItem = dataSnapshot.child("LOG").child("BGSOUND3").getValue(NarrationItem.class);
                bgSoundId = new ArrayList(Arrays.asList(bgSoundItem.rawId.split(",")));
                tempMediaList = new ArrayList<>();

                for (String s : bgSoundId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                mediaList3 = tempMediaList;
                adapter3 = new BackgroundAdapter(getContext(), mediaList3, mediaSelect3);

                updateRecyclerAdapter();

                database.getReference().removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateRecyclerAdapter() {
        if (bgRecyclerView1.getAdapter() == null){
            bgRecyclerView1.setAdapter(adapter1);
        }else{
            adapter1.setPlayingMedia(mediaSelect1);
            adapter1.notifyDataSetChanged();
        }
        if (bgRecyclerView2.getAdapter() == null){
            bgRecyclerView2.setAdapter(adapter2);
        }else{
            adapter2.setPlayingMedia(mediaSelect2);
            adapter2.notifyDataSetChanged();
        }
        if (bgRecyclerView3.getAdapter() == null){
            bgRecyclerView3.setAdapter(adapter3);
        }else{
            adapter3.setPlayingMedia(mediaSelect3);
            adapter3.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    private void hideNavigationBar() {
        this.getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}