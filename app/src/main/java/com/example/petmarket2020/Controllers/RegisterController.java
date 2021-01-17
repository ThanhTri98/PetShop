package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.RegisterActivity;
import com.example.petmarket2020.Views.SuccessfullyActivity;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterController {
    private final UsersDAL usersDAL;
    private final Activity activity;

    public RegisterController(Activity activity) {
        this.activity = activity;
        this.usersDAL = new UsersDAL(activity);
    }

    public void registerUser(UsersModel usersModel, View viewTrans, RelativeLayout rlBar, TextInputLayout tilUid) {
        tilUid.setErrorEnabled(false);
        rlBar.setVisibility(View.VISIBLE);
        this.usersDAL.registerUser(usersModel, new IControlData() {
            @Override
            public void isSuccessful(boolean isSu) {
                if (isSu) {
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, viewTrans, "transition_next");
                    activity.startActivity(new Intent(activity, SuccessfullyActivity.class), activityOptions.toBundle());
                    activity.finish();
                } else {
                    tilUid.setErrorEnabled(true);
                    tilUid.setError(activity.getString(R.string.RGDupUid));
                    tilUid.requestFocus();
                    RegisterActivity.currentUid = usersModel.getUid();
                }
                rlBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
