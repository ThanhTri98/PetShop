package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RV_PostManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final IOnItemClick iOnItemClick;
    private List<PostModel> postModelList;
    private final int VIEW_TYPE;
    private final StorageReference storageReference;

    public static final int FLASH = 0;
    public static final int EDIT = 1;
    public static final int ITEM_CLICK = 5;
    public static final int HIDDEN = 2;
    public static final int UNHIDDEN = 3;

    public RV_PostManageAdapter(int VIEW_TYPE, IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
        this.VIEW_TYPE = VIEW_TYPE;
        storageReference = FirebaseStorage.getInstance().getReference();
        postModelList = new ArrayList<>();
    }

    public void setOrUpdateData(List<PostModel> postModelList) {
        this.postModelList = postModelList;
        notifyDataSetChanged();
    }

    public interface IOnItemClick {
        void onClick(PostModel postModel, int type); //Edit or ViewDetail
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_manage_selling, parent, false);
            return new SellingHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_manage_hidden, parent, false);
            return new HiddenHolder(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_manage_refused, parent, false);
            return new RefusedHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_manage_waiting, parent, false);
            return new WaitingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostModel postModel = postModelList.get(position);
        String postId = postModel.getPostId();
        int sizeImage = postModel.getImages().size();
        if (holder.getItemViewType() == 0) {
            SellingHolder sellingHolder = (SellingHolder) holder;
            sellingHolder.tvTitle.setText(postModel.getTitle());
            sellingHolder.tvPrice.setText(Utils.formatCurrencyVN(postModel.getPrice()));
            sellingHolder.tvTime.setText(postModel.getTimeStart());
            sellingHolder.tvImgCount.setText(String.valueOf(sizeImage));
            ImageView ivImage = sellingHolder.ivImage;
            Glide.with(ivImage).load(storageReference.child(postModel.getImages()
                    .get(new Random().nextInt(sizeImage)))).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            sellingHolder.btnFlash.setOnClickListener(v -> iOnItemClick.onClick(postModel, FLASH));
            sellingHolder.btnEdit.setOnClickListener(v -> iOnItemClick.onClick(postModel, EDIT));
            sellingHolder.btnHidden.setOnClickListener(v -> iOnItemClick.onClick(postModel, HIDDEN));
            sellingHolder.itemView.setOnClickListener(v -> iOnItemClick.onClick(postModel, ITEM_CLICK));
        } else if (holder.getItemViewType() == 1) {
            HiddenHolder hiddenHolder = (HiddenHolder) holder;
            hiddenHolder.tvTitle.setText(postModel.getTitle());
            hiddenHolder.tvPrice.setText(Utils.formatCurrencyVN(postModel.getPrice()));
            hiddenHolder.tvImgCount.setText(String.valueOf(sizeImage));
            ImageView ivImage = hiddenHolder.ivImage;
            Glide.with(ivImage).load(storageReference.child(postModel.getImages()
                    .get(new Random().nextInt(sizeImage)))).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            hiddenHolder.btnHidden.setOnClickListener(v -> iOnItemClick.onClick(postModel, UNHIDDEN));
            hiddenHolder.itemView.setOnClickListener(v -> iOnItemClick.onClick(postModel, ITEM_CLICK));
        } else if (holder.getItemViewType() == 2) {
            RefusedHolder refusedHolder = (RefusedHolder) holder;
            refusedHolder.tvTitle.setText(postModel.getTitle());
            refusedHolder.tvPrice.setText(Utils.formatCurrencyVN(postModel.getPrice()));
            refusedHolder.tvImgCount.setText(String.valueOf(sizeImage));
            ImageView ivImage = refusedHolder.ivImage;
            Glide.with(ivImage).load(storageReference.child(postModel.getImages()
                    .get(new Random().nextInt(sizeImage)))).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            refusedHolder.btnEdit.setOnClickListener(v -> iOnItemClick.onClick(postModel, EDIT));
        } else {
            WaitingHolder waitingHolder = (WaitingHolder) holder;
            waitingHolder.tvTitle.setText(postModel.getTitle());
            waitingHolder.tvPrice.setText(Utils.formatCurrencyVN(postModel.getPrice()));
            waitingHolder.tvImgCount.setText(String.valueOf(sizeImage));
            ImageView ivImage = waitingHolder.ivImage;
            Glide.with(ivImage).load(storageReference.child(postModel.getImages()
                    .get(new Random().nextInt(sizeImage)))).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return postModelList != null ? postModelList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    private static class SellingHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvTitle;
        private final TextView tvPrice;
        private final TextView tvTime;
        private final TextView tvImgCount;
        private final Button btnFlash;
        private final Button btnEdit;
        private final Button btnHidden;

        public SellingHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvImgCount = itemView.findViewById(R.id.tvImgCount);
            btnFlash = itemView.findViewById(R.id.btnFlash);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHidden = itemView.findViewById(R.id.btnHidden);
        }
    }

    private static class HiddenHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvTitle;
        private final TextView tvPrice;
        private final TextView tvImgCount;
        private final Button btnHidden;

        public HiddenHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvImgCount = itemView.findViewById(R.id.tvImgCount);
            btnHidden = itemView.findViewById(R.id.btnHidden);
        }
    }

    private static class RefusedHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvTitle;
        private final TextView tvPrice;
        private final TextView tvImgCount;
        private final Button btnEdit;

        public RefusedHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvImgCount = itemView.findViewById(R.id.tvImgCount);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    private static class WaitingHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvTitle;
        private final TextView tvPrice;
        private final TextView tvImgCount;

        public WaitingHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvImgCount = itemView.findViewById(R.id.tvImgCount);
        }
    }
}
