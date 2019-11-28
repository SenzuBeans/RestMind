package com.alternative.cap.restmindv3.ui.background;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.wooplr.spotlight.SpotlightView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BackgroundFragment extends Fragment
        implements BackgroundAdapter.BackgroundAdapterListener {

    private ArrayList<MediaItem> mediaList1;
    private ArrayList<MediaItem> mediaList2;
    private ArrayList<MediaItem> mediaList3;

    private int mediaSelect1 = -1;
    private int mediaSelect2 = -1;
    private int mediaSelect3 = -1;
    private final static int MAX_VOLUME = 100;

    private RecyclerView bgRecyclerView1;
    private RecyclerView bgRecyclerView2;
    private RecyclerView bgRecyclerView3;
    private BackgroundAdapter adapter1;
    private BackgroundAdapter adapter2;
    private BackgroundAdapter adapter3;

    private ImageButton volumeSettingBtn;
    private LinearLayout volumeLayout;
    private LinearLayout toggleLayout;
    private SeekBar seekVolume1;
    private SeekBar seekVolume2;
    private SeekBar seekVolume3;

    private TextView playerText1;
    private TextView playerText2;
    private TextView playerText3;

    private Animation fadeIn;
    private Animation fadeOut;
    private SpotlightView spotlightView;

    private FirebaseUser user;
    private FirebaseDatabase database;

    private SimpleExoPlayer ExoPlayer1;
    private MediaSource MediaSource1;
    private ConcatenatingMediaSource ConcatenatingMediaSource1;
    private DefaultDataSourceFactory DataSourceFactory1;

    private SimpleExoPlayer ExoPlayer2;
    private MediaSource MediaSource2;
    private ConcatenatingMediaSource ConcatenatingMediaSource2;
    private DefaultDataSourceFactory DataSourceFactory2;

    private SimpleExoPlayer ExoPlayer3;
    private MediaSource MediaSource3;
    private ConcatenatingMediaSource ConcatenatingMediaSource3;
    private DefaultDataSourceFactory DataSourceFactory3;

    Thread x1;
    Thread x2;
    Thread x3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
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

        volumeSettingBtn = root.findViewById(R.id.volumeSettingBtn);
        volumeLayout = root.findViewById(R.id.volumeSettingLayout);
        toggleLayout = root.findViewById(R.id.volumeToggleLayout);
        seekVolume1 = root.findViewById(R.id.seekVolumePlayer1);
        seekVolume2 = root.findViewById(R.id.seekVolumePlayer2);
        seekVolume3 = root.findViewById(R.id.seekVolumePlayer3);

        playerText1 = root.findViewById(R.id.player1);
        playerText2 = root.findViewById(R.id.player2);
        playerText3 = root.findViewById(R.id.player3);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);

        volumeSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volumeLayout.startAnimation(fadeIn);
                volumeLayout.setVisibility(View.VISIBLE);
            }
        });

        seekVolume1.setProgress(100);
        seekVolume2.setProgress(100);
        seekVolume3.setProgress(100);

        updateVolume();

        toggleLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                volumeLayout.startAnimation(fadeOut);
                volumeLayout.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        volumeLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                return true;
            }
        });

//        showSpotlight();
    }
//
//    private void showSpotlight() {
//
//        spotlightView = new SpotlightView.Builder(getActivity())
//                .introAnimationDuration(400)
//                .enableRevealAnimation(true)
//                .performClick(true)
//                .fadeinTextDuration(400)
//                .headingTvColor(Color.parseColor("#eb273f"))
//                .headingTvSize(32)
//                .headingTvText("Love")
//                .subHeadingTvColor(Color.parseColor("#ffffff"))
//                .subHeadingTvSize(16)
//                .subHeadingTvText("Like the picture?\nLet others know.")
//                .maskColor(Color.parseColor("#dc000000"))
//                .target(volumeSettingBtn)
//                .lineAnimDuration(400)
//                .lineAndArcColor(Color.parseColor("#eb273f"))
//                .dismissOnTouch(true)
//                .dismissOnBackPress(true)
//                .enableDismissAfterShown(true)
//                .show();
//    }

    private void updateVolume() {

        if (mediaSelect1 >= 0) {
            playerText1.setText(mediaList1.get(mediaSelect1).name);
        } else {
            playerText1.setText("no song");
        }
        if (mediaSelect2 >= 0) {
            playerText2.setText(mediaList2.get(mediaSelect2).name);
        } else {
            playerText2.setText("no song");
        }
        if (mediaSelect3 >= 0) {
            playerText3.setText(mediaList3.get(mediaSelect3).name);
        } else {
            playerText3.setText("no song");
        }

        seekVolume1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                if (ExoPlayer1 != null)
                    ExoPlayer1.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekVolume2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                if (ExoPlayer2 != null)
                    ExoPlayer2.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekVolume3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                if (ExoPlayer3 != null)
                    ExoPlayer3.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                ArrayList<String> bgSoundId = new ArrayList<String>(Arrays.asList(bgSoundItem.rawId.split(",")));
                ArrayList<MediaItem> tempMediaList = new ArrayList<>();

                for (String s : bgSoundId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                mediaList1 = tempMediaList;
                adapter1 = new BackgroundAdapter(getContext(), mediaList1, mediaSelect1, BackgroundFragment.this::onItemSelect, 1);

                bgSoundItem = dataSnapshot.child("LOG").child("BGSOUND2").getValue(NarrationItem.class);
                bgSoundId = new ArrayList<String>(Arrays.asList(bgSoundItem.rawId.split(",")));
                tempMediaList = new ArrayList<>();

                for (String s : bgSoundId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                mediaList2 = tempMediaList;
                adapter2 = new BackgroundAdapter(getContext(), mediaList2, mediaSelect2, BackgroundFragment.this::onItemSelect, 2);


                bgSoundItem = dataSnapshot.child("LOG").child("BGSOUND3").getValue(NarrationItem.class);
                bgSoundId = new ArrayList<String>(Arrays.asList(bgSoundItem.rawId.split(",")));
                tempMediaList = new ArrayList<>();

                for (String s : bgSoundId) {
                    tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
                }

                mediaList3 = tempMediaList;
                adapter3 = new BackgroundAdapter(getContext(), mediaList3, mediaSelect3, BackgroundFragment.this::onItemSelect, 3);

                updateRecyclerAdapter();
                updatePlayer();
                updateListSong();

                database.getReference().removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updatePlayer() {
        if (ExoPlayer1 == null) {
            ExoPlayer1 = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            DataSourceFactory1 = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext()
                    , "Player 1"));
            if (ConcatenatingMediaSource1 == null)
                ConcatenatingMediaSource1 = new ConcatenatingMediaSource();

        }
        if (ExoPlayer2 == null) {
            ExoPlayer2 = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            DataSourceFactory2 = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext()
                    , "Player 2"));
            if (ConcatenatingMediaSource2 == null)
                ConcatenatingMediaSource2 = new ConcatenatingMediaSource();

        }

        if (ExoPlayer3 == null) {
            ExoPlayer3 = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            DataSourceFactory3 = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext()
                    , "Player 3"));
            if (ConcatenatingMediaSource3 == null)
                ConcatenatingMediaSource3 = new ConcatenatingMediaSource();

        }

        updateListSong();


    }

    private void updateListSong() {
        for (MediaItem item : mediaList1) {
            MediaSource1 = new ProgressiveMediaSource.Factory(DataSourceFactory1)
                    .createMediaSource(Uri.parse(item.link_2));
            ConcatenatingMediaSource1.addMediaSource(MediaSource1);
        }
        ExoPlayer1.prepare(ConcatenatingMediaSource1);

        for (MediaItem item : mediaList2) {
            MediaSource2 = new ProgressiveMediaSource.Factory(DataSourceFactory2)
                    .createMediaSource(Uri.parse(item.link_2));
            ConcatenatingMediaSource2.addMediaSource(MediaSource2);
        }
        ExoPlayer2.prepare(ConcatenatingMediaSource2);

        for (MediaItem item : mediaList3) {
            MediaSource3 = new ProgressiveMediaSource.Factory(DataSourceFactory3)
                    .createMediaSource(Uri.parse(item.link_2));
            ConcatenatingMediaSource3.addMediaSource(MediaSource3);
        }
        ExoPlayer3.prepare(ConcatenatingMediaSource3);
    }

    private void updateRecyclerAdapter() {
        if (bgRecyclerView1.getAdapter() == null) {
            bgRecyclerView1.setAdapter(adapter1);
        } else {
            adapter1.setPlayingMedia(mediaSelect1);
            adapter1.notifyDataSetChanged();
        }
        if (bgRecyclerView2.getAdapter() == null) {
            bgRecyclerView2.setAdapter(adapter2);
        } else {
            adapter2.setPlayingMedia(mediaSelect2);
            adapter2.notifyDataSetChanged();
        }
        if (bgRecyclerView3.getAdapter() == null) {
            bgRecyclerView3.setAdapter(adapter3);
        } else {
            adapter3.setPlayingMedia(mediaSelect3);
            adapter3.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
        updateVolume();
    }

    private void hideNavigationBar() {
        this.getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onItemSelect(int itemSelect, int path) {
        if (path == 1) {
            mediaSelect1 = itemSelect;
            if (mediaSelect1 >= 0) {
                x1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ExoPlayer1 != null) {
                            ExoPlayer1.seekTo(mediaSelect1, 0);
                            ExoPlayer1.setPlayWhenReady(true);

                        }
                        ExoPlayer1.setRepeatMode(Player.REPEAT_MODE_ONE);
                    }
                });
                x1.start();
            } else {
                x1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ExoPlayer1 != null){
                            ExoPlayer1.seekTo(0, 0);
                            ExoPlayer1.setPlayWhenReady(false);
                        }
                    }
                });
                x1.start();
                x1.interrupt();
            }
        } else if (path == 2) {
            mediaSelect2 = itemSelect;
            if (mediaSelect2 >= 0) {
                x2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ExoPlayer2 != null) {
                            ExoPlayer2.seekTo(mediaSelect2, 0);
                            ExoPlayer2.setPlayWhenReady(true);

                        }
                        ExoPlayer2.setRepeatMode(Player.REPEAT_MODE_ONE);
                    }
                });
                x2.start();
            } else {
                x2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ExoPlayer2 != null) {
                            ExoPlayer2.seekTo(0, 0);
                            ExoPlayer2.setPlayWhenReady(false);

                        }
                        ExoPlayer2.setRepeatMode(Player.REPEAT_MODE_ONE);
                    }
                });
                x2.start();
                x2.interrupt();

            }

        } else if (path == 3) {
            mediaSelect3 = itemSelect;

            if (mediaSelect3 >= 0) {
                x3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ExoPlayer3 != null) {
                            ExoPlayer3.seekTo(mediaSelect3, 0);
                            ExoPlayer3.setPlayWhenReady(true);

                        }
                        ExoPlayer3.setRepeatMode(Player.REPEAT_MODE_ONE);
                    }
                });
                x3.start();
            } else {
                x3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ExoPlayer3 != null) {
                            ExoPlayer3.seekTo(0, 0);
                            ExoPlayer3.setPlayWhenReady(false);

                        }
                        ExoPlayer3.setRepeatMode(Player.REPEAT_MODE_ONE);
                    }
                });
                x3.start();
                x3.interrupt();

            }

        }


        updateRecyclerAdapter();
        updateVolume();
    }

    private void stopPlayer() {
        if(ExoPlayer1 != null){
            ExoPlayer1.stop(true);
        }
        if(ExoPlayer2 != null){
            ExoPlayer2.stop(true);
        }
        if(ExoPlayer3 != null){
            ExoPlayer3.stop(true);
        }

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopPlayer();
        super.onDestroy();
    }
}