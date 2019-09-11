package com.alternative.cap.restmindv3.view;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MutableValue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FeatureView extends FrameLayout {

    private final String TAG = "dodo";

    private ImageView featureImageView;
    private TextView nameFeatureItemTextView;
    private TextView descriptionFeatureItemTextView;
    private MutableValue heightRecyclerViewValue;

    public FeatureView(Context context) {
        super(context);
        init();
        workbench();
    }

    public FeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        workbench();
    }

    public FeatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        workbench();
    }

    @TargetApi(29)
    public FeatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        workbench();
    }

    private void init() {
        inflate(getContext(), R.layout.item_feature, this);
    }

    private void workbench() {
        featureImageView = findViewById(R.id.featureItemImage);
        nameFeatureItemTextView = findViewById(R.id.nameFeatureItemTextView);
        descriptionFeatureItemTextView = findViewById(R.id.descriptionFeatureItemTextView);

        heightRecyclerViewValue = new MutableValue();
        Log.d(TAG, "workSpace: 2 :" + (heightRecyclerViewValue.getHeightRecyclerViewValue() - ((int)heightRecyclerViewValue.convertDpToPixel(104, getContext()))) / 4);

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
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (heightRecyclerViewValue.getHeightRecyclerViewValue() - ((int)heightRecyclerViewValue.convertDpToPixel(50, getContext()))) / 4;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                height,
                MeasureSpec.EXACTLY
        );

        // Child Views
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    public void setFeatureImageView(int resId) {
        Glide.with(getContext())
                .load(resId)
                .thumbnail(Glide.with(getContext()).load(R.drawable.image_loading_bg))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(featureImageView);
    }

    public void setNameFeatureItemTextView(String nameFeature) {
        this.nameFeatureItemTextView.setText(nameFeature);
    }

    public void setDescriptionFeatureItemTextView(String descriptionFeature) {
        this.descriptionFeatureItemTextView.setText(descriptionFeature);
    }
}
