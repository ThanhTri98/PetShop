package com.example.petmarket2020.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Random;

public class RV_FavoritesAdapter extends RecyclerView.Adapter<RV_FavoritesAdapter.FavoritesHolder> {
    private List<PosterItem> posterItems;
    private final StorageReference storageReference;
    private PostController postController;

    public RV_FavoritesAdapter(RV_FavoritesAdapter.IItemOnClick iItemOnClick) {
        this.iItemOnClick = iItemOnClick;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void setData(List<PosterItem> posterItems) {
        this.posterItems = posterItems;
        notifyDataSetChanged();
    }

    public void setPostController(PostController postController) {
        this.postController = postController;
    }

    private final RV_FavoritesAdapter.IItemOnClick iItemOnClick;

    public interface IItemOnClick {
        void itemClick(long price, String peType, String postId);
    }

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_favorite, parent, false);
        return new FavoritesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {
        PosterItem posterItem = posterItems.get(position);
        if (postController != null) {
            ImageView imgFav = holder.imgFav;
            imgFav.setTag(R.id.postId, posterItem.getPostId());
            postController.isFavorite(posterItem.getPostId(), imgFav);
            imgFav.setOnClickListener(v -> postController.setFavorite(imgFav, holder.pgBar));
        }
        String title = posterItem.getTitle();
        long price = posterItem.getPrice();
        String image = posterItem.getImages().get(new Random().nextInt(posterItem.getImages().size()));
        holder.tvTitle.setText(title);
        holder.tvPrice.setText(Utils.formatCurrencyVN(price));
        Glide.with(holder.ivImage).load(storageReference.child(image)).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivImage);
        holder.itemView.setOnClickListener(v -> iItemOnClick.itemClick(price, posterItem.getPeType(), posterItem.getPostId()));
    }

    @Override
    public int getItemCount() {
        return posterItems != null ? posterItems.size() : 0;
    }

    public static class FavoritesHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvPrice;
        private final ImageView ivImage;
        private final ImageView imgFav;
        private final ProgressBar pgBar;

        public FavoritesHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            imgFav = itemView.findViewById(R.id.imgFav);
            pgBar = itemView.findViewById(R.id.pgBar);
        }
    }
}
