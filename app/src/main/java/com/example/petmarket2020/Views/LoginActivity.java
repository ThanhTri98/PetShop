package com.example.petmarket2020.Views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String REF = "Users";
    private TextInputLayout tilUid, tilPwd;
    private RelativeLayout rlBar;
    private TextView tvError;

    // FireBase
    private DatabaseReference mRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        mRef = FirebaseDatabase.getInstance().getReference(REF);
    }

    private void getWidget() {
        tilUid = findViewById(R.id.tilUid);
        tilPwd = findViewById(R.id.tilPwd);
        tvError = findViewById(R.id.tvError);

        rlBar = findViewById(R.id.rlBar);
    }

    public void callFinish(View view) {
        finish();
    }

    public void callRegisterLG(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, view, "transition_layout_rg");
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class), activityOptions.toBundle());
    }

    private String[] checkFieldValid() {
        String uId = Objects.requireNonNull(tilUid.getEditText()).getText().toString().trim();
        if (TextUtils.isEmpty(uId)) {
            tilUid.setErrorEnabled(true);
            tilUid.setError(getString(R.string.RGNonNull));
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
        return new String[]{uId, pwd};
    }

    public void callLogin(View view) {
        String[] info = checkFieldValid();
        if (info != null) {
            rlBar.setVisibility(View.VISIBLE);
            mRef.child(info[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        String pwd = snapshot.child("pwd").getValue().toString();
                        if (!BCrypt.checkpw(info[1], pwd)) {
                            tvError.setVisibility(View.VISIBLE);
                        } else {
                            SessionManager sessionManager = new SessionManager(LoginActivity.this);
                            String fullName = snapshot.child(SessionManager.KEY_FULLNAME).getValue().toString();
                            String dob = snapshot.child(SessionManager.KEY_DOB).getValue().toString();
                            String phone = snapshot.child(SessionManager.KEY_PHONE).getValue().toString();
                            String gender = snapshot.child(SessionManager.KEY_GENDER).getValue().toString();
                            sessionManager.createLoginSession(info[0], pwd, fullName, dob, phone, gender);
                            finish();
                            tvError.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        tvError.setVisibility(View.VISIBLE);
                    }
                    rlBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
