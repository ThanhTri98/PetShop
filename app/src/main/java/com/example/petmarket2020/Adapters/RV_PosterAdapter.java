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

public class RV_PosterAdapter extends RecyclerView.Adapter<RV_PosterAdapter.MyViewHolder> {
    private final List<PosterItem> listItems;
    private final StorageReference storageReference;
    private PostController postController;
    private Location locationUser;
    private final IOnItemClick iOnItemClick;

    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
    }

    public RV_PosterAdapter(List<PosterItem> listItems, IOnItemClick iOnItemClick) {
        super();
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_poster, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PosterItem posterItem = listItems.get(position);
        // Location
        if (locationUser != null) {
            Location locationPost = new Location("");
            locationPost.setLatitude(posterItem.getLatitude());
            locationPost.setLongitude(posterItem.getLongitude());
            double distance = (double) Math.round((locationUser.distanceTo(locationPost) / 1000) * 100) / 100;
            holder.distance.setText(distance +" km");
        }

        if (postController != null) {
            ImageView imgFav = holder.imgFav;
            imgFav.setTag(R.id.postId, posterItem.getPostId());
            postController.isFavorite(posterItem.getPostId(), imgFav);
            imgFav.setOnClickListener(v -> postController.setFavorite(imgFav, holder.pgBar));
        }
        if (posterItem.isHot()) {
            if (!posterItem.getPkgId().equals("pkg0"))
                holder.ivHot.setVisibility(View.VISIBLE);
            if (posterItem.getPkgId().equals("pkg2"))
                holder.tvTitle.setTextColor(ContextCompat.getColor(holder.ivHot.getContext(), R.color.colorPrimary));
        }
        List<String> images = posterItem.getImages();
        String imageUrl = images.get(new Random().nextInt(images.size()));
        Glide.with(holder.tvAddress.getContext()).load(storageReference.child(imageUrl)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        String poType = posterItem.getPoType().contains("bán") ? "[BÁN] " : "[MUA] ";
        String title = poType + posterItem.getTitle();
        long price = posterItem.getPrice();
        AtomicLong views = new AtomicLong(posterItem.getViewCounts());
        String are = posterItem.getArea();
        String city = are.contains("Hồ Chí Minh") ? "TP.Hồ Chí Minh" : are;
        String timeStart = posterItem.getTimeStart();
        holder.tvTitle.setText(title);
        holder.tvPrice.setText(Utils.formatCurrencyVN(price));
        holder.tvAddress.setText(city);
        holder.tvDate.setText(timeStart);
        holder.tvViews.setText(String.valueOf(views.get()));
        holder.itemView.setOnClickListener(v ->
        {
            views.addAndGet(1);
            iOnItemClick.sendId(posterItem.getPostId(), posterItem.getPeType(), posterItem.getPrice(), views.get());
            new Handler().postDelayed(() -> holder.tvViews.setText(String.valueOf(views.get())), 500);
        });

    }


    @Override
    public int getItemCount() {
        if (listItems != null)
            return listItems.size();
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgFav, ivHot;
        TextView tvTitle, tvPrice, tvAddress, tvDate, tvViews, distance;
        ProgressBar pgBar;

        public MyViewHolder(@NonNull View itemView) {
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

    public interface IOnItemClick {
        void sendId(String postId, String peType, long price, long views);
    }
}
