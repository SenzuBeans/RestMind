package com.alternative.cap.restmindv3.ui.narration.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;


import java.util.ArrayList;

public class NarrationSubAdapter extends RecyclerView.Adapter<NarrationSubAdapter.NarrationSubViewHolder> {

    private String[] dataList;
    private int[] coverImageTest = new int[]{R.drawable.cover1,R.drawable.cover2, R.drawable.cover3};

    public NarrationSubAdapter(String[] data) {
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
        holder.setDetail(coverImageTest[position%3], dataList[position]);
    }

    @Override
    public int getItemCount() {
        return dataList.length;
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

        public void setDetail(int imageId, String name){
            coverImage.setImageResource(imageId);
            nameNarration.setText(name);
        }
    }
}
