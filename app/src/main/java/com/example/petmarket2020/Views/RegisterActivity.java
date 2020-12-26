package com.example.petmarket2020.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String REF = "Users";
    private static final String PHONE_PATTERN = "^[+]84[3-9][0-9]{8}$";
    private static final String UID_PATTERN = "^[a-z0-9]+$";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    private Date currentDate;
    private TextInputLayout tilFullName, tilUid, tilPhoneNumber, tilPwd;
    private RelativeLayout rlBar;
    private RadioGroup rgGender;
    private TextView tvNoti;
    private DatePicker dpDoB;

    private String defaultGender = "Nam";
    private String currentUid = "";
    // FireBase
    private DatabaseReference mRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWidget();
        currentDate = new Date();
        mRef = FirebaseDatabase.getInstance().getReference(REF);
        setListener();
        per();
    }

    private void per() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }
    }


    public void callHiddenKeyboard(View view) {
        Utils.HiddenKeyboard(RegisterActivity.this);
    }

    private void getWidget() {
        tilFullName = findViewById(R.id.tilFullName);
        tilPhoneNumber = findViewById(R.id.tilPhoneNumber);
        tilUid = findViewById(R.id.tilUid);
        tilPwd = findViewById(R.id.tilPwd);

        rlBar = findViewById(R.id.rlBar);
        rgGender = findViewById(R.id.rgGender);
        tvNoti = findViewById(R.id.tvNoti);
        dpDoB = findViewById(R.id.dpDoB);
    }

    @SuppressLint("NonConstantResourceId")
    private void setListener() {
        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rdOther:
                    defaultGender = "Khác";
                    break;
                case R.id.rdFeMale:
                    defaultGender = "Nữ";
                    break;
                default:
                    defaultGender = "Nam";
                    break;
            }
        });
    }

    public void callFinish(View view) {
        finishAfterTransition();
    }

    private Users checkFieldValid() {
        // Start: Kiểm tra giá trị người dùng nhập
        String fullName = Objects.requireNonNull(tilFullName.getEditText()).getText().toString().trim();
        if (TextUtils.isEmpty(fullName)) {
            tilFullName.setErrorEnabled(true);
            tilFullName.setError(getString(R.string.RGNonNull));
            tilFullName.requestFocus();
            return null;
        } else {
            tilFullName.setErrorEnabled(false);
        }
        String phoneTmp = Objects.requireNonNull(tilPhoneNumber.getEditText()).getText().toString().trim();
        String phone = phoneTmp.length() > 0 && phoneTmp.charAt(0) == '0' ? "+84" + phoneTmp.substring(1) : "+84" + phoneTmp;
        if (TextUtils.isEmpty(phone)) {
            tilPhoneNumber.setErrorEnabled(true);
            tilPhoneNumber.setError(getString(R.string.RGNonNull));
            tilPhoneNumber.requestFocus();
            return null;
        } else {
            tilPhoneNumber.setErrorEnabled(false);
        }
        if (!Pattern.matches(PHONE_PATTERN, phone)) {
            tilPhoneNumber.setErrorEnabled(true);
            tilPhoneNumber.setError(getString(R.string.RGNonFormat));
            tilPhoneNumber.requestFocus();
            return null;
        } else {
            tilPhoneNumber.setErrorEnabled(false);
        }
        String uId = Objects.requireNonNull(tilUid.getEditText()).getText().toString().trim();
        if (uId.equals(currentUid)) return null;
        if (TextUtils.isEmpty(uId)) {
            tilUid.setErrorEnabled(true);
            tilUid.setError(getString(R.string.RGNonNull));
            tilUid.requestFocus();
            return null;
        } else {
            tilUid.setErrorEnabled(false);
        }
        if (uId.contains(" ")) {
            tilUid.setErrorEnabled(true);
            tilUid.setError(getString(R.string.RGNonFormat));
            tilUid.requestFocus();
            return null;
        } else {
            tilUid.setErrorEnabled(false);
        }
        if (!Pattern.matches(UID_PATTERN, uId)) {
            tilUid.setErrorEnabled(true);
            tilUid.setError(getString(R.string.RGNonFormat));
            tilUid.requestFocus();
            return null;
        } else {
            tilUid.setErrorEnabled(false);
        }
        String pwd = Objects.requireNonNull(tilPwd.getEditText()).getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            tilPwd.setErrorEnabled(true);
            tilPwd.setError(getString(R.string.RGNonNull));
            tilPwd.requestFocus();
            return null;
        } else {
            tilPwd.setErrorEnabled(false);
        }
        if (pwd.contains(" ")) {
            tilPwd.setErrorEnabled(true);
            tilPwd.setError(getString(R.string.RGNonFormat));
            tilPwd.requestFocus();
            return null;
        } else {
            tilPwd.setErrorEnabled(false);
        }
        if (pwd.length() < 6) {
            tilPwd.setErrorEnabled(true);
            tilPwd.setError(getString(R.string.RGLenPwd));
            tilPwd.requestFocus();
            return null;
        } else {
            tilPwd.setErrorEnabled(false);
        }

        // End: Kiểm tra giá trị người dùng nhập
        // Start: Kiểm tra ngày sinh
        int year = dpDoB.getYear();
        int month = dpDoB.getMonth() + 1;
        int day = dpDoB.getDayOfMonth() + 1;
        String dateTmp = year + "/" + month + "/" + day;
        try {
            if (currentDate.compareTo(simpleDateFormat.parse(dateTmp)) <= 0) {
                tvNoti.setVisibility(View.VISIBLE);
                return null;
            } else {
                tvNoti.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            return null;
        }
        String dateOfBirth = year + "/" + month + "/" + (day - 1);
        // End: Kiểm tra ngày sinh
        return new Users(fullName, uId, pwd, defaultGender, dateOfBirth, phone);
    }

    public void callNextStep(View view) {
        Users users = checkFieldValid();
        if (users != null) {
            rlBar.setVisibility(View.VISIBLE);
            mRef.child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        currentUid = users.getUid();
                        tilUid.setErrorEnabled(true);
                        tilUid.setError(getString(R.string.RGDupUid));
                        tilUid.requestFocus();
                        rlBar.setVisibility(View.INVISIBLE);
                    } else {
                        tilUid.setErrorEnabled(false);
                        sendCodeOTP(users.getPhoneNumber());
                        Intent intent = new Intent(getApplicationContext(), Register2ndActivity.class);
                        intent.putExtra("Users", users);
                        // Add transition
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, view, "transition_layout");
                        rlBar.setVisibility(View.GONE);
                        startActivity(intent, activityOptions.toBundle());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(RegisterActivity.this, "Kết nối internet có vấn đề!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendCodeOTP(String phone) {
        // Tạo mã OTP
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                62L,
                TimeUnit.SECONDS,
                RegisterActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.w("onVerificationCompleted", phoneAuthCredential.getSmsCode());
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.w("onVerificationFailed", "FAILEDDDD");
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Log.w("onCodeAutoRetrievalTimeOut", s);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Log.w("onCodeSent", s);
                    }
                }
        );
    }
}
