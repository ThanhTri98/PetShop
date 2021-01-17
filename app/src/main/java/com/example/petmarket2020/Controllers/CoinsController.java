package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.RV_CoinsAdapter;
import com.example.petmarket2020.DAL.CoinsDAL;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.CoinsModel;
import com.example.petmarket2020.Views.CoinsActivity;
import com.example.petmarket2020.Views.PaymentOptionActivity;

import java.util.List;

public class CoinsController {
    private final Activity activity;
    private final CoinsDAL coinsDAL;

    public CoinsController(Activity activity) {
        this.activity = activity;
        coinsDAL = new CoinsDAL();
    }

    public void getCoinsPackage(RecyclerView rvCoins, View pgBar) {
        pgBar.setVisibility(View.VISIBLE);
        rvCoins.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        coinsDAL.getCoinsPackage(new IControlData() {
            @Override
            public void responseData(Object data) {
                List<CoinsModel> coinsModels = (List<CoinsModel>) data;
                if (!coinsModels.isEmpty()) {
                    RV_CoinsAdapter rv_coinsAdapter = new RV_CoinsAdapter(coinsModels, value -> {
                        Intent intent = new Intent(activity, PaymentOptionActivity.class);
                        intent.putExtra("value", value);
                        activity.startActivityForResult(intent, CoinsActivity.REQUEST_CODE);
                    });
                    rvCoins.setAdapter(rv_coinsAdapter);
                    rvCoins.setHasFixedSize(true);
                    pgBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void payProcess(String uId, long coins, String payments, boolean isSuccess, TextView tvRemainsValues) {
        coinsDAL.payProcess(uId, coins, payments, isSuccess, new IControlData() {
            @Override
            public void responseData(Object data) {
                if (data != null) {
                    String valueS = Utils.formatCurrencyVN(coins);
                    tvRemainsValues.setText(Utils.formatCurrencyVN((long) data));
                    Toast.makeText(activity, "Nạp thành công " + valueS, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Đã xảy ra lỗi, vui lòng kiểm tra lại!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
