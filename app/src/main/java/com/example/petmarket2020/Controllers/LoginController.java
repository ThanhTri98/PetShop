package com.example.petmarket2020.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.Interfaces.IUsers;
import com.example.petmarket2020.Models.UsersModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

// validate data
public class LoginController {
    private final UsersDAL usersDAL;
    private final Activity activity;

    public LoginController(Activity activity) {
        this.activity = activity;
        usersDAL = new UsersDAL(activity);
    }

    public void firebaseAuthWithGoogle(String idToken, RelativeLayout rlBar) {
        rlBar.setVisibility(View.VISIBLE);
        IUsers iUsers = isSu -> {
            rlBar.setVisibility(View.INVISIBLE);
            if (isSu) {
                activity.finish();
                Toast.makeText(activity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        };
        usersDAL.firebaseAuthWithGoogle(idToken, iUsers);
    }

    public void loginWithUidPwd(String uid, String pwd, RelativeLayout rlBar, TextView tvError) {
        if (pwd.length() < 6) {
            tvError.setVisibility(View.VISIBLE);
            return;
        }
        rlBar.setVisibility(View.VISIBLE);
        IUsers iUsers = isSu -> {
            rlBar.setVisibility(View.INVISIBLE);
            if (isSu) {
                activity.finish();
                Toast.makeText(activity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                tvError.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(activity, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                tvError.setVisibility(View.VISIBLE);
            }
        };
        usersDAL.loginWithUidPwd(uid, pwd, iUsers);
    }

    public void signInWithGoogle(int GG_CODE, String clientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);
        googleSignInClient.revokeAccess();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, GG_CODE);
    }
}
