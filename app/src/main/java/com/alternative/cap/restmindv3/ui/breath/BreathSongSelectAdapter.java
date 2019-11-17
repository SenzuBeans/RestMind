package com.alternative.cap.restmindv3.ui.breath;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.BreathSongList;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BreathSongSelectAdapter extends RecyclerView.Adapter<BreathSongSelectAdapter.SongSelectViewHolder> {

    Context cons;
    ArrayList<MediaItem> dataList;

    public BreathSongSelectAdapter(Context cons, ArrayList<MediaItem> dataList) {
        this.cons = cons;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SongSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_select, parent, false);
        return new SongSelectViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull SongSelectViewHolder holder, int position) {
        holder.setData(dataList.get(position));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, dataList.get(position).name+ "...song selected" , Snackbar.LENGTH_SHORT).show();
                BreathSongList.current = position;
                notifyDataSetChanged();
            }
        });
        if (position == BreathSongList.current){
            holder.itemSelect();
        }else{
            holder.root.setBackgroundResource(R.drawable.btn_rounded3);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class SongSelectViewHolder extends RecyclerView.ViewHolder {

        View root;
        ImageView songImage;
        TextView songName;
        TextView songArtist;

        public SongSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            songImage = itemView.findViewById(R.id.songSelectImage);
            songName = itemView.findViewById(R.id.songSelectName);
            songArtist = itemView.findViewById(R.id.songSelectArtist);
        }

        public void setData(MediaItem item){
            Glide.with(cons)
                    .load(item.image_link_2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(songImage);

            songName.setText(item.name);
            songArtist.setText(item.artist);
        }

        public void itemSelect(){
            root.setBackgroundResource(R.drawable.btn_rounded5);
        }
    }
}
