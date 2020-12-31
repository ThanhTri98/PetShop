package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.petmarket2020.Adapters.VP_PostAdapter;
import com.example.petmarket2020.R;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static List<String> listTitle;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvTitle;
    public static MyViewPager vpg;
    private ImageView ivBack;
    @SuppressLint("StaticFieldLeak")
    public static BottomAppBar bab;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvBab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWidget();
        setUpViewPager();
        setListener();
        initListTitle();

    }

    private void initListTitle() {
        listTitle = new ArrayList<>();
        listTitle.add("Bạn muốn đăng tin gì?");
        listTitle.add("Loại thú cưng");
        listTitle.add("Chọn giống?");
        listTitle.add("Tiêu đề");
        listTitle.add("Thời hạn");
        listTitle.add("Thông tin thêm");
        listTitle.add("Thêm hình ảnh");
        listTitle.add("Thông tin liên hệ");
        listTitle.add("Xem trước bài đăng");

    }

    private void getWidget() {
        vpg = findViewById(R.id.vpg);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        bab = findViewById(R.id.bab);
        tvBab = findViewById(R.id.tvBab);
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int currentPage = vpg.getCurrentItem();
                if (currentPage > 0) {
                    tvTitle.setText(listTitle.get(currentPage - 1));
                    vpg.setCurrentItem(currentPage - 1);
                    if (currentPage > 2) tvBab.setText("Xác nhận");
                } else {
                    finish();
                }
            }
        });
        bab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int currentPage = vpg.getCurrentItem();
                if (currentPage < 8) {
                    tvTitle.setText(listTitle.get(currentPage + 1));
                    vpg.setCurrentItem(currentPage + 1);
                    if (currentPage == 7) tvBab.setText("Hoàn tất");
                    else if (currentPage > 2) {
                        tvBab.setText("Xác nhận");
                    }
                } else {
                    finish();
                }
            }
        });
    }

    private void setUpViewPager() {
        VP_PostAdapter VPPostAdapter = new VP_PostAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpg.setAdapter(VPPostAdapter);
    }

}