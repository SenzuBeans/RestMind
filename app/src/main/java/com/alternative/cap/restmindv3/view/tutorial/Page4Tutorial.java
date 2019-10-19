package com.alternative.cap.restmindv3.view.tutorial;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.MemberActivity;
import com.alternative.cap.restmindv3.view.BundleSavedState;

public class Page4Tutorial extends FrameLayout {

    public Page4Tutorial(Context context) {
        super(context);
        init();
        initInstance();
    }

    public Page4Tutorial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initInstance();
    }

    public Page4Tutorial(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initInstance();
    }

    @TargetApi(29)
    public Page4Tutorial(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initInstance();
    }

    private Button tutorialLoginBtn;

    private void init(){
        inflate(getContext(), R.layout.page_tutorial_4 , this);
    }

    private void initInstance(){
        tutorialLoginBtn = findViewById(R.id.tutorialLoginBtn);


        workbench();
    }

    private void workbench() {
        tutorialLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), MemberActivity.class));
                Page4BtnClickListener listener = (Page4BtnClickListener) getContext();
                listener.onLoginBtnClicked();
            }
        });
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
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    public interface Page4BtnClickListener {
        void onLoginBtnClicked();
    }
}

