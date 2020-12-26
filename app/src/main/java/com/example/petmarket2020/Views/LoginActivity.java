package com.example.petmarket2020.Views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.R;

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void callRegisterLG(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, view, "transition_layout_rg");
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class), activityOptions.toBundle());
    }
}
