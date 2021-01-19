package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.PostController;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.R;
import com.smarteist.autoimageslider.SliderView;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    public static final String EDIT = "Sửa";
    public static final String UPDATE = "Cập nhật";
    private ImageView imgFav;
    private SliderView imageSlider;
    private TextView tvBarTotal, tvTime, tvError, tvTitle, tvPrice, tvBreed, tvGender, tvAge, tvInject, tvHealthy, tvPhoneNumber, tvArea;
    private RatingBar ratingBarTotal, ratingUser;
    private ProgressBar pgBar;
    private EditText etComment;
    private Button btnSubmit;
    private RecyclerView rvSamePost, rvRate;
    private View pgBar2;
    private RelativeLayout rlIsRate;

    private PostController postController;
    private String peType;
    private long price;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getWidget();
        postController = new PostController(this);
        PostModel postModel = (PostModel) getIntent().getSerializableExtra("postModel");
        if (postModel != null) {
            postId = postModel.getPostId();
            peType = postModel.getPeType();
            price = postModel.getPrice();
        } else {
            postId = getIntent().getStringExtra("postId");
            peType = getIntent().getStringExtra("peType");
            price = getIntent().getLongExtra("price", 0);
        }

        imgFav.setTag(R.id.postId, postId);
        postController.isFavorite(postId, imgFav);
        postController.isRatedAndGetAllComment(postId, rlIsRate, btnSubmit, ratingUser, etComment, tvTime, ratingBarTotal, tvBarTotal, rvRate);
        postController.postDetail
                (postId, postModel, imageSlider, new TextView[]
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
        findViewById(R.id.btnCall).setOnClickListener(this);
        findViewById(R.id.btnSms).setOnClickListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        ratingBarTotal = findViewById(R.id.ratingBarTotal);
        ratingUser = findViewById(R.id.ratingUser);
        etComment = findViewById(R.id.etComment);
        etComment.addTextChangedListener(this);
        imgFav = findViewById(R.id.imgFav);
        imgFav.setOnClickListener(this);
        imageSlider = findViewById(R.id.imageSlider);
        tvError = findViewById(R.id.tvError);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvBreed = findViewById(R.id.tvBreed);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);
        tvInject = findViewById(R.id.tvInject);
        tvHealthy = findViewById(R.id.tvHealthy);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvArea = findViewById(R.id.tvArea);
        tvTime = findViewById(R.id.tvTime);
        tvBarTotal = findViewById(R.id.tvBarTotal);
        pgBar = findViewById(R.id.pgBar);
        rvSamePost = findViewById(R.id.rvSamePost);
        rvRate = findViewById(R.id.rvRate);
        pgBar2 = findViewById(R.id.pgBar2);
        rlIsRate = findViewById(R.id.rlIsRate);
    }

    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
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
            case R.id.btnCall:
                String phoneNumber = tvPhoneNumber.getText().toString();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phoneNumber))));
                break;
            case R.id.btnSms:
                String phoneNumber2 = tvPhoneNumber.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber2)));
                startActivity(intent);
                break;
            case R.id.btnSubmit:
                String typeBtn = ((Button) v).getText().toString();
                if (!typeBtn.equals(EDIT)) {
                    String comment = etComment.getText().toString().trim();
                    if (TextUtils.isEmpty(comment)) {
                        tvError.setText(t2);
                        new Handler().postDelayed(() -> tvError.setText(null), 2000);
                        return;
                    } else if (!TextUtils.isEmpty(tvError.getText().toString())) {
                        return;
                    }
                    if (typeBtn.equals(UPDATE)) {
                        String oldCmt = (String) etComment.getTag();
                        if (oldCmt.equals(comment)) {
                            Toast.makeText(PostDetailActivity.this, "Không có gì để cập nhật", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    postController.rankingProcess(postId, etComment, ratingUser, pgBar2, btnSubmit, tvTime);
                } else {
                    ratingUser.setIsIndicator(false);
                    etComment.setEnabled(true);
                    etComment.requestFocus();
                    btnSubmit.setText(UPDATE);
                }

                break;
        }
    }

    private final String t1 = "Nội dung > 10 kí tự";
    private final String t2 = "Viết đại gì rồi gửi cũng được mà!!";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            if (s.toString().trim().length() < 11) tvError.setText(t1);
            else tvError.setText(null);
        }
    }

}