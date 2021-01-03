package com.example.petmarket2020.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.petmarket2020.Controllers.RegisterController;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String PHONE_PATTERN = "^[+]84[3-9][0-9]{8}$";
    private static final String UID_PATTERN = "^[a-z0-9]+$";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.KOREA);
    private TextInputLayout tilFullName, tilUid, tilPhoneNumber, tilPwd;
    private RelativeLayout rlBar;
    private RadioGroup rgGender;
    private TextView tvNoti;
    private DatePicker dpDoB;

    private String defaultGender = "Nam";
    public static String currentUid = "";
    private RegisterController registerController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerController = new RegisterController(this);
        getWidget();
        setListener();
        per();
    }

    private void per() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }
    }


    public void callHiddenKeyboard(View view) {
        Utils.hiddenKeyboard(RegisterActivity.this);
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
        finish();
    }

    private UsersModel checkFieldValid() {
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
        String dateTmp = day + "/" + month + "/" + year;
        try {
            if (new Date().compareTo(simpleDateFormat.parse(dateTmp)) <= 0) {
                tvNoti.setVisibility(View.VISIBLE);
                return null;
            } else {
                tvNoti.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            return null;
        }
        String dateOfBirth = (day - 1) + "/" + month + "/" + year;
        // End: Kiểm tra ngày sinh
        return new UsersModel(uId, pwd, fullName, defaultGender, dateOfBirth, phone);
    }

    public void callRegister(View view) {
        UsersModel usersModel = checkFieldValid();
        if (usersModel != null) {
            registerController.registerUser(usersModel, view, rlBar, tilUid);
        }
    }

}
