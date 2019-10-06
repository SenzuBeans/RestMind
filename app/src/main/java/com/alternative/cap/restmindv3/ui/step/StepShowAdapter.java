package com.alternative.cap.restmindv3.ui.step;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class StepShowAdapter extends RecyclerView.Adapter<StepShowAdapter.StepViewHolder> {

    ArrayList<MediaItem> dataList;
    int current;
    Context cons;

    public StepShowAdapter(ArrayList<MediaItem> passingDataList, int passingCurrent, Context context) {
        dataList = passingDataList;
        current = passingCurrent;
        cons = context;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step_show, parent,false);
        return new StepViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.stepName.setText(dataList.get(position).name);
        holder.stepArtist.setText(dataList.get(position).artist);
        if (position <= current){
//            holder.root.setBackground(ContextCompat.getDrawable(cons, R.drawable.background_step));
            holder.stepName.setTextColor(Color.BLACK);
            holder.stepArtist.setTextColor(Color.BLACK);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "This step are allow to listen", Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "This step are not allow to listen", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        private View root;
        private TextView stepName;
        private TextView stepArtist;


        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            stepName = itemView.findViewById(R.id.stepName);
            stepArtist = itemView.findViewById(R.id.stepArtist);
        }
    }
}
