package com.alternative.cap.restmindv3.ui.background;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alternative.cap.restmindv3.R;

public class BackgroundFragment extends Fragment {

    private VideoView backgroundVideo;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_background, container, false);
        workbench(root,savedInstanceState);
        return root;
    }

    private void workbench(View root, Bundle savedInstanceState) {
        backgroundVideo = root.findViewById(R.id.backgroundVideoView);
        backgroundVideo.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.relax_video));

        backgroundVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                backgroundVideo.start();
            }
        });
        backgroundVideo.start();
    }

    @Override
    public void onResume() {
        if (backgroundVideo != null){
            backgroundVideo.start();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (backgroundVideo != null){
            backgroundVideo.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (backgroundVideo != null){
            backgroundVideo.pause();
        }
        super.onDestroy();
    }
}