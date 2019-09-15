package com.alternative.cap.restmindv3.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MediaListViewHolder> {

    MediaListAdapterListener listener;
    private ArrayList<String> nameMediaList;
    private ArrayList<String> artistMediaList;
    private ArrayList<String> uriMediaList;

    public MusicAdapter(MediaListAdapterListener passingListener, ArrayList<String> nameMediaList, ArrayList<String> artistMediaList, ArrayList<String> uriMediaList) {
        this.listener = passingListener;

        this.nameMediaList = nameMediaList;
        this.artistMediaList = artistMediaList;
        this.uriMediaList = uriMediaList;
    }

    @Override
    public MusicAdapter.MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MediaListViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MediaListViewHolder holder, int position) {
       if (position < nameMediaList.size()) {
           holder.nameMediaListTv.setText(nameMediaList.get(position));
           holder.nameArtistMediaListTv.setText(artistMediaList.get(position));
           holder.mediaItem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    listener.onItemClicked(nameMediaList.get(position),artistMediaList.get(position),uriMediaList.get(position));
               }
           });
       }else{
           holder.mediaItem.setVisibility(View.GONE);
       }
    }

    @Override
    public int getItemCount() {
        return nameMediaList.size()+1;
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
    }

    public interface MediaListAdapterListener{
        void onItemClicked(String passName, String passArtist, String passUri);
    }
}
