package com.alternative.cap.restmindv3.ui.setting.sub_setting.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.UserDetails;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private ArrayList<UserDetails> userList;

    public RankingAdapter(ArrayList<UserDetails> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        holder.setRanking(userList.get(position));
    }

    @Override
    public int getItemCount() {
        if (userList.size() > 20){
            return 20;
        }else{
            return userList.size();
        }
    }

    public class RankingViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView rankingImage;
        TextView rankingName;
        TextView rankingEmail;
        TextView rankingTime;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            rankingImage = itemView.findViewById(R.id.rankingItemImageView);
            rankingName = itemView.findViewById(R.id.rankingItemUserName);
            rankingEmail = itemView.findViewById(R.id.rankingItemUserEmail);
            rankingTime = itemView.findViewById(R.id.rankingItemUserTime);
        }

        public void setRanking(UserDetails userDetails){
            if (userDetails.totalTime != null) {
                if (Integer.parseInt(userDetails.totalTime) > 50) {
                    rankingImage.setImageResource(R.drawable.ic_rank_dimond_class);
                } else {
                    rankingImage.setImageResource(R.drawable.ic_rank_gold_class);
                }
            }else{
                rankingImage.setImageResource(R.drawable.ic_music);
            }

            if (userDetails.name == null){
                rankingName.setText("VISITOR");
            }else{
                rankingName.setText(userDetails.name);
            }

            if (userDetails.email == null){
                rankingEmail.setText("VISITOR");
            }else{
                rankingEmail.setText(userDetails.email);
            }

            if (userDetails.totalTime == null){
                rankingTime.setText("0");
            }else{
                rankingTime.setText(userDetails.totalTime);
            }
        }
    }
}
