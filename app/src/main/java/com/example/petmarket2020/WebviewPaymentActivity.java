package com.example.petmarket2020;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.Views.CoinsActivity;

public class WebviewPaymentActivity extends AppCompatActivity {
    WebView webView;
//    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activity = this;
        String link = (String) getIntent().getStringExtra("paymentUrl");
        setContentView(R.layout.activity_webview_payment);
        MyWebviewClient myWebviewClient = new MyWebviewClient();
        webView = findViewById(R.id.idWebview);
        webView.setWebViewClient(myWebviewClient);
        webView.loadUrl(link);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else
            super.onBackPressed();
    }

    class MyWebviewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // true to cancel
            String url = request.getUrl().toString().substring(0, 18);
            Log.e("shouldOverrideUrlLoading", url);
            if (url.equalsIgnoreCase("https://google.com")) {
                Intent i = new Intent(getApplicationContext(), CoinsActivity.class);
                i.putExtra("message", "Success");
                setResult(RESULT_OK, i);
                Log.e("RESULT_OK", RESULT_OK + "");
                finish();
                return true;
            }
            return false;
        }

    }

}
