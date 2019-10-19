package com.alternative.cap.restmindv3.ui.background;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.view.VideoView;

import java.util.ArrayList;


public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder> {

    private ArrayList<String> listLink;
    private ArrayList<String> listId;

    public BackgroundAdapter(ArrayList<String> passingListLink, ArrayList<String> passingListId) {

        this.listLink = passingListLink;
        this.listId = passingListId;
    }

    @Override
    public BackgroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = new VideoView(parent.getContext());
        return new BackgroundViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BackgroundViewHolder holder, final int position) {
        holder.getVideoView().setUri(listLink.get(position));
        holder.getVideoView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.getVideoView().focusPlayer();
            }
        });
        holder.id.setText(listId.get(position));
    }

    @Override
    public int getItemCount() {
        return listLink.size();
    }

    public class BackgroundViewHolder extends RecyclerView.ViewHolder {
        private VideoView videoView;
        private TextView id;

        public BackgroundViewHolder(View itemView) {
            super(itemView);
            videoView = (VideoView) itemView;
            id = itemView.findViewById(R.id.backgroundPageID);
        }

        public VideoView getVideoView() {
            return videoView;
        }
    }

    public interface BackgroundListener {
        void onBackgroundClicked(int backgroundPosition);
    }
}
