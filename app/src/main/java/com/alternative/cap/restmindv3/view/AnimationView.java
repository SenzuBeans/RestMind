package com.alternative.cap.restmindv3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alternative.cap.restmindv3.R;

public class AnimationView extends LinearLayout {
    public AnimationView(Context context) {
        super( context );
        initInstance();
        workbench();
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        initInstance();
        workbench();
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initInstance();
        workbench();
    }

    private void initInstance() {
        inflate( getContext(), R.layout.view_animation, this);
    }

    private void workbench() {

    }
}
