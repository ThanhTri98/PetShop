package com.example.petmarket2020.Views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.R;

public class SuccessfullyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterActivity.activity.finish();
        setContentView(R.layout.activity_successfully);
    }

    public void callLogin(View view) {
        // Add transition
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SuccessfullyActivity.this, view, "transition_layout_lg");
        startActivity(new Intent(SuccessfullyActivity.this, LoginActivity.class), activityOptions.toBundle());
        finish();
    }

    public void callFinish(View view) {
        finish();
    }
}
