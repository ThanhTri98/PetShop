package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.R;
import com.smarteist.autoimageslider.SliderView;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private SliderView imageSlider;
    private TextView tvBarTotal, tvTitle, tvPrice, tvBreed, tvGender, tvAge, tvInject, tvHealthy, tvPhoneNumber, tvArea, tvReport;
    private RatingBar ratingBarTotal, ratingUser;
    private EditText etCmt;
    private Button btnSubmit, btnCall, btnSms;
    private RecyclerView rvSamePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getWidget();
        String postId = getIntent().getStringExtra("postId");
        new PostController(this).postDetail
                (postId, imageSlider, new TextView[]
                        {
                                tvTitle, tvPrice, tvBreed, tvGender, tvAge, tvInject, tvHealthy, tvPhoneNumber, tvArea
                        });
    }

    private void getWidget() {
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
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
        tvReport = findViewById(R.id.tvReport);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}