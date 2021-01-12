package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.R;
import com.smarteist.autoimageslider.SliderView;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgFav;
    private SliderView imageSlider;
    private TextView tvBarTotal, tvTitle, tvPrice, tvBreed, tvGender, tvAge, tvInject, tvHealthy, tvPhoneNumber, tvArea;
    private RatingBar ratingBarTotal, ratingUser;
    private ProgressBar pgBar;
    private EditText etCmt;
    private Button btnSubmit, btnCall, btnSms;
    private RecyclerView rvSamePost;
    private PostController postController;
    private String peType;
    private long price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postController = new PostController(this);
        setContentView(R.layout.activity_post_detail);
        getWidget();
        String postId = getIntent().getStringExtra("postId");
        peType = getIntent().getStringExtra("peType");
        price = getIntent().getLongExtra("price", 0);
        imgFav.setTag(R.id.postId, postId);
        postController.isFavorite(postId, imgFav);
        postController.postDetail
                (postId, imageSlider, new TextView[]
                        {
                                tvTitle, tvPrice, tvBreed, tvGender, tvAge, tvInject, tvHealthy, tvPhoneNumber, tvArea
                        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        postController.getSamePosts(peType, price, rvSamePost);
    }

    private void getWidget() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        findViewById(R.id.tvReport).setOnClickListener(this);
        imgFav = findViewById(R.id.imgFav);
        imgFav.setOnClickListener(this);
        imageSlider = findViewById(R.id.imageSlider);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvBreed = findViewById(R.id.tvBreed);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        tvInject = findViewById(R.id.tvInject);
        tvHealthy = findViewById(R.id.tvHealthy);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvArea = findViewById(R.id.tvArea);
        pgBar = findViewById(R.id.pgBar);
        rvSamePost = findViewById(R.id.rvSamePost);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.imgFav:
                postController.setFavorite(imgFav, pgBar);
                break;
            case R.id.tvReport:
                Toast.makeText(PostDetailActivity.this, "CHUA LAM", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}