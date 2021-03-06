package com.alternative.cap.restmindv3.ui.narration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.BreathSongList;
import com.alternative.cap.restmindv3.util.MediaItem;

import java.util.ArrayList;

public class NarrationAdapter extends RecyclerView.Adapter<NarrationAdapter.NarrationViewHolder> {


    private NarrationSubAdapter.NarrationSubListener listener;
    private Context cons;
    private ArrayList<String> headerName;
    private ArrayList<ArrayList<MediaItem>> mediaList;

    public NarrationAdapter(Context context, ArrayList<String> passingHeader, ArrayList<ArrayList<MediaItem>> passingMediaName, NarrationSubAdapter.NarrationSubListener passingListener) {
        listener = passingListener;
        this.cons = context;
        this.headerName = passingHeader;
        this.mediaList = passingMediaName;
    }


    @NonNull
    @Override
    public NarrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_narration, parent, false);

        return new NarrationViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NarrationViewHolder holder, int position) {
        holder.header.setText(headerName.get(position));
        holder.setSubAdapter(mediaList.get(position), position);
        holder.allItemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMoreClicked(mediaList.get(position), headerName.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return headerName.size() - 1;
    }

    public class NarrationViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView header;
        private RecyclerView subRecyclerView;
        private TextView allItemTv;

        public NarrationViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            header = itemView.findViewById(R.id.narrationHeaderTv);
            subRecyclerView = itemView.findViewById(R.id.narrationSubRecyclerView);
            allItemTv = itemView.findViewById(R.id.allItemTv);
        }

        public void setSubAdapter(ArrayList<MediaItem> data, int position) {
            NarrationSubAdapter subAdapter = new NarrationSubAdapter(cons, data, listener, headerName.get(position));
            subRecyclerView.setLayoutManager(
                    new LinearLayoutManager(cons, LinearLayoutManager.HORIZONTAL, false));
            subRecyclerView.setAdapter(subAdapter);
        }
    }

}
