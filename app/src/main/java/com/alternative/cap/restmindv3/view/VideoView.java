package com.alternative.cap.restmindv3.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.alternative.cap.restmindv3.R;

public class VideoView extends FrameLayout {

    private final String TAG = "dodo";

    private android.widget.VideoView backgroundVideo;
    private boolean isPlay = false;
    private boolean isStart = true;

    public VideoView(Context context) {
        super(context);
        init();
        workbench();
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        workbench();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        workbench();
    }

    @TargetApi(29)
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        workbench();
    }

    private void init() {
        inflate(getContext(), R.layout.page_video, this);
    }

    private void workbench() {
        backgroundVideo = findViewById(R.id.backgroundPageVideoView);
    }

    public void setUri(String link) {
        Log.d(TAG, "setUri: "+ link);
        backgroundVideo.setVideoPath(link);
        backgroundVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                backgroundVideo.start();
            }
        });
        backgroundVideo.start();
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

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        BundleSavedState savedState = new BundleSavedState(superState);
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
