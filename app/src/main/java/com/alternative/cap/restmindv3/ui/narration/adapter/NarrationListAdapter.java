package com.alternative.cap.restmindv3.ui.narration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class NarrationListAdapter extends RecyclerView.Adapter<NarrationListAdapter.NarrationListVireHolder> {

    NarrationSubAdapter.NarrationSubListener listener;
    private Context cons;
    private ArrayList<MediaItem> dataList;
    private String header;

    public NarrationListAdapter(Context context, ArrayList<MediaItem> dataList,String passingHeader , NarrationSubAdapter.NarrationSubListener passingListener) {
        this.cons = context;
        this.dataList = dataList;
        this.header = passingHeader;
        listener = passingListener;
    }

    @NonNull
    @Override
    public NarrationListVireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_narration_list, parent, false);

        return new NarrationListVireHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NarrationListVireHolder holder, int position) {
        holder.setData(dataList.get(position));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(dataList, position, header);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class NarrationListVireHolder extends RecyclerView.ViewHolder {
        View root;
        CircularImageView imageView;
        TextView listItemName;
        TextView listItemArtist;
        RatingBar listItemRatingBar;

        public NarrationListVireHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            imageView = itemView.findViewById(R.id.narrationListItemCover);
            listItemName = itemView.findViewById(R.id.narrationListItemName);
            listItemArtist =  itemView.findViewById(R.id.narrationListItemArtist);
            listItemRatingBar = itemView.findViewById(R.id.narrationListItemRatingBar);
        }


        public void setData(MediaItem item) {
            Glide.with(cons)
                    .load(item.image_link_2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            listItemName.setText(item.name);
            listItemArtist.setText(item.artist);
            listItemRatingBar.setRating(item.rating_score / item.rating_count);
        }
    }
}
