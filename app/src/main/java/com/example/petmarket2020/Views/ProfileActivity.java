package com.example.petmarket2020.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail, etFullName;
    private TextView tvPhoneNumber, tvGender, tvDob, tvStartDate, tvMailErr;
    private ImageView imgBack;
    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Users users = (Users) getIntent().getSerializableExtra("Users");
        getWidget();
        setListener();
        etFullName.setText(users.getFullName());
        if (!TextUtils.isEmpty(users.getEmail())) {
            etEmail.setText(users.getEmail());
        } else {
            btnVerify.setVisibility(View.INVISIBLE);
            tvMailErr.setVisibility(View.INVISIBLE);
        }
        tvPhoneNumber.setText(users.getPhoneNumber());
        tvGender.setText(users.getGender());
        tvDob.setText(users.getDateOfBirth());
    }

    private void setListener() {
        imgBack.setOnClickListener(this);
    }

    private void getWidget() {
        btnVerify = findViewById(R.id.btnVerify);

        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvGender = findViewById(R.id.tvGender);
        tvDob = findViewById(R.id.tvDob);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvMailErr = findViewById(R.id.tvMailErr);

        imgBack = findViewById(R.id.imgBack);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }

    }
}