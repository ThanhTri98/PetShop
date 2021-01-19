package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Models.NotificationsModel;
import com.example.petmarket2020.R;

import java.util.ArrayList;
import java.util.List;

public class RV_NotiAdapter extends RecyclerView.Adapter<RV_NotiAdapter.NotiHolder> {
    private final List<NotificationsModel> notificationsModels;
    private final IOnItemClick iOnItemClick;

    public RV_NotiAdapter(IOnItemClick iOnItemClick) {
        notificationsModels = new ArrayList<>();
        this.iOnItemClick = iOnItemClick;
    }

    public interface IOnItemClick {
        void itemClick(String postId, long notiId);
    }

    public void setData(List<NotificationsModel> notificationsModels) {
        this.notificationsModels.clear();
        this.notificationsModels.addAll(notificationsModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        return new NotiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiHolder holder, int position) {
        NotificationsModel notificationsModel = notificationsModels.get(position);
        holder.tvContent.setText(notificationsModel.getContent());
        holder.tvTime.setText(notificationsModel.getTime());

        if (notificationsModel.isChecked()) {
            holder.ivStatus.setImageResource(R.drawable.ic_baseline_info_24);
            holder.itemView.setBackgroundResource(R.color.colorPrimaryTransparent);
        }
        holder.itemView.setOnClickListener(v -> iOnItemClick.itemClick(notificationsModel.getPostId(), notificationsModel.getNotiId()));
    }

    @Override
    public int getItemCount() {
        return notificationsModels != null ? notificationsModels.size() : 0;
    }

    public static class NotiHolder extends RecyclerView.ViewHolder {
        private final TextView tvContent;
        private final TextView tvTime;
        private final ImageView ivStatus;

        public NotiHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }
}
