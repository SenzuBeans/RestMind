package com.alternative.cap.restmindv3.ui.step;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.MediaListViewHolder> {

    Context cons;
    StepListAdapterListener listener;
    private ArrayList<ArrayList<MediaItem>> dataList;
    private ArrayList<String[]> headerList;

    public StepListAdapter(StepListFragment passingListener, ArrayList<ArrayList<MediaItem>>  passingDataList, ArrayList<String[]>  passingHeaderList, Context context) {
        cons = context;
        this.listener = passingListener;
        this.dataList = passingDataList;
        this.headerList = passingHeaderList;
    }

    @Override
    public StepListAdapter.MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step_list, parent, false);
        return new MediaListViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListAdapter.MediaListViewHolder holder, int position) {
        holder.stepName.setText(headerList.get(position)[0]);
        holder.stepArtist.setText(headerList.get(position)[1]);
        holder.setMediaCover(headerList.get(position)[2]);
        holder.stepItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(headerList.get(position)[0], dataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MediaListViewHolder extends RecyclerView.ViewHolder {

        private View stepItem;
        private CircularImageView stepListCover;
        private TextView stepName;
        private TextView stepArtist;

        public MediaListViewHolder(@NonNull View itemView) {
            super(itemView);
            stepItem = itemView;
            stepListCover = stepItem.findViewById(R.id.musicCover);
            stepName = stepItem.findViewById(R.id.stepName);
            stepArtist = stepItem.findViewById(R.id.stepArtist);
        }

        public void setMediaCover(String uri) {
            Glide.with(cons)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(stepListCover);
        }
    }

    public interface StepListAdapterListener {
        void onItemClicked(String passingHeader, ArrayList<MediaItem> passingDataList);
    }
}
