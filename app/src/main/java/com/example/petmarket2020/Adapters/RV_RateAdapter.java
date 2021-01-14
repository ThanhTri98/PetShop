package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.Models.RankingModel;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RV_RateAdapter extends RecyclerView.Adapter<RV_RateAdapter.RateHolder> {
    private final List<RankingModel> rankingModelList;
    private final List<String[]> nameAndAvatarList;
    private final StorageReference storageReference;

    public RV_RateAdapter(List<RankingModel> rankingModelList, List<String[]> nameAndAvatarList) {
        this.nameAndAvatarList = nameAndAvatarList;
        this.rankingModelList = rankingModelList;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public RateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate_and_comment, parent, false);
        return new RateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateHolder holder, int position) {
        RankingModel rankingModel = rankingModelList.get(position);
        String[] nameAndAva = nameAndAvatarList.get(position);
        String name = nameAndAva[0];
        String ava = nameAndAva[1];
        String comment = rankingModel.getComment();
        long rate = rankingModel.getRate();
        String time = rankingModel.getTime();
        holder.ratingUser.setRating(rate);
        holder.tvCmtUser.setText(comment);
        holder.tvTime.setText(time);
        holder.tvNameUser.setText(name);
        ImageView ivAva = holder.ivAvatar;
        if (ava != null)
            Glide.with(ivAva).load(storageReference.child(ava)).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivAva);
        else
            ivAva.setImageResource(R.drawable.ic_login_user);
    }

    @Override
    public int getItemCount() {
        return rankingModelList.size();
    }

    public static class RateHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        private final RatingBar ratingUser;
        private final TextView tvNameUser;
        private final TextView tvTime;
        private final TextView tvCmtUser;

        public RateHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            ratingUser = itemView.findViewById(R.id.ratingUser);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCmtUser = itemView.findViewById(R.id.tvCmtUser);
        }
    }
}
