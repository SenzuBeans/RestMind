package com.alternative.cap.restmindv3.manager.adapter;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.view.FeatureView;


public class FeatureFragmentAdapter extends RecyclerView.Adapter<FeatureFragmentAdapter.FeatureFragmentViewHolder> {

    private String[] nameFeatureList;
    private String[] descriptionFeatureList;
    private int[] imageFeatureList;
    private FragmentActivity mActivity;
    private float touchingPoint = 0;
    private boolean clicked;

    public FeatureFragmentAdapter(FragmentActivity activity) {
        nameFeatureList = new String[]{"Breathing", "Narration", "Media Player", "More Feature"};
        descriptionFeatureList = new String[]{"breathing info", "narration info", "media player info", ""};
        imageFeatureList = new int[]{R.drawable.feature_image_breathe_f, R.drawable.feature_image_narration_f, R.drawable.feature_image_sound_f, R.drawable.feature_image_more_f};
        mActivity = activity;
    }

    public FeatureFragmentAdapter(FragmentActivity activity, String[] nameFeatureList, String[] descriptionFeatureList) {
        this.nameFeatureList = nameFeatureList;
        this.descriptionFeatureList = descriptionFeatureList;
        mActivity = activity;
    }

    @Override
    public FeatureFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = new FeatureView(parent.getContext());
        return new FeatureFragmentViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(FeatureFragmentViewHolder holder, final int position) {

        holder.getFeatureView().setFeatureImageView(imageFeatureList[position]);
        holder.getFeatureView().setNameFeatureItemTextView(nameFeatureList[position]);
        holder.getFeatureView().setDescriptionFeatureItemTextView(descriptionFeatureList[position]);
        holder.getRelateLayout().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchingPoint = motionEvent.getY();
                        clicked = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getY() < touchingPoint && Math.abs(motionEvent.getY() - touchingPoint) > 23f) {
                            //TODO : add activity move up here;
                            touchingPoint = motionEvent.getY();
                            clicked = false;
                        } else if (motionEvent.getY() > touchingPoint && Math.abs(motionEvent.getY() - touchingPoint) > 23f) {
                            touchingPoint = motionEvent.getY();
                            clicked = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (clicked) {
                            FeatureListener listener = (FeatureListener) mActivity;
                            listener.onFeatureClick(position);
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameFeatureList.length;
    }

    public class FeatureFragmentViewHolder extends RecyclerView.ViewHolder {
        private FeatureView featureView;
        private RelativeLayout relateLayout;

        public FeatureFragmentViewHolder(View itemView) {
            super(itemView);
            featureView = (FeatureView) itemView;
            relateLayout = itemView.findViewById(R.id.relateLayout);
        }

        public FeatureView getFeatureView() {
            return featureView;
        }

        public RelativeLayout getRelateLayout() {
            return relateLayout;
        }
    }

    public interface FeatureListener {
        void onFeatureClick(int featurePosition);
    }
}
