package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Controllers.CoinsController;
import com.example.petmarket2020.Controllers.ProfileController;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;

public class CoinsActivity extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_CODE = 69;
    private TextView tvRemainsValues;
    private Button btnCoinHistory;
    private RecyclerView rvCoins;
    private View pgBar;
    private String uId;
    private CoinsController coinsController;
    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);
        getWidget();
        profileController = new ProfileController(this);
        coinsController = new CoinsController(this);
        coinsController.getCoinsPackage(rvCoins, pgBar);
        uId = getIntent().getStringExtra("uId");
//        long coins = getIntent().getLongExtra("coins", 0);
//        if (coins == 0)
//            tvRemainsValues.setText("0");
//        else {
//            String coinsS = Utils.formatCurrencyVN(coins);
//            tvRemainsValues.setText(coinsS.substring(0, coinsS.length() - 2));
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        long coins = getIntent().getLongExtra("coins", 0);
        Log.e("CoinActivity_50", coins + "");
        if (coins == 0)
            tvRemainsValues.setText("0");
        else {
            String coinsS = Utils.formatCurrencyVN(coins);
            tvRemainsValues.setText(coinsS.substring(0, coinsS.length() - 2));
        }
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
            if (data != null) {
//                if (resultCode == 99) {
//                    long value = data.getLongExtra("value", 0);
//                    String payments = data.getStringExtra("payments");
//                    boolean isSu = data.getBooleanExtra("isSu", false);
//                    coinsController.payProcess(uId, value, payments, isSu, tvRemainsValues);
//                    String message = data.getStringExtra("message");
//                    Snackbar.make(findViewById(R.id.rvCoins), message, Snackbar.LENGTH_SHORT).show();
//                } else {
                long value = Long.parseLong(data.getStringExtra("value"));
                Log.e("CoinsActivity_value_91", value + "");
                String payments = data.getStringExtra("payments");
                boolean isSu = data.getBooleanExtra("isSu", false);
                coinsController.payProcess(uId, value, payments, isSu, tvRemainsValues);
                profileController.updateCoinsToSession(uId, value);
//                }
            } else {
                Toast.makeText(CoinsActivity.this, "Đã xảy ra lỗi, vui lòng kiểm tra lại!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}