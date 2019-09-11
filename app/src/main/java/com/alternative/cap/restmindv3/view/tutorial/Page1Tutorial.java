package com.alternative.cap.restmindv3.view.tutorial;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.FrameLayout;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.view.BundleSavedState;

public class Page1Tutorial extends FrameLayout {

    public Page1Tutorial(Context context) {
        super(context);
        init();
        initInstance();
    }

    public Page1Tutorial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initInstance();
    }

    public Page1Tutorial(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initInstance();
    }

    @TargetApi(29)
    public Page1Tutorial(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initInstance();
    }

    private void init(){
        inflate(getContext(), R.layout.page_tutorial_1 , this);
    }

    private void initInstance(){

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

}

