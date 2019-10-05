package com.alternative.cap.restmindv3.ui.music;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private SimpleExoPlayer soundPlayer;
    private MediaSource soundMediaSource;
    private ConcatenatingMediaSource soundConcatenatingMediaSource;
    private DefaultDataSourceFactory soundDataSourceFactory;
    private PlayerControlView soundController;

    private static MusicListener listener;
    private static ArrayList<MusicItem> dataList;
    private static int CURRENT_SOUND = 0;
    private static Context cons;

    public MusicPlayerFragment() {
    }

    public static MusicPlayerFragment newInstance(ArrayList<MusicItem> passingDataList, int passingCurrent, Context context, MusicListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        CURRENT_SOUND = passingCurrent;
        cons = context;

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
    }

    private void workplace(View rootView, Bundle savedInstanceState) {
        if (soundPlayer == null) {
            soundPlayer = ExoPlayerFactory.newSimpleInstance(cons, new DefaultTrackSelector());
            soundController.setPlayer(soundPlayer);

            soundDataSourceFactory = new DefaultDataSourceFactory(cons, Util.getUserAgent(cons, "Sound Player"));
            if (soundConcatenatingMediaSource == null)
                soundConcatenatingMediaSource = new ConcatenatingMediaSource();

            updateDataList(dataList);
        }

        if (soundPlayer != null){
            soundPlayer.seekTo(CURRENT_SOUND, 0);
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
                if (isPlaying){
                    Glide.with(cons)
                            .load(dataList.get(soundPlayer.getCurrentWindowIndex()).image_link)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(soundPlayerCover);
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
        getFragmentManager().popBackStack();
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
        getFragmentManager().popBackStack();
        listener.onDestory();
        super.onDestroy();
    }

    public interface MusicListener{
        void onDestory();
    }
}
