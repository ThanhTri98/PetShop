package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.Broadcast.SmsReceiver;
import com.example.petmarket2020.Controllers.ProfileController;
import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.R;

public class VerifyCodeActivity extends AppCompatActivity {
    private LinearLayout llCode;
    private RelativeLayout rlBar;
    private Button btnResend;
    private TextView tvError, tvSeconds, tvTimeEnd, tvPhoneNumber;

    private MyAsyncTask myAsyncTask;
    private final EditText[] codes = new EditText[6];
    private ProfileController profileController;
    public static String codeResponse = "";
    private String phoneNumber = "",uid="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        getWidget();
        profileController = new ProfileController(this);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        uid = getIntent().getStringExtra("uid");
        tvPhoneNumber.setText(phoneNumber);
        profileController.verifyPhone(phoneNumber);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        initCodes();
        autoFocusCode();

    }

    private void getWidget() {
        llCode = findViewById(R.id.llCode);
        rlBar = findViewById(R.id.rlBar);
        btnResend = findViewById(R.id.btnResend);

        tvSeconds = findViewById(R.id.tvSeconds);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);
        tvError = findViewById(R.id.tvError);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
    }

    private void initCodes() {
        int len = llCode.getChildCount();
        for (int i = 0; i < len; i++) {
            codes[i] = (EditText) llCode.getChildAt(i);
        }
        SmsReceiver.codes = codes;
    }

    public void callFinish(View view) {
        finish();
    }

    public void callVerify(View view) {
        if (!checkValidField()) return;
        tvError.setVisibility(View.GONE);
        rlBar.setVisibility(View.VISIBLE);
        if (verifyCode(codeResponse)) {
            profileController.updateVerifyInfo(1,uid);
            Intent intent = new Intent();
            intent.putExtra(NodeRootDB.USERS, profileController.getUserDetail());
            setResult(RESULT_OK, intent);
            new Handler().postDelayed(() -> {
                rlBar.setVisibility(View.INVISIBLE);
                Toast.makeText(VerifyCodeActivity.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                finish();
            }, 500);

        } else {
            new Handler().postDelayed(() -> {
                btnResend.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.VISIBLE);
                tvSeconds.setVisibility(View.GONE);
                tvTimeEnd.setVisibility(View.GONE);
                myAsyncTask.cancel(true);
                rlBar.setVisibility(View.INVISIBLE);
            }, 500);
        }
    }

    // Xác thực code người dùng
    private boolean verifyCode(String code) {
        StringBuilder usrInputCode = new StringBuilder();
        for (EditText editText : codes) {
            usrInputCode.append(editText.getText().toString());
        }
        if (usrInputCode.length() != codes.length) return false;
//        if (!code.equals("") && usrInputCode.length() == codes.length) return true;
        return usrInputCode.toString().equals(code);
    }


    // Tự động di chuyển sang ô code tiếp theo(1)
    private void autoFocusCode() {
        int len = codes.length;
        for (int i = 0; i < len; i++) {
            if (i != len - 1)
                nextInput(codes[i], i + 1);
            selectAll(codes[i]);
        }
    }

    // (2)
    private void nextInput(EditText editText, final int nextIndex) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    codes[nextIndex].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Gửi lại yêu cầu lấy mã xác nhận
    public void callResendCode(View view) {
        rlBar.setVisibility(View.VISIBLE);
        profileController.verifyPhone(phoneNumber);
        myAsyncTask.cancel(true);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        rlBar.setVisibility(View.INVISIBLE);
    }

    // Chọn tất cả trong edittext
    private void selectAll(final EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                editText.setSelectAllOnFocus(true);
        });
    }

    // Kiểm tra người dùng có nhập đủ các ô code chưa
    private boolean checkValidField() {
        for (EditText editText : codes) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                editText.requestFocus();
                editText.setBackgroundResource(R.drawable.bg_err_otp);
                return false;
            } else {
                editText.setBackgroundResource(R.drawable.bg_otp);
            }
        }
        return true;
    }

    // Cập nhật giao diện đếm ngược 60s
    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvSeconds.setVisibility(View.VISIBLE);
            tvTimeEnd.setVisibility(View.VISIBLE);
            tvTimeEnd.setText(getString(R.string.verifyNoti));
            btnResend.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 60; i >= 0; i--) {
                if (isCancelled())
                    break;
                publishProgress(i);
                SystemClock.sleep(1000);
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvSeconds.setText(values[0] + "s");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            codeResponse = "";
            tvSeconds.setVisibility(View.GONE);
            tvTimeEnd.setText(getString(R.string.verifyTimeEnd));
            btnResend.setVisibility(View.VISIBLE);
        }
    }
}