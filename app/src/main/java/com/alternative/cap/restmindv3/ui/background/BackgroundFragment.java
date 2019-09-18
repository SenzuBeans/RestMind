package com.alternative.cap.restmindv3.ui.background;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.VideoItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class BackgroundFragment extends Fragment {

    //    private RecyclerView backgroundRecyclerView;
//    private PageIndicator backgroundIndicator;
    private android.widget.VideoView backgroundVideo;
    private TextView backgroundId;

    private ArrayList<String> listLink;
    private ArrayList<String> listId;

    private int currentVideo = 0;
    private float touchingPoint = 0;
    private boolean clicked;
    private boolean isPlay = false;
    private boolean isStart = true;
    private boolean isMoveLeft;
    private boolean isMoveRight;

    private BackgroundAdapter adapter;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference videoRef;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_background, container, false);
        workbench(root, savedInstanceState);
        return root;
    }

    private void workbench(View root, Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        videoRef = database.getReference().child("video");

//        backgroundRecyclerView = root.findViewById(R.id.backgroundViewPager);
//        backgroundIndicator = root.findViewById(R.id.backgroundIndicator);
        backgroundVideo = root.findViewById(R.id.backgroundVideoView);
        backgroundId = root.findViewById(R.id.backgroundID);

        listLink = new ArrayList<>();
        listId = new ArrayList<>();

        Random random = new Random();
        int x = random.nextInt(1000);
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);

        videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    VideoItem item = ds.getValue(VideoItem.class);

                    listLink.add(item.link);
                    listId.add(ds.getKey());
                }
                doStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doStuff() {
//        adapter = new BackgroundAdapter(listLink, listId);
        setUri(listLink.get(currentVideo));
//        backgroundRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        backgroundRecyclerView.setAdapter(adapter);
//
//        backgroundIndicator.attachTo(backgroundRecyclerView);
        backgroundVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchingPoint = motionEvent.getX();
                        clicked = true;
                        isMoveLeft = false;
                        isMoveRight = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getX() < touchingPoint && Math.abs(motionEvent.getX() - touchingPoint) > 200f) {
                            Log.d("dodo", "onTouch: left" + motionEvent.getX());
                            isMoveLeft = true;
                            isMoveRight = false;
                            touchingPoint = motionEvent.getX();
                            clicked = false;
                        } else if (motionEvent.getX() > touchingPoint && Math.abs(motionEvent.getX() - touchingPoint) > 200f) {
                            Log.d("dodo", "onTouch: right" + motionEvent.getX());
                            isMoveRight = true;
                            isMoveLeft = false;
                            touchingPoint = motionEvent.getX();
                            clicked = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (clicked) {
                            focusPlayer();
                        }else if (isMoveLeft){
                            changeVideo(0);
                        }else if(isMoveRight){
                            changeVideo(1);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void changeVideo(int i) {
       if (i == 0) {
           if (currentVideo < listLink.size() && currentVideo > 0) {
               currentVideo--;
           }else if (currentVideo == 0) {
               currentVideo = listLink.size() - 1;
           }
       }else {
           if (currentVideo < listLink.size() - 1  && currentVideo >= 0) {
               currentVideo++;
           } else if (currentVideo == listLink.size() - 1) {
               currentVideo = 0;
           }
       }
        Log.d("dodo", "changeVideo: current : " + currentVideo + " : list size : "+ listLink.size());

       backgroundId.setText(listId.get(currentVideo));

        setUri(listLink.get(currentVideo));
    }

    public void setUri(String link) {
        Log.d("dodo", "setUri: " + link);
        backgroundVideo.setVideoURI(Uri.parse(link));
        backgroundVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                backgroundVideo.start();
            }
        });
        backgroundVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                backgroundVideo.start();
            }
        });
    }

    public void focusPlayer() {
        if (backgroundVideo != null) {
            if (!isPlay) {
                if (isStart) {
                    backgroundVideo.start();
                    isStart = false;
                    isPlay = true;
                } else {
                    backgroundVideo.resume();
                    isPlay = true;
                }
            } else {
                backgroundVideo.pause();
            }
        }
    }

}