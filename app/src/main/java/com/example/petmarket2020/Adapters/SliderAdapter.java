package com.example.petmarket2020.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.petmarket2020.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder> {
    private final List<Bitmap> bitmapList;

    public SliderAdapter(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_view_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.ivImage.setImageBitmap(bitmapList.get(position));

    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    public static class MyViewHolder extends SliderViewAdapter.ViewHolder {
        private final ImageView ivImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
