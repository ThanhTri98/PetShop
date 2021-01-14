package com.example.petmarket2020.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class PaymentOptionActivity extends AppCompatActivity {
    private int options; //0: no select, 1: Momo, 2: VNPay
    private long coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        long value = getIntent().getLongExtra("value", 0);
        coins = value;
        String vl1 = Utils.formatCurrencyVN(value);
        String vl2 = vl1.substring(0, vl1.length() - 2);
        TextView tvCoins = findViewById(R.id.tvCoins);
        tvCoins.setText(vl2);
        TextView tvRealCoins = findViewById(R.id.tvRealCoins);
        tvRealCoins.setText(vl1);
        TextView tvRealCoins2 = findViewById(R.id.tvRealCoins2);
        tvRealCoins2.setText(vl1);

        TextView tvPayment = findViewById(R.id.tvPayment);
        RadioGroup rgOptions = findViewById(R.id.rgOptions);
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            tvPayment.setBackgroundResource(R.color.colorVerify);
            tvPayment.setTextColor(Color.WHITE);
            if (checkedId == R.id.rdMomo) {
                options = 1;
            } else {
                options = 2;
            }
        });
        tvPayment.setOnClickListener(v -> {
            if (options == 0) return;
            if (options == 1) {
                requestMomoPayment(value);
            } else {
                Toast.makeText(PaymentOptionActivity.this, "VNPay", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void requestMomoPayment(long amount) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        String merchantName = "CGV Cinemas";
        String merchantCode = "CGV19072017";
        String merchantNameLabel = "Nhà cung cấp";
//        String description = "Fast & Furious 8";
        String amountS = String.valueOf(amount);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, amountS);
        String ss = Utils.formatCurrencyVN(amount);
        String description = "Nạp " + ss.substring(0, ss.length() - 2) + " Đồng Tốt vào ví";
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
        //client Optional
        eventValue.put(MoMoParameterNamePayment.FEE, "0");
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);

        eventValue.put(MoMoParameterNamePayment.REQUEST_ID, merchantCode + "-" + UUID.randomUUID().toString());
        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, merchantCode);

//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("site_code", "008");
//            objExtraData.put("site_name", "CGV Cresent Mall");
//            objExtraData.put("screen_code", 0);
//            objExtraData.put("screen_name", "Special");
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
//            objExtraData.put("movie_format", "2D");
//            objExtraData.put("ticket", "{\"ticket\":{\"01\":{\"type\":\"std\",\"price\":110000,\"qty\":3}}}");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    if (data.getStringExtra("data") != null && !data.getStringExtra("data").equals("")) {
                        // Thanh Cong
                        setResultMomo(RESULT_OK);
                    } else {
                        setResultMomo(RESULT_CANCELED);
                    }

                } else {
                    setResultMomo(RESULT_CANCELED);
                }
            } else {
                setResultMomo(RESULT_CANCELED);
            }
        } else {
            setResultMomo(RESULT_CANCELED);
        }
    }

    private void setResultMomo(int result) {
        Intent intent = new Intent();
        intent.putExtra("value", coins);
        intent.putExtra("payments", "Momo");
        intent.putExtra("isSu", result == RESULT_OK);
        setResult(result, intent);
        finish();
    }
}