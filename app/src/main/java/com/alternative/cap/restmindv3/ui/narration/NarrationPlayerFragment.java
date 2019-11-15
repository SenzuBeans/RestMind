package com.alternative.cap.restmindv3.ui.narration;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
    private RatingBar narrationRatingBar;
    private FrameLayout narrationRatingContent;
    private RelativeLayout narrationMainContent;

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

        narrationRatingBar = rootView.findViewById(R.id.narrationRatingBar);
        narrationRatingContent = rootView.findViewById(R.id.narrationRatingContent);
        narrationMainContent = rootView.findViewById(R.id.narrationMainContent);
    }

    private void workplace(View rootView, Bundle savedInstanceState) {
        setPlayer();

        narrationRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        swapContent(true);
                        break;
                }
                return true;
            }
        });
    }

    private void swapContent(boolean b) {
        if (b) {
            narrationRatingContent.setVisibility(View.VISIBLE);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.narrationRatingContent, RatingFragment.newInstance(new RatingFragment.RatingListener() {
                        @Override
                        public void onCancel() {
                            swapContent(false);
                        }
                    }, dataList.get(narrationPlayer.getCurrentWindowIndex())))
                    .addToBackStack(null)
                    .commit();
        } else {
            narrationRatingContent.setVisibility(View.GONE);
            narrationRatingBar.setRating(setRating(dataList.get(narrationPlayer.getCurrentWindowIndex())));
        }
    }

    private void setPlayer() {
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
                if (newIndex != currentSound) {
                    stopAnimation();
                    loadAnimation();
                    currentSound = newIndex;
//                    Log.d("dodo", "onPositionDiscontinuity: " + currentSound);
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

    private void updateDataList(ArrayList<MediaItem> dataList) {
        for (MediaItem item : dataList) {
            narrationMediaSource = new ProgressiveMediaSource.Factory(narrationDataSourceFactory)
                    .createMediaSource(Uri.parse(item.link_2));
            narrationConcatenatingMediaSource.addMediaSource(narrationMediaSource);
        }
        narrationPlayer.prepare(narrationConcatenatingMediaSource);
    }

    private void loadAnimation() {
        narrationPlayerName.setText(dataList.get(narrationPlayer.getCurrentWindowIndex()).name);
        narrationPlayerArtist.setText(dataList.get(narrationPlayer.getCurrentWindowIndex()).artist);
        narrationRatingBar.setRating(setRating(dataList.get(narrationPlayer.getCurrentWindowIndex())));

        Glide.with(cons)
                .load(dataList.get(narrationPlayer.getCurrentWindowIndex()).image_link_2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(narrationPlayerCover);

        anim = new RotateAnimation(0, 360, narrationPlayerCover.getPivotX() + 1, narrationPlayerCover.getPivotY() + 1);
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

    private float setRating(MediaItem item) {
        if (item.rating_score == 0) {
            return 0;
        } else {
            return item.rating_score / item.rating_count;
        }
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
