package com.example.petmarket2020.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Random;

public class RV_PosterAdapter extends RecyclerView.Adapter<RV_PosterAdapter.MyViewHolder> {
    private final List<PosterItem> listItems;
    private final Activity activity;
    private final StorageReference storageReference;

    public RV_PosterAdapter(Activity activity, List<PosterItem> listItems) {
        this.listItems = listItems;
        this.activity = activity;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.item_home_poster, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        List<String> images = listItems.get(position).getImages();
        String imageUrl = images.get(new Random().nextInt(images.size()));
        Glide.with(activity).load(storageReference.child(imageUrl)).into(holder.imageView);
        String poType = listItems.get(position).getPoType().contains("bán") ? "[BÁN] " : "[MUA] ";
        String title = poType + listItems.get(position).getTitle();
        long price = listItems.get(position).getPrice();
        String are = listItems.get(position).getArea();
        String city = are.contains("Hồ Chí Minh") ? "TP.Hồ Chí Minh" : are;
        String timeStart = listItems.get(position).getTimeStart();
        holder.tvTitle.setText(title);
        holder.tvPrice.setText(Utils.formatCurrencyVN(price));
        holder.tvAddress.setText(city);
        holder.tvDate.setText(timeStart);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgFav;
        TextView tvTitle, tvPrice, tvAddress, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imgFav = itemView.findViewById(R.id.imgFav);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgFav.setOnClickListener(v -> {
                if (imgFav.getTag().toString().equals("1")) {
                    imgFav.setTag("2");
                    imgFav.setImageResource(R.drawable.ic_item_tym_checked);
                    Toast.makeText(itemView.getContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                } else {
                    imgFav.setTag("1");
                    imgFav.setImageResource(R.drawable.ic_item_tym);
                }
            });
        }
    }
}
