package com.alternative.cap.restmindv3.ui.narration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MusicItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;

public class NarrationSubAdapter extends RecyclerView.Adapter<NarrationSubAdapter.NarrationSubViewHolder> {

    private Context cons;
    private ArrayList<MusicItem>  dataList;

    public NarrationSubAdapter(Context context, ArrayList<MusicItem> data) {
        cons = context;
        this.dataList = data;
    }

    @NonNull
    @Override
    public NarrationSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_narration_sub, parent, false);
        return new NarrationSubViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NarrationSubViewHolder holder, int position) {
        holder.setDetail(dataList.get(position).image_link, dataList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class NarrationSubViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private ImageView coverImage;
        private TextView nameNarration;
        private TextView artistMedia;

        public NarrationSubViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            coverImage = itemView.findViewById(R.id.narrationMediaCoverImage);
            nameNarration = itemView.findViewById(R.id.narrationMediaName);
            artistMedia = itemView.findViewById(R.id.narrationMediaArtist);
        }

        public void setDetail(String imageLink, String name){
            Glide.with(cons)
                    .load(imageLink)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(coverImage);

            nameNarration.setText(name);
        }
    }
}
