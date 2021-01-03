package com.example.petmarket2020.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.Controllers.LoginController;
import com.example.petmarket2020.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilUid, tilPwd;
    private RelativeLayout rlBar;
    private TextView tvError;
    private static final int GG_CODE = 1;
    // FireBase
    private LoginController loginController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        loginController = new LoginController(this);
    }


    private void getWidget() {
        tilUid = findViewById(R.id.tilUid);
        tilPwd = findViewById(R.id.tilPwd);

        tvError = findViewById(R.id.tvError);

        rlBar = findViewById(R.id.rlBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GG_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                firebaseAuthWithGoogle(Objects.requireNonNull(task.getResult(ApiException.class)).getIdToken());
            } catch (ApiException ignored) {
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        loginController.firebaseAuthWithGoogle(idToken, rlBar);
    }

    public void callFinish(View view) {
        finish();
    }

    public void callRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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

    @SuppressLint("NonConstantResourceId")
    public void callLogin(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String[] info = checkFieldValid();
                if (info != null) {
                    loginController.loginWithUidPwd(info[0], info[1], rlBar, tvError);
                }
                break;
            case R.id.ivGG:
                loginController.signInWithGoogle(GG_CODE, getString(R.string.default_web_client_id));
                break;
            case R.id.ivFB:
                Toast.makeText(LoginActivity.this, "Đang lỗi, vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
