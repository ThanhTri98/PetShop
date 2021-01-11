package com.example.petmarket2020.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder> {
    private List<Bitmap> bitmapList;
    private List<String> stringList;
    private StorageReference storageReference;

    public SliderAdapter() {
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_view_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        if (bitmapList != null)
            viewHolder.ivImage.setImageBitmap(bitmapList.get(position));
        else
            Glide.with(viewHolder.ivImage.getContext())
                    .load(storageReference.child(stringList.get(position)))
                    .placeholder(R.drawable.progress_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.ivImage);


    }

    @Override
    public int getCount() {
        if (bitmapList != null) return bitmapList.size();
        else return stringList.size();
    }

    public static class MyViewHolder extends SliderViewAdapter.ViewHolder {
        private final ImageView ivImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
