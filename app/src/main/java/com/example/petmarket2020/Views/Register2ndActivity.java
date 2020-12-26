package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.Broadcast.SmsReceiver;
import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Register2ndActivity extends AppCompatActivity {
    private static final String REF = "Users";
    private LinearLayout llCode;
    private RelativeLayout rlBar;
    private Button btnResend;
    private TextView tvError, tvSeconds, tvTimeEnd;

    private Users users;
    private MyAsyncTask myAsyncTask;
    private final EditText[] codes = new EditText[6];
    // FireBase
    private DatabaseReference mRef;
    public static String codeResponse = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2nd);
        getWidget();
        initCodes();
        autoFocusCode();
        mRef = FirebaseDatabase.getInstance().getReference(REF);
        users = (Users) getIntent().getSerializableExtra("Users");
        myAsyncTask = new MyAsyncTask(tvSeconds, tvTimeEnd, btnResend);
        myAsyncTask.execute();
    }

    private void getWidget() {
        llCode = findViewById(R.id.llCode);
        rlBar = findViewById(R.id.rlBar);
        btnResend = findViewById(R.id.btnResend);

        tvSeconds = findViewById(R.id.tvSeconds);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);
        tvError = findViewById(R.id.tvError);
    }

    private void initCodes() {
        int len = llCode.getChildCount();
        for (int i = 0; i < len; i++) {
            codes[i] = (EditText) llCode.getChildAt(i);
        }
        SmsReceiver.codes = codes;
    }

    public void callFinish(View view) {
        supportFinishAfterTransition();
    }

    @SuppressLint("SetTextI18n")
    public void callRegister(View view) {
        if (!checkValidField()) return;
        if (users != null) {
            tvError.setVisibility(View.GONE);
            if (verifyCode(codeResponse)) {
                rlBar.setVisibility(View.VISIBLE);
                mRef.child(users.getUid()).setValue(users).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Register2ndActivity.this, view, "transition_next");
                        startActivity(new Intent(Register2ndActivity.this, SuccessfullyActivity.class), activityOptions.toBundle());
                        finish();
                    } else {
                        Toast.makeText(Register2ndActivity.this, "Kết nối internet có vấn đề!", Toast.LENGTH_LONG).show();
                    }
                    rlBar.setVisibility(View.GONE);
                });
            } else {
                btnResend.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.VISIBLE);
                tvSeconds.setVisibility(View.GONE);
                tvTimeEnd.setVisibility(View.GONE);
                myAsyncTask.cancel(true);
            }
        }
    }

    // Xác thực code người dùng
    private boolean verifyCode(String code) {
//        Toast.makeText(this,code,Toast.LENGTH_LONG).show();
        StringBuilder usrInputCode = new StringBuilder();
        for (EditText editText : codes) {
            usrInputCode.append(editText.getText().toString());
        }
        if (usrInputCode.length() != codes.length) return false;
        if (!code.equals("") && usrInputCode.length() == codes.length) return true;
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
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                users.getPhoneNumber(),
                61L,
                TimeUnit.SECONDS,
                Register2ndActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        codeResponse = phoneAuthCredential.getSmsCode();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                }
        );
        myAsyncTask.cancel(true);
        myAsyncTask = new MyAsyncTask(tvSeconds, tvTimeEnd, btnResend);
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
    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        TextView tvSeconds, tvTimeEnd;
        Button btnResend;

        public MyAsyncTask(TextView tvSeconds, TextView tvTimeEnd, Button btnResend) {
            this.tvSeconds = tvSeconds;
            this.tvTimeEnd = tvTimeEnd;
            this.btnResend = btnResend;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvSeconds.setVisibility(View.VISIBLE);
            tvTimeEnd.setVisibility(View.VISIBLE);
            tvTimeEnd.setText("Mã kích hoạt còn hiệu lực:");
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            codeResponse = "";
            tvSeconds.setVisibility(View.GONE);
            tvTimeEnd.setText("Mã kích hoạt hết hiệu lực");
            btnResend.setVisibility(View.VISIBLE);
        }
    }
}