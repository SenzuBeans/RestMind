package com.alternative.cap.restmindv3.ui.music;

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
import com.alternative.cap.restmindv3.util.MusicItem;
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

public class MusicPlayerFragment extends Fragment {

    private CircularImageView soundPlayerCover;
    private Animation anim;
    private SimpleExoPlayer soundPlayer;
    private MediaSource soundMediaSource;
    private ConcatenatingMediaSource soundConcatenatingMediaSource;
    private DefaultDataSourceFactory soundDataSourceFactory;
    private PlayerControlView soundController;

    private TextView soundPlayerHeader;
    private TextView soundPlayerName;
    private TextView soundPlayerArtist;

    private static MusicListener listener;

    private static ArrayList<MusicItem> dataList;
    private static int currentSound = 0;
    private static String header = "Music List";
    private static Context cons;

    private boolean isAnimationPlaying = false;

    public MusicPlayerFragment() {
    }

    public static MusicPlayerFragment newInstance(ArrayList<MusicItem> passingDataList, int passingCurrent, Context context, MusicListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        currentSound = passingCurrent;
        cons = context;

        MusicPlayerFragment fragment = new MusicPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MusicPlayerFragment newInstance(String passingHeader, ArrayList<MusicItem> passingDataList, int passingCurrent, Context context, MusicListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        currentSound = passingCurrent;
        cons = context;
        header = passingHeader;

        MusicPlayerFragment fragment = new MusicPlayerFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        initInstance(rootView, savedInstanceState);
        workplace(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        soundController = rootView.findViewById(R.id.soundPlayerControlView);
        soundPlayerCover = rootView.findViewById(R.id.soundPlayerCover);

        soundPlayerHeader = rootView.findViewById(R.id.soundPlayerHeader);
        soundPlayerName = rootView.findViewById(R.id.soundPlayerName);
        soundPlayerArtist = rootView.findViewById(R.id.soundPlayerArtist);
    }

    private void workplace(View rootView, Bundle savedInstanceState) {
        soundPlayerHeader.setText(header);

        if (soundPlayer == null) {
            soundPlayer = ExoPlayerFactory.newSimpleInstance(cons, new DefaultTrackSelector());
            soundController.setPlayer(soundPlayer);

            soundDataSourceFactory = new DefaultDataSourceFactory(cons, Util.getUserAgent(cons, "Sound Player"));
            if (soundConcatenatingMediaSource == null)
                soundConcatenatingMediaSource = new ConcatenatingMediaSource();

            updateDataList(dataList);
        }

        if (soundPlayer != null) {
            soundPlayer.seekTo(currentSound, 0);
            soundPlayer.setPlayWhenReady(true);
        }
        soundPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        soundController.setVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                soundController.show();
            }
        });
        soundPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (!isPlaying) {
                    stopAnimation();
                }
            }
        });

        soundPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                int newIndex = soundPlayer.getCurrentWindowIndex();
                if (newIndex != currentSound){
                    stopAnimation();
                    loadAnimation();
                    currentSound = newIndex;
                    Log.d("dodo", "onPositionDiscontinuity: " + currentSound);
                }
            }
        });

        soundPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    loadAnimation();
                }
            }
        });
    }

    private void loadAnimation() {
        soundPlayerName.setText(dataList.get(soundPlayer.getCurrentWindowIndex()).name);
        soundPlayerArtist.setText(dataList.get(soundPlayer.getCurrentWindowIndex()).artist);

        Glide.with(cons)
                .load(dataList.get(soundPlayer.getCurrentWindowIndex()).image_link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(soundPlayerCover);

        anim = new RotateAnimation(0, 360, soundPlayerCover.getPivotX(), soundPlayerCover.getPivotY());
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(15000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (soundPlayer.isPlaying()) {
                    soundPlayerCover.startAnimation(anim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        soundPlayerCover.startAnimation(anim);
        isAnimationPlaying = true;
    }

    private void stopAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAnimationPlaying) {
                    soundPlayerCover.getAnimation().cancel();
                    isAnimationPlaying = false;
                }
            }
        });
    }

    private void updateDataList(ArrayList<MusicItem> dataList) {
        for (MusicItem item : dataList) {
            soundMediaSource = new ProgressiveMediaSource.Factory(soundDataSourceFactory)
                    .createMediaSource(Uri.parse(item.link));
            soundConcatenatingMediaSource.addMediaSource(soundMediaSource);
        }
        soundPlayer.prepare(soundConcatenatingMediaSource);
    }

    @Override
    public void onStop() {
        if (soundPlayer != null) {
            soundController.setPlayer(null);
            soundPlayer.release();
            soundPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestory();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (soundPlayer != null) {
            soundController.setPlayer(null);
            soundPlayer.release();
            soundPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestory();
        super.onDestroy();
    }

    public interface MusicListener {
        void onDestory();
    }
}
