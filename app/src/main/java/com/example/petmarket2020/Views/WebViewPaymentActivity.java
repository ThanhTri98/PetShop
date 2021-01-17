package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.R;

public class WebViewPaymentActivity extends AppCompatActivity {
    //    private CoinsController coinsController;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String link = getIntent().getStringExtra("paymentUrl");
        MyWebviewClient myWebviewClient = new MyWebviewClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_payment);
        WebView webView = findViewById(R.id.webViewPaymentVNPAY);
        webView.loadUrl(link);
        webView.setWebViewClient(myWebviewClient);
        WebSettings webSettings = webView.getSettings();
//        coinsController = new CoinsController(this);
        webSettings.setJavaScriptEnabled(true);
    }


   private class MyWebviewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // true to cancel
            String url = request.getUrl().toString().substring(0,18);
            Log.e("shouldOverrideUrlLoading", url);
            if (url.equals("https://google.com")) {
//                Intent i = new Intent();
                //getApplicationContext(), PaymentOptionActivity.class
//                i.putExtra("message", "OK");
//                long values = getIntent().getLongExtra("value", 0);
//                i.putExtra("value", values);
//                Log.e("WebView_Coins_48", values + "");
//                coinsController.payProcess();
                setResult(RESULT_OK);
                finish();
                return true;
            }
//            setResult(RESULT_CANCELED);
//            finish();
            return false;
        }

    }
}
