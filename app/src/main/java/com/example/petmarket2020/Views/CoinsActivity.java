package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.R;
import com.example.petmarket2020.WebviewPaymentActivity;
import com.google.android.material.snackbar.Snackbar;

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


public class CoinsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String vnp_Version = "2.0.0";
    private static final String vnp_Command = "pay";
    private static final String vnp_OrderInfo = "NapTien";
    private static final String orderType = "250000";

    private static final String vnp_IpAddr = "171.248.171.115";
    private static final String vnp_TmnCode = "D9MJUFM5";
    private static final String vnp_hashSecret = "UKHJNECSLUBBETVKEMVABTASUAVDYFDW";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);

    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.idContentLayout1_5M) {
                createURLPayment("150000000");
            } else if (v.getId() == R.id.idContentLayout1M) {
                createURLPayment("100000000");
            } else if (v.getId() == R.id.idContentLayout500) {

                createURLPayment("50000000");
            } else if (v.getId() == R.id.idContentLayout100) {
                createURLPayment("10000000");
            } else if (v.getId() == R.id.idContentLayout50) {
                createURLPayment("5000000");
            } else
                createURLPayment("2000000");
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (
                NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void createURLPayment(String money) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String vnp_TxnRef = String.valueOf((int) (Math.random() * Math.pow(10, 5)));
        String vnp_ReturnUrl = "https://google.com";
        HashMap<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", money);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);// ReturnURL(mapForReturn, myHash));
        vnp_Params.put("vnp_Locale", "VN");
        vnp_Params.put("vnp_Merchant", "PetMarkets");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(dt);
        vnp_Params.put("vnp_CreateDate", dateString);

        String vnp_PayUrl = "http://sandbox.vnpayment.vn/paymentv2/vpcpay.html";


        String vnp_SecureHash = vnp_hashSecret + concatAllFields(vnp_Params);//hashData.toString();
        // encoding sha-256
        String encodedString = encodeSHA256(vnp_SecureHash);
        Log.e("encodedString", encodedString);
        Log.e("vnp_SecureHash", vnp_SecureHash);
        String queryUrl = concatAllFields(vnp_Params) + "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + encodedString;
        String paymentUrl = vnp_PayUrl + "?" + queryUrl;

        Intent intent = new Intent(getApplicationContext(), WebviewPaymentActivity.class);
        intent.putExtra("paymentUrl", paymentUrl);
        intent.putExtra("code", "00");
        intent.putExtra("message", "Success");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String message = data.getStringExtra("message");
                Log.e("message", message);
                Snackbar.make(findViewById(R.id.idContentLayout20), message, Snackbar.LENGTH_SHORT).show();
            }
        }
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

    private static String encodeSHA256(String code) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(code.getBytes(StandardCharsets.UTF_8));
        String encodedString = bytesToHex(encodedHash);
        return encodedString;
    }

    public static String concatAllFields(Map fields) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // create a list and sort it
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        // create a buffer for the md5 input and add the secure secret first
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
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
}


