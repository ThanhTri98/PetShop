package com.example.petmarket2020.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.R;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        PostController postController = new PostController(this);
        RecyclerView rvFav = findViewById(R.id.rvFav);
        rvFav.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvFav.setHasFixedSize(true);
        postController.getFavoriteList(rvFav);
    }
}