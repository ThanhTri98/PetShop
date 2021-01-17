package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class PaymentOptionActivity extends AppCompatActivity {
    private int options; //0: no select, 1: Momo, 2: VNPay
    private long coins;

    private static final String vnp_Version = "2.0.0";
    private static final String vnp_Command = "pay";
    private static final String vnp_OrderInfo = "NapTien";
    private static final String orderType = "250000";

    private static final String vnp_IpAddr = "171.248.171.115";
    private static final String vnp_TmnCode = "D9MJUFM5";
    private static final String vnp_hashSecret = "UKHJNECSLUBBETVKEMVABTASUAVDYFDW";

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
                try {
                    requestVNPayment(value);
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
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

    //VNPay
    public void requestVNPayment(long money) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String vnp_TxnRef = String.valueOf((int) (Math.random() * Math.pow(10, 5)));
        String vnp_ReturnUrl = "https://google.com";
        HashMap<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", money * 100 + "");
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);// ReturnURL(mapForReturn, myHash));
        vnp_Params.put("vnp_Locale", "VN");
        vnp_Params.put("vnp_Merchant", "PetMarkets");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", dateString);

        String vnp_PayUrl = "http://sandbox.vnpayment.vn/paymentv2/vpcpay.html";


        String vnp_SecureHash = vnp_hashSecret + concatAllFields(vnp_Params);//hashData.toString();
        // encoding sha-256
        String encodedString = encodeSHA256(vnp_SecureHash);
//        Log.e("encodedString", encodedString);
//        Log.e("vnp_SecureHash", vnp_SecureHash);
        String queryUrl = concatAllFields(vnp_Params) + "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + encodedString;
        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
//        Log.e("vnp_SecureHash", paymentUrl);
        Intent intent = new Intent(getApplicationContext(), WebViewPaymentActivity.class);
        intent.putExtra("paymentUrl", paymentUrl);
        intent.putExtra("value", money);
        startActivityForResult(intent, 1);
    }

    private static String encodeSHA256(String code) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(code.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String concatAllFields(HashMap<String, String> fields) throws UnsupportedEncodingException {
        // create a list and sort it
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        // create a buffer for the md5 input and add the secure secret first
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(URLDecoder.decode(fieldValue, "UTF-8"));
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
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
        } else if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
                setResultVNPay(resultCode);
//            } else {
//
//            }
        }
    }

    private void setResultVNPay(int result) {
        Intent intent = new Intent();
        intent.putExtra("value", coins);
        intent.putExtra("payments", "VNPay");
        intent.putExtra("isSu", result == RESULT_OK);
        setResult(result, intent);
        finish();
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