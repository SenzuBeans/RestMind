package com.alternative.cap.restmindv3.ui.narration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;

public class NarrationSubAdapter extends RecyclerView.Adapter<NarrationSubAdapter.NarrationSubViewHolder> {

    private static NarrationSubListener listener;
    private Context cons;
    private ArrayList<MediaItem> dataList;
    private String header;

    public NarrationSubAdapter(Context context, ArrayList<MediaItem> data, NarrationSubListener passingListener, String passingHeader) {
        listener = passingListener;
        cons = context;
        this.dataList = data;
        this.header = passingHeader;
    }

    @NonNull
    @Override
    public NarrationSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_narration_sub, parent, false);
        return new NarrationSubViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NarrationSubViewHolder holder, int position) {
        holder.swap(true);
        holder.setDetail(dataList.get(position).image_link_2, dataList.get(position).name);
        holder.artistMedia.setText(dataList.get(position).artist);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(dataList, position, header);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList.size() > 15) {
            return 15;
        } else {
            return dataList.size();
        }
    }

    public class NarrationSubViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private ImageView coverImage;
        private TextView nameNarration;
        private TextView artistMedia;
        private LinearLayout mediaLayout;
        private LinearLayout moreLayout;

        public NarrationSubViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            coverImage = itemView.findViewById(R.id.narrationMediaCoverImage);
            nameNarration = itemView.findViewById(R.id.narrationMediaName);
            artistMedia = itemView.findViewById(R.id.narrationMediaArtist);
            mediaLayout = itemView.findViewById(R.id.narrationSubMediaLayout);
            moreLayout = itemView.findViewById(R.id.narrationSubMoreLayout);
        }

        public void setDetail(String imageLink, String name) {
            Glide.with(cons)
                    .load(imageLink)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                    .into(coverImage);

            nameNarration.setText(name);
        }

        public void swap(boolean b) {
            if (b) {
                mediaLayout.setVisibility(View.VISIBLE);
                moreLayout.setVisibility(View.GONE);
            } else {
                moreLayout.setVisibility(View.VISIBLE);
                mediaLayout.setVisibility(View.GONE);
            }
        }
    }

    public interface NarrationSubListener {
        void onItemClicked(ArrayList<MediaItem> passingDataList, int current, String passingHeader);

        void onMoreClicked(ArrayList<MediaItem> passingDataList, String passingHeader);
    }
}
