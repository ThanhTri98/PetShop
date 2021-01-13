package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.CoinsController;
import com.example.petmarket2020.R;

public class CoinsActivity extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_CODE = 69;
    private TextView tvRemainsValues;
    private Button btnCoinHistory;
    private RecyclerView rvCoins;
    private View pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);
        CoinsController coinsController = new CoinsController(this);
        getWidget();
        coinsController.getCoinsPackage(rvCoins, pgBar);
    }

    private void getWidget() {
        tvRemainsValues = findViewById(R.id.tvRemainsValues);
        btnCoinHistory = findViewById(R.id.btnCoinHistory);
        rvCoins = findViewById(R.id.rvCoins);
        pgBar = findViewById(R.id.pgBar);

        findViewById(R.id.ivBack).setOnClickListener(this);
        findViewById(R.id.btnCoinHistory).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnCoinHistory:
                Toast.makeText(CoinsActivity.this, "CHUA LAM", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                Toast.makeText(CoinsActivity.this, "Nạp thành công", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CoinsActivity.this, "Đã xảy ra lỗi, vui lòng kiểm tra lại!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}