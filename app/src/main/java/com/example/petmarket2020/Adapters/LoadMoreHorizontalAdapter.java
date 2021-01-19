package com.example.petmarket2020.Adapters;

import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.DiffUtilCallbackPosterItem;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class LoadMoreHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private int TYPE;
    private boolean isLoading;
    private final List<PosterItem> listItems;
    private final StorageReference storageReference;
    private PostController postController;
    private Location locationUser;

    private final IOnItemClick iOnItemClick;

    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
    }

    public interface IOnItemClick {
        void sendId(String postId, String peType, long price, long views);
    }

    public LoadMoreHorizontalAdapter(List<PosterItem> listItems, int TYPE, IOnItemClick iOnItemClick) {
        super();
        this.TYPE = TYPE;
        this.iOnItemClick = iOnItemClick;
        this.listItems = listItems;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void setPostController(PostController postController) {
        this.postController = postController;
    }

    public void updateData(List<PosterItem> posterItems) {
        final DiffUtilCallbackPosterItem diffUtilCallbackPosterItem = new DiffUtilCallbackPosterItem(this.listItems, posterItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallbackPosterItem);
        this.listItems.clear();
        this.listItems.addAll(posterItems);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_poster, parent, false);
            return new ItemHolder(view);
        } else {
            View view;
            if (TYPE == 0)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_loading, parent, false);
            else
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ver_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            PosterItem posterItem = listItems.get(position);
            ItemHolder itemHolder = (ItemHolder) holder;
            if (posterItem.isHot()) {
                if (!posterItem.getPkgId().equals("pkg0"))
                    itemHolder.ivHot.setVisibility(View.VISIBLE);
                if (posterItem.getPkgId().equals("pkg2"))
                    itemHolder.tvTitle.setTextColor(ContextCompat.getColor(itemHolder.ivHot.getContext(), R.color.colorPrimary));
            }
            if (locationUser != null) {
                Location locationPost = new Location("");
                locationPost.setLatitude(posterItem.getLatitude());
                locationPost.setLongitude(posterItem.getLongitude());
                double distance = (double) Math.round((locationUser.distanceTo(locationPost) / 1000) * 100) / 100;
                itemHolder.distance.setText(distance + " km");
            }
            if (postController != null) {
                ImageView imgFav = itemHolder.imgFav;
                imgFav.setTag(R.id.postId, posterItem.getPostId());
                postController.isFavorite(posterItem.getPostId(), imgFav);
                imgFav.setOnClickListener(v -> postController.setFavorite(imgFav, itemHolder.pgBar));
            }
            List<String> images = posterItem.getImages();
            String imageUrl = images.get(new Random().nextInt(images.size()));
            Glide.with(itemHolder.tvAddress.getContext()).load(storageReference.child(imageUrl))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(itemHolder.imageView);
            String poType = posterItem.getPoType().contains("bán") ? "[BÁN] " : "[MUA] ";
            String title = poType + posterItem.getTitle();
            long price = posterItem.getPrice();
            AtomicLong views = new AtomicLong(posterItem.getViewCounts());
            String area = posterItem.getArea();
            String city = area.contains("Hồ Chí Minh") ? "TP.Hồ Chí Minh" : area;
            String timeStart = posterItem.getTimeStart();
            itemHolder.tvTitle.setText(title);
            itemHolder.tvPrice.setText(Utils.formatCurrencyVN(price));
            itemHolder.tvAddress.setText(city);
            itemHolder.tvDate.setText(timeStart);
            itemHolder.tvViews.setText(String.valueOf(views.get()));
            itemHolder.itemView.setOnClickListener(
                    v -> {
                        views.getAndIncrement();
                        iOnItemClick.sendId(posterItem.getPostId(), posterItem.getPeType(), posterItem.getPrice(), views.get());
                        new Handler().postDelayed(() -> itemHolder.tvViews.setText(String.valueOf(views.get())), 1000);

                    });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (listItems != null && position == listItems.size() - 1 && isLoading) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgFav, ivHot;
        TextView tvTitle, tvPrice, tvAddress, tvDate, tvViews, distance;
        ProgressBar pgBar;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            pgBar = itemView.findViewById(R.id.pgBar);
            imageView = itemView.findViewById(R.id.imageView);
            ivHot = itemView.findViewById(R.id.ivHot);
            imgFav = itemView.findViewById(R.id.imgFav);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvViews = itemView.findViewById(R.id.tvViews);
            distance = itemView.findViewById(R.id.distance);
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void addItemLoading() {
        isLoading = true;
        listItems.add(new PosterItem());
    }

    public void removeItemLoading() {
        isLoading = false;
        int pos = listItems.size() - 1;
        PosterItem posterItem = listItems.get(pos);
        if (posterItem != null) {
            listItems.remove(pos);
            notifyItemRemoved(pos);
        }
    }
}
