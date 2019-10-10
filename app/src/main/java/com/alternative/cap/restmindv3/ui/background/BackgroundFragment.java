package com.alternative.cap.restmindv3.ui.background;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.manager.Contextor;
import com.alternative.cap.restmindv3.util.VideoItem;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Random;

public class BackgroundFragment extends Fragment {

    private SharedPreferences shared;

    private TextureView backgroundVideo;
    private SimpleExoPlayer backgroundPlayer;
    private MediaSource backgroundMediaSource;
    private ConcatenatingMediaSource backgroundConcatenatingMediaSource;
    private DefaultDataSourceFactory backgroundDataSourceFactory;
    private TextView backgroundId;

    private ArrayList<VideoItem> videoList;

    private int currentVideo = 0;
    private float touchingPoint = 0;
    private boolean clicked;
    private boolean isPlay = false;
    private boolean isStart = true;
    private boolean isMoveLeft;
    private boolean isMoveRight;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference videoRef;

    private SimpleTarget tapTarget;
    private SimpleTarget leftTarget;
    private SimpleTarget rightTarget;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        videoRef = database.getReference().child("video");
        videoList = new ArrayList<>();
        shared = getContext().getSharedPreferences("BackgroundWT", 0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_background, container, false);
        initInstance(root, savedInstanceState);
        workbench(root, savedInstanceState);
        return root;
    }

    private void initInstance(View root, Bundle savedInstanceState) {


        backgroundVideo = root.findViewById(R.id.backgroundVideoView);
        backgroundId = root.findViewById(R.id.backgroundID);


        Random random = new Random();
        int x = random.nextInt(1000);
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);

        videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    VideoItem item = ds.getValue(VideoItem.class);
                    videoList.add(item);
                }
                doStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tapTarget = new SimpleTarget.Builder(getActivity())
                .setPoint(backgroundVideo)
                .setShape(new Circle(0f))
                .setTitle("How to use")
                .setDescription("\nThere have three commanded." +
                        "\n\n\n\t\t\tSingle Tap Screen" +
                        "\n\n\t - for play and pause Background Video" +
                        "\n\n\n\t\t\tSlide Right on Screen" +
                        "\n\n\t - for move to next Background Video" +
                        "\n\n\n\t\t\tSlide Left on Screen" +
                        "\n\n\t - for move to previous Background Video")
                .build();

    }

    private void workbench(View root, Bundle savedInstanceState) {
        hideNavigationBar();

        backgroundVideo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                backgroundVideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                boolean isBackgroundWT = shared.getBoolean("isBackgroundWT", false);
                if (!isBackgroundWT) {
                    Spotlight spotlight = Spotlight.with(getActivity());
                    spotlight.setOverlayColor(R.color.spotlight_bg);
                    spotlight.setAnimation(new DecelerateInterpolator(1f));
                    spotlight.setTargets(tapTarget);
                    spotlight.setClosedOnTouchedOutside(true);

                    spotlight.setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                        @Override
                        public void onStarted() {

                        }

                        @Override
                        public void onEnded() {
                            Snackbar.make(backgroundVideo, "Don't show walkthrough again!!!", Snackbar.LENGTH_LONG).setAction("Confirm!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences.Editor editor = shared.edit();
                                    editor.putBoolean("isBackgroundWT", true);
                                    editor.commit();
                                    Snackbar.make(backgroundVideo, "Walkthrough will not again!!!", Snackbar.LENGTH_SHORT).show();
                                }
                            }).show();
                        }
                    });
                    spotlight.start();
                }

            }
        });

    }

    private void setPlayer() {

        if (backgroundPlayer == null) {
            backgroundPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
//            backgroundVideo.setPlayer(backgroundPlayer);

            backgroundDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "Background Player"));
            if (backgroundConcatenatingMediaSource == null)
                backgroundConcatenatingMediaSource = new ConcatenatingMediaSource();

            updateDataList(videoList);
        }

        if (backgroundPlayer != null) {
            backgroundPlayer.setPlayWhenReady(false);
//            backgroundVideo.hideController();
        }

        backgroundPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        backgroundPlayer.setVideoTextureView(backgroundVideo);
    }

    private void updateDataList(ArrayList<VideoItem> dataList) {
        for (VideoItem item : dataList) {
            backgroundMediaSource = new ProgressiveMediaSource.Factory(backgroundDataSourceFactory)
                    .createMediaSource(Uri.parse(item.link));
            backgroundConcatenatingMediaSource.addMediaSource(backgroundMediaSource);
        }

        backgroundPlayer.prepare(backgroundConcatenatingMediaSource);
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

    private void doStuff() {
        setPlayer();
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
                        } else if (isMoveLeft) {
                            if (backgroundPlayer.getCurrentWindowIndex() == 0) {
                                backgroundPlayer.seekTo(videoList.size() - 1, 0);
                            } else {
                                backgroundPlayer.previous();
                            }
                        } else if (isMoveRight) {
                            if (backgroundPlayer.getCurrentWindowIndex() == videoList.size() - 1) {
                                backgroundPlayer.seekTo(0, 0);
                            } else {
                                backgroundPlayer.next();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void focusPlayer() {
        if (backgroundVideo != null) {
            if (backgroundPlayer.isPlaying()) {
                backgroundPlayer.setPlayWhenReady(false);
            } else {
                backgroundPlayer.setPlayWhenReady(true);
            }
        }
    }

}