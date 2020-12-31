package com.example.petmarket2020.Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.Users;
import com.example.petmarket2020.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilUid, tilPwd;
    private RelativeLayout rlBar;
    private TextView tvError;
    private SessionManager sessionManager;
    //Google, Facebook SignIn
    private static final int GG_CODE = 1;
    private GoogleSignInClient mGoogleSignInClient;
    // FireBase
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        sessionManager = new SessionManager(this);
        mRef = FirebaseDatabase.getInstance().getReference(NodeRootDB.USERS);
        mAuth = FirebaseAuth.getInstance();
        createRequest();
//        callbackManager = CallbackManager.Factory.create();
    }


    private void getWidget() {

        tilUid = findViewById(R.id.tilUid);
        tilPwd = findViewById(R.id.tilPwd);
        tvError = findViewById(R.id.tvError);

        //    private CallbackManager callbackManager;
        ImageView imgGG = findViewById(R.id.imgGG);
        imgGG.setOnClickListener(this);
        ImageView imgFB = findViewById(R.id.imgFB);
        imgFB.setOnClickListener(this);

        rlBar = findViewById(R.id.rlBar);
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.revokeAccess();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GG_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> Log.d("Hihiihihii", object.toString()));
//        Bundle bundle = new Bundle();
//        bundle.putString("fields", "gender,name,id,first_name,last_name");
//        graphRequest.setParameters(bundle);
//        graphRequest.executeAsync();
        if (requestCode == GG_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                firebaseAuthWithGoogle(Objects.requireNonNull(task.getResult(ApiException.class)).getIdToken());
            } catch (ApiException ignored) {
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        rlBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser userDB = mAuth.getCurrentUser();
                        if (userDB != null) {
                            String email = userDB.getEmail();
                            assert email != null;
                            String emailID = email.substring(0, email.indexOf("@"));
                            mRef = mRef.child(emailID);
                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users users;
                                    if (snapshot.exists()) {
                                        users = snapshot.getValue(Users.class);
                                        sessionManager.createLoginSession(users, 1);
                                    } else {
                                        users = new Users();
                                        users.setUid(emailID);
                                        users.setPwd("***");
                                        users.setFullName(userDB.getDisplayName());
                                        users.setEmail(email);
                                        users.setEmailVerify(false);
                                        users.setJoinDate(Utils.getCurrentDate());
                                        mRef.setValue(users);
                                        sessionManager.createLoginSession(users, 2);
                                    }
//                                    uid, pwd, fullName, email, gender, dateOfBirth, phoneNumber, avatar
                                    rlBar.setVisibility(View.INVISIBLE);
                                    finish();
                                    FirebaseAuth.getInstance().signOut();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }

    public void callFinish(View view) {
        finish();
    }

    public void callRegisterLG(View view) {
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

    public void callLogin(View view) {
        String[] info = checkFieldValid();
        if (info != null) {
            if (info[1].length() < 6) {
                tvError.setVisibility(View.VISIBLE);
                return;
            }
            rlBar.setVisibility(View.VISIBLE);
            mRef.child(info[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users users = snapshot.getValue(Users.class);
                    Log.d("USER", snapshot.toString());
                    if (users != null) {
                        String pwd = Objects.requireNonNull(snapshot.child("pwd").getValue()).toString();
                        if (!BCrypt.checkpw(info[1], pwd)) {
                            tvError.setVisibility(View.VISIBLE);
                        } else {
//                             uid,  pwd,  fullName,  email,  gender,  dateOfBirth,  phoneNumber,  avatar
                            sessionManager.createLoginSession(users, 1);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgGG) {
            signInWithGoogle();
        } else if (v.getId() == R.id.imgFB) {
            Toast.makeText(LoginActivity.this, "Đang lỗi, vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
//            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
//            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                @Override
//                public void onSuccess(LoginResult loginResult) {
//                    handleFacebookAccessToken(loginResult.getAccessToken());
//                    Log.d("Hihi", "onSuccess");
//                }
//
//                @Override
//                public void onCancel() {
//                }
//
//                @Override
//                public void onError(FacebookException error) {
//                }
//            });
        }
    }
    //    private void handleFacebookAccessToken(AccessToken accessToken) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        Log.d("Hihi", "credential" + accessToken.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        assert user != null;
//                        Log.d("Hihi", user.getDisplayName() + "-" + user.getPhoneNumber());
//                        Log.d("Hihi", "isSuccessful");
//                    } else {
//                        Log.d("Hihi", "Failllled");
//                    }
//                });
//    }

//    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//            if (currentAccessToken == null) {
//                LoginManager.getInstance().logOut();
//            }
//        }
//    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        accessTokenTracker.stopTracking();
//    }
}
