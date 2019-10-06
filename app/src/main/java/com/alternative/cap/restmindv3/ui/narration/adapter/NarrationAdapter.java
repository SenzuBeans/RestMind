package com.alternative.cap.restmindv3.ui.narration.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MusicItem;

import java.util.ArrayList;

public class NarrationAdapter extends RecyclerView.Adapter<NarrationAdapter.NarrationViewHolder> {

    private NarrationSubAdapter.NarrationSubListener listener;
    private Context cons;
    private ArrayList<String> headerName;
    private ArrayList<ArrayList<MusicItem>> mediaList;

    public NarrationAdapter(Context context, ArrayList<String> passingHeader, ArrayList<ArrayList<MusicItem>> passingMediaName , NarrationSubAdapter.NarrationSubListener passingListener) {
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
    }

    @Override
    public int getItemCount() {
        return headerName.size();
    }

    public class NarrationViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView header;
        private RecyclerView subRecyclerView;

        public NarrationViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            header = itemView.findViewById(R.id.narrationHeaderTv);
            subRecyclerView = itemView.findViewById(R.id.narrationSubRecyclerView);
        }

        public void setSubAdapter(ArrayList<MusicItem>  data, int position){
            NarrationSubAdapter subAdapter = new NarrationSubAdapter(cons,data, listener, headerName.get(position));
            subRecyclerView.setLayoutManager(
                    new LinearLayoutManager(cons, LinearLayoutManager.HORIZONTAL, false));
            subRecyclerView.setAdapter(subAdapter);
        }
    }
}
