package com.example.petmarket2020.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petmarket2020.R;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private static  final String STATUS_CHECKED = "Đã lưu";
    private static  final String STATUS_UNCHECKED = "Lưu tin";
    private static  final int ICON_UNCHECKED = R.drawable.ic_item_tym;
    private static  final int ICON_CHECKED = R.drawable.ic_item_tym_checked;
    private LinearLayout llSavePosts;
    private TextView tvStatus;
    private View vIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getWidget();
        setListener();
    }
    private void getWidget(){
        llSavePosts = findViewById(R.id.llSavePosts);
        tvStatus = findViewById(R.id.tvStatus);
        vIcon = findViewById(R.id.vIcon);
    }
    private void setListener(){
        llSavePosts.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llSavePosts:
                if(tvStatus.getText().toString().equals(STATUS_CHECKED)){
                    tvStatus.setText(STATUS_UNCHECKED);
                    vIcon.setBackgroundResource(ICON_UNCHECKED);
                }else{
                    tvStatus.setText(STATUS_CHECKED);
                    vIcon.setBackgroundResource(ICON_CHECKED);
                }
                break;
        }
    }
}