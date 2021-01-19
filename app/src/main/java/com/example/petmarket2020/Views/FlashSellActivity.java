package com.example.petmarket2020.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.petmarket2020.Controllers.FlashSellController;
import com.example.petmarket2020.R;
import com.google.firebase.storage.FirebaseStorage;

public class FlashSellActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sell);
        FlashSellController flashSellController = new FlashSellController(this, getSupportFragmentManager());
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String postId = intent.getStringExtra("postId");
        ImageView ivImage = findViewById(R.id.ivImage);
        Glide.with(this).load(FirebaseStorage.getInstance().getReference(image)).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvTime = findViewById(R.id.tvTime);
        tvTime.setText(time);
        RecyclerView rvPkg = findViewById(R.id.rvPkg);
        rvPkg.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvPkg.setHasFixedSize(true);
        flashSellController.getPostPackage(rvPkg,postId);
    }
}