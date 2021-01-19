package com.example.petmarket2020.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.ShowImagePicker;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.PostActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddImageFragment extends Fragment {
    private static final int REQUEST_IMAGE = 1;
    private ShowImagePicker showImagePicker;
    private TextView tvTitle, tvError;
    private MyViewPager vpg;
    private LinearLayout llImages;

    private StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showImagePicker = new ShowImagePicker(getActivity(), REQUEST_IMAGE);
        vpg = Objects.requireNonNull(getActivity()).findViewById(R.id.vpg);
        tvTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.tvTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_image, container, false);
        llImages = view.findViewById(R.id.llImages);
        tvError = view.findViewById(R.id.tvError);
        // load old image
        HashMap<String, Bitmap> picturesMap = PostActivity.getAllImages();
        if (picturesMap != null) {
            loadOldImageLocal(picturesMap);
        }
        List<String> images;
        try {
            images = (List<String>) PostActivity.getData(PostActivity.KEY_OLD_IMAGE);
        } catch (NullPointerException e) {
            images = null;
        }
        if (images != null) {
            storageReference = FirebaseStorage.getInstance().getReference();
            loadImageNetWork(images);
        }
        view.findViewById(R.id.flAddImage).setOnClickListener(v -> {
            if (llImages.getChildCount() == 6) {
                tvError.setText(getString(R.string.AIText02));
                new Handler().postDelayed(() -> tvError.setText(""), 2000);
                return;
            }
            showImagePicker.showImagePickerOptions();
        });
        view.findViewById(R.id.bab).setOnClickListener(v -> {
            if (llImages.getChildCount() > 1) {
                int currentIndex = vpg.getCurrentItem();
                vpg.setCurrentItem(currentIndex + 1);
                tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
            } else {
                tvError.setText(getString(R.string.AIText2));
            }
        });
        return view;
    }

    private void loadImageNetWork(List<String> images) {
        images.forEach(s -> {
            tvError.setText("");
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.image_layout, null);
            ImageView ivImage = view.findViewById(R.id.ivImage);
            String key = getRandomKey();
            view.findViewById(R.id.ivDelete).setOnClickListener(v -> {
                llImages.removeView(view);
                images.remove(s);
                PostActivity.addImageRemoved(s);
                PostActivity.addOrUpdateData(PostActivity.KEY_OLD_IMAGE, images);
                PostActivity.removeImage(key);
            });
            Glide.with(this).load(storageReference.child(s)).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            llImages.addView(view);
            new Handler().postDelayed(() -> processDataImages(((BitmapDrawable) ivImage.getDrawable()).getBitmap(), key), 500);
        });
    }

    private void loadOldImageLocal(HashMap<String, Bitmap> picturesMap) {
        picturesMap.forEach((s, bitmap) -> {
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.image_layout, null);
            ImageView ivImage = view.findViewById(R.id.ivImage);
            view.findViewById(R.id.ivDelete).setOnClickListener(v -> {
                llImages.removeView(view);
                PostActivity.removeImage(s);
            });
            Glide.with(this).load(bitmap).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            llImages.addView(view);
            tvError.setText("");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getParcelableExtra("path");
                String key = getRandomKey();
                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.image_layout, null);
                ImageView ivImage = view.findViewById(R.id.ivImage);
                view.findViewById(R.id.ivDelete).setOnClickListener(v -> {
                    llImages.removeView(view);
                    PostActivity.removeImage(key);
                });
                Glide.with(this).load(uri.toString()).into(ivImage);
                llImages.addView(view);
                tvError.setText("");
                new Handler().postDelayed(() -> processDataImages(((BitmapDrawable) ivImage.getDrawable()).getBitmap(), key), 500);
            }
        }
    }

    private void processDataImages(Bitmap imageView, String key) {
        PostActivity.addImage(key, imageView);
    }

    private String getRandomKey() {
        return String.valueOf(System.currentTimeMillis());
    }
}