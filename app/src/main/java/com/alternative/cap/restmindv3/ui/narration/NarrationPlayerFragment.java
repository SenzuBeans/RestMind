package com.alternative.cap.restmindv3.ui.narration;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class NarrationPlayerFragment extends Fragment {

    private CircularImageView narrationPlayerCover;
    private Animation anim;
    private SimpleExoPlayer narrationPlayer;
    private MediaSource narrationMediaSource;
    private ConcatenatingMediaSource narrationConcatenatingMediaSource;
    private DefaultDataSourceFactory narrationDataSourceFactory;
    private PlayerControlView narrationController;

    private TextView narrationPlayerHeader;
    private TextView narrationPlayerName;
    private TextView narrationPlayerArtist;

    private static MusicListener listener;

    private static ArrayList<MediaItem> dataList;
    private static int currentSound = 0;
    private static String header = "Music List";
    private static Context cons;

    private boolean isAnimationPlaying = false;

    public NarrationPlayerFragment() {
    }

    public static NarrationPlayerFragment newInstance(ArrayList<MediaItem> passingDataList, int passingCurrent, Context context, MusicListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        currentSound = passingCurrent;
        cons = context;

        NarrationPlayerFragment fragment = new NarrationPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NarrationPlayerFragment newInstance(String passingHeader, ArrayList<MediaItem> passingDataList, int passingCurrent, Context context, MusicListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        currentSound = passingCurrent;
        cons = context;
        header = passingHeader;

        NarrationPlayerFragment fragment = new NarrationPlayerFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_narration_player, container, false);
        initInstance(rootView, savedInstanceState);
        workplace(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        narrationController = rootView.findViewById(R.id.stepPlayerControlView);
        narrationPlayerCover = rootView.findViewById(R.id.stepPlayerCover);

        narrationPlayerHeader = rootView.findViewById(R.id.stepPlayerHeader);
        narrationPlayerName = rootView.findViewById(R.id.stepPlayerName);
        narrationPlayerArtist = rootView.findViewById(R.id.stepPlayerArtist);
    }

    private void workplace(View rootView, Bundle savedInstanceState) {
        narrationPlayerHeader.setText(header);

        if (narrationPlayer == null) {
            narrationPlayer = ExoPlayerFactory.newSimpleInstance(cons, new DefaultTrackSelector());
            narrationController.setPlayer(narrationPlayer);

            narrationDataSourceFactory = new DefaultDataSourceFactory(cons, Util.getUserAgent(cons, "Sound Player"));
            if (narrationConcatenatingMediaSource == null)
                narrationConcatenatingMediaSource = new ConcatenatingMediaSource();

            updateDataList(dataList);
        }

        if (narrationPlayer != null) {
            narrationPlayer.seekTo(currentSound, 0);
            narrationPlayer.setPlayWhenReady(true);
        }
        narrationPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        narrationController.setVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                narrationController.show();
            }
        });
        narrationPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (!isPlaying) {
                    stopAnimation();
                }
            }
        });

        narrationPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                int newIndex = narrationPlayer.getCurrentWindowIndex();
                if (newIndex != currentSound){
                    stopAnimation();
                    loadAnimation();
                    currentSound = newIndex;
                    Log.d("dodo", "onPositionDiscontinuity: " + currentSound);
                }
            }
        });

        narrationPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    loadAnimation();
                }
            }
        });
    }

    private void loadAnimation() {
        narrationPlayerName.setText(dataList.get(narrationPlayer.getCurrentWindowIndex()).name);
        narrationPlayerArtist.setText(dataList.get(narrationPlayer.getCurrentWindowIndex()).artist);

        Glide.with(cons)
                .load(dataList.get(narrationPlayer.getCurrentWindowIndex()).image_link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(narrationPlayerCover);

        anim = new RotateAnimation(0, 360, narrationPlayerCover.getPivotX(), narrationPlayerCover.getPivotY());
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(15000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (narrationPlayer.isPlaying()) {
                    narrationPlayerCover.startAnimation(anim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        narrationPlayerCover.startAnimation(anim);
        isAnimationPlaying = true;
    }

    private void stopAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAnimationPlaying) {
                    narrationPlayerCover.getAnimation().cancel();
                    isAnimationPlaying = false;
                }
            }
        });
    }

    private void updateDataList(ArrayList<MediaItem> dataList) {
        for (MediaItem item : dataList) {
            narrationMediaSource = new ProgressiveMediaSource.Factory(narrationDataSourceFactory)
                    .createMediaSource(Uri.parse(item.link));
            narrationConcatenatingMediaSource.addMediaSource(narrationMediaSource);
        }
        narrationPlayer.prepare(narrationConcatenatingMediaSource);
    }

    @Override
    public void onStop() {
        if (narrationPlayer != null) {
            narrationController.setPlayer(null);
            narrationPlayer.release();
            narrationPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestroy();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (narrationPlayer != null) {
            narrationController.setPlayer(null);
            narrationPlayer.release();
            narrationPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestroy();
        super.onDestroy();
    }

    public interface MusicListener {
        void onDestroy();
    }
}
