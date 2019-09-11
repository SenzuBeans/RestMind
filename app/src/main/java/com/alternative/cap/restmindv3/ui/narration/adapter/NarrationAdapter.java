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

import java.util.ArrayList;

public class NarrationAdapter extends RecyclerView.Adapter<NarrationAdapter.NarrationViewHolder> {

    private Context cons;
    private String[] headerName;
    private String[][] mediaName;

    public NarrationAdapter(Context context, String[] passingHeader, String[][] passingMediaName) {
        this.cons = context;
        this.headerName = passingHeader;
        this.mediaName = passingMediaName;
    }


    @NonNull
    @Override
    public NarrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_narration, parent, false);

        return new NarrationViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NarrationViewHolder holder, int position) {
        holder.header.setText(headerName[position]);
        holder.setSubAdapter(mediaName[position]);
    }

    @Override
    public int getItemCount() {
        return headerName.length;
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

        public void setSubAdapter(String[] data){
            NarrationSubAdapter subAdapter = new NarrationSubAdapter(data);
            subRecyclerView.setLayoutManager(
                    new LinearLayoutManager(cons, LinearLayoutManager.HORIZONTAL, false));
            subRecyclerView.setAdapter(subAdapter);
        }
    }
}
