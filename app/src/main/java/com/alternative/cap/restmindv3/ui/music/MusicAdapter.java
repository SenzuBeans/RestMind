package com.alternative.cap.restmindv3.ui.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MusicItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MediaListViewHolder> {

    Context cons;
    MediaListAdapterListener listener;
    private ArrayList<MusicItem> dataList;

    public MusicAdapter(MediaListAdapterListener passingListener, ArrayList<MusicItem> passingDataList, Context context) {
        cons = context;
        this.listener = passingListener;
        this.dataList = passingDataList;
    }

    @Override
    public MusicAdapter.MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MediaListViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MediaListViewHolder holder, int position) {
        holder.nameMediaListTv.setText(dataList.get(position).name);
        holder.nameArtistMediaListTv.setText(dataList.get(position).artist);
        holder.setMediaCover(dataList.get(position).image_link);
        holder.mediaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(dataList, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MediaListViewHolder extends RecyclerView.ViewHolder {

        private View mediaItem;
        private CircularImageView mediaListCover;
        private TextView nameMediaListTv;
        private TextView nameArtistMediaListTv;

        public MediaListViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaItem = itemView;
            mediaListCover = mediaItem.findViewById(R.id.musicCover);
            nameMediaListTv = mediaItem.findViewById(R.id.nameMusicTv);
            nameArtistMediaListTv = mediaItem.findViewById(R.id.nameArtistMusicTv);
        }

        public void setMediaCover(String uri) {
            Glide.with(cons)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mediaListCover);
        }
    }

    public interface MediaListAdapterListener {
        void onItemClicked(ArrayList<MusicItem> passingDataList, int current);
    }
}
