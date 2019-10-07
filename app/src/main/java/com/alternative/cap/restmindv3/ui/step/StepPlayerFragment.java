package com.alternative.cap.restmindv3.ui.step;

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

public class StepPlayerFragment extends Fragment {

    private CircularImageView stepPlayerCover;
    private Animation anim;
    private SimpleExoPlayer stepPlayer;
    private MediaSource stepMediaSource;
    private ConcatenatingMediaSource stepConcatenatingMediaSource;
    private DefaultDataSourceFactory stepDataSourceFactory;
    private PlayerControlView stepController;

    private TextView stepPlayerName;
    private TextView stepPlayerArtist;

    private static StepListener listener;

    private static ArrayList<MediaItem> dataList;
    private static String header;
    private static Context cons;

    private boolean isAnimationPlaying = false;

    public StepPlayerFragment() {
    }

    public static StepPlayerFragment newInstance(String passingHeader, ArrayList<MediaItem> passingDataList, Context context, StepListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        header = passingHeader;
        cons = context;

        StepPlayerFragment fragment = new StepPlayerFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_step_player, container, false);
        initInstance(rootView, savedInstanceState);
        workplace(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        stepController = rootView.findViewById(R.id.stepPlayerControlView);
        stepPlayerCover = rootView.findViewById(R.id.stepPlayerCover);

        stepPlayerName = rootView.findViewById(R.id.stepPlayerName);
        stepPlayerArtist = rootView.findViewById(R.id.stepPlayerArtist);
    }

    private void workplace(View rootView, Bundle savedInstanceState) {

        if (stepPlayer == null) {
            stepPlayer = ExoPlayerFactory.newSimpleInstance(cons, new DefaultTrackSelector());
            stepController.setPlayer(stepPlayer);

            stepDataSourceFactory = new DefaultDataSourceFactory(cons, Util.getUserAgent(cons, "Sound Player"));
            if (stepConcatenatingMediaSource == null)
                stepConcatenatingMediaSource = new ConcatenatingMediaSource();

            updateDataList(dataList);
        }

        if (stepPlayer != null) {
//            stepPlayer.seekTo(currentSound, 0);
            stepPlayer.setPlayWhenReady(true);
        }
        stepPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        stepController.setVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                stepController.show();
            }
        });
        stepPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (!isPlaying) {
                    stopAnimation();
                }
            }
        });

        stepPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                int newIndex = stepPlayer.getCurrentWindowIndex();
//                if (newIndex != currentSound){
                    stopAnimation();
                    loadAnimation();
//                    currentSound = newIndex;
//                    Log.d("dodo", "onPositionDiscontinuity: " + currentSound);
//                }
            }
        });

        stepPlayer.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    loadAnimation();
                }
            }
        });
    }

    private void loadAnimation() {
        stepPlayerName.setText(dataList.get(stepPlayer.getCurrentWindowIndex()).name);
        stepPlayerArtist.setText(dataList.get(stepPlayer.getCurrentWindowIndex()).artist);

        Glide.with(cons)
                .load(dataList.get(stepPlayer.getCurrentWindowIndex()).image_link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(stepPlayerCover);

        anim = new RotateAnimation(0, 360, stepPlayerCover.getPivotX(), stepPlayerCover.getPivotY());
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(15000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (stepPlayer.isPlaying()) {
                    stepPlayerCover.startAnimation(anim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        stepPlayerCover.startAnimation(anim);
        isAnimationPlaying = true;
    }

    private void stopAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAnimationPlaying) {
                    stepPlayerCover.getAnimation().cancel();
                    isAnimationPlaying = false;
                }
            }
        });
    }

    private void updateDataList(ArrayList<MediaItem> dataList) {
        for (MediaItem item : dataList) {
            stepMediaSource = new ProgressiveMediaSource.Factory(stepDataSourceFactory)
                    .createMediaSource(Uri.parse(item.link));
            stepConcatenatingMediaSource.addMediaSource(stepMediaSource);
        }
        stepPlayer.prepare(stepConcatenatingMediaSource);
    }

    @Override
    public void onStop() {
        if (stepPlayer != null) {
            stepController.setPlayer(null);
            stepPlayer.release();
            stepPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestory();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (stepPlayer != null) {
            stepController.setPlayer(null);
            stepPlayer.release();
            stepPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestory();
        super.onDestroy();
    }

    public interface StepListener {
        void onDestory();
    }
}
