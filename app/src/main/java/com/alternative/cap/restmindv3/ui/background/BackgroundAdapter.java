package com.alternative.cap.restmindv3.ui.background;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder> {

    Context cons;
    private ArrayList<MediaItem> mediaList;
    private int playingMedia;
    private BackgroundAdapterListener listener;
    private int path = 0;

    public BackgroundAdapter(Context cons, ArrayList<MediaItem> mediaList, int playingMedia, BackgroundAdapterListener listener, int i) {
        this.cons = cons;
        this.mediaList = mediaList;
        this.playingMedia = playingMedia;
        this.listener = listener;
        this.path = i;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bg_soung, parent, false);
        return new BackgroundViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BackgroundViewHolder holder, final int position) {
        holder.setData(mediaList.get(position));
        if (position == playingMedia){
            holder.setSelect();
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void setPlayingMedia(int playingMedia) {
        this.playingMedia = playingMedia;
    }

    public class BackgroundViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private CircularImageView bgBackImage;
        private ImageView bgFaceImage;
        private TextView name;

        public BackgroundViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            bgBackImage = itemView.findViewById(R.id.bgBackImage);
            bgFaceImage = itemView.findViewById(R.id.bgFaceImage);
            name = itemView.findViewById(R.id.bgName);
        }

        public void setData(MediaItem item) {
            Glide.with(cons)
                    .load(item.image_link_2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bgFaceImage);

            name.setText(item.name);
            bgBackImage.setBackgroundResource(R.drawable.btn_rounded2);
        }

        public void setSelect(){
            bgBackImage.setBackgroundResource(R.drawable.btn_rounded);
        }
    }


    public interface BackgroundAdapterListener{
        void onItemSelect(int itemSelect, int path);
    }

}
