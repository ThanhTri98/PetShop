package com.example.petmarket2020.DAL;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.petmarket2020.HelperClass.NodeRootDB;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IUsers;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.UsersModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UsersDAL {
    private static final long LIMIT_TIME_PHONE_OTP = 60L;
    private final SessionManager sessionManager;
    private final DatabaseReference mRef;
    private final StorageReference mStorageRef;
    private final FirebaseAuth mAuth;
    private final Activity activity;

    public UsersDAL(Activity activity) {
        this.activity = activity;
        sessionManager = new SessionManager(activity);
        mRef = FirebaseDatabase.getInstance().getReference(NodeRootDB.USERS);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    // verify
    public void verifyPhone(String phoneNumber, IUsers iUsers) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                LIMIT_TIME_PHONE_OTP,
                TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            iUsers.responseData(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    }
                }
        );
    }

    public void updateVerifyInfo(int type, String uid) {
//    type == 1 (phone); type == 2 (email)
        if (uid != null) {
            if (type == 1) {
                mRef.child(uid).child("phoneVerified").setValue(true);
                HashMap<String, Object> hMap = new HashMap<>();
                hMap.put(SessionManager.KEY_PHONE_VERIFY, true);
                sessionManager.updateSessionInfo(hMap);
            } else {
                mRef.child(uid).child("emailVerified").setValue(true);
                HashMap<String, Object> hMap = new HashMap<>();
                hMap.put(SessionManager.KEY_EMAIL, true);
                sessionManager.updateSessionInfo(hMap);
            }
        }
    }

    // Register
    public void registerUser(UsersModel usersModel, IUsers iUsers) {
        mRef.child(usersModel.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Tài khoản đã tồn tại
                    iUsers.isSuccessful(false);
                } else {
                    mRef.child(usersModel.getUid()).setValue(usersModel).addOnSuccessListener
                            (aVoid -> iUsers.isSuccessful(true));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // Login Google
    public void firebaseAuthWithGoogle(String idToken, IUsers iUsers) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser userDB = mAuth.getCurrentUser();
                        if (userDB != null) {
                            String email = userDB.getEmail();
                            assert email != null;
                            String emailID = email.substring(0, email.indexOf("@"));
                            mRef.child(emailID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    uid, pwd, fullName, email, gender, dateOfBirth, phoneNumber, avatar
                                    UsersModel usersModel = snapshot.getValue(UsersModel.class);
                                    if (usersModel != null) {
                                        sessionManager.createLoginSession(usersModel, 1);
                                        iUsers.isSuccessful(true);
                                    } else {
                                        usersModel = new UsersModel();
                                        usersModel.setUid(emailID);
                                        usersModel.setPwd("***");
                                        usersModel.setFullName(userDB.getDisplayName());
                                        usersModel.setEmail(email);
                                        usersModel.setEmailVerified(true);
                                        usersModel.setPhoneVerified(false);
                                        usersModel.setJoinDate(Utils.getCurrentDate(false));
                                        String urlAvatar = Objects.requireNonNull(userDB.getPhotoUrl()).toString();
                                        uploadAvatar(urlAvatar, usersModel, iUsers);
                                    }
                                    mAuth.signOut();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } else {
                        iUsers.isSuccessful(false);
                    }
                });
    }

    private void uploadAvatar(String urlAvatar, UsersModel usersModel, IUsers iUsers) {
        new Thread(() -> {
            try {
                URL url = new URL(urlAvatar);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    InputStream is = url.openStream();
                    byte[] byteChunk = new byte[1024]; // Or whatever size you want to read in at a time.
                    int n;

                    while ((n = is.read(byteChunk)) > 0) {
                        baos.write(byteChunk, 0, n);
                    }
                    String photoName = NodeRootDB.STORAGE_PROFILE + "/" + usersModel.getUid() + ".jpg";
                    usersModel.setAvatar(photoName);
                    mRef.child(usersModel.getUid()).setValue(usersModel)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    mStorageRef.child(photoName).putBytes(baos.toByteArray()).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            sessionManager.createLoginSession(usersModel, 2);
                                            iUsers.isSuccessful(true);
                                        }
                                        try {
                                            is.close();
                                            baos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            });

                } catch (IOException ignored) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void loginWithUidPwd(String uId, String pwd, IUsers iUsers) {
//        Log.e("loginWithUidPwd",uId);
        mRef.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel usersModel = snapshot.getValue(UsersModel.class);
//                Log.e("loginWithUidPwd","loginWithUidPwd2");
                if (usersModel != null) {
                    String pwd1 = Objects.requireNonNull(snapshot.child("pwd").getValue()).toString();
                    if (!BCrypt.checkpw(pwd, pwd1)) {
                        iUsers.isSuccessful(false);
                    } else {
//                       uid,  pwd,  fullName,  email,  gender,  dateOfBirth,  phoneNumber,  avatar
                        sessionManager.createLoginSession(usersModel, 1);
                        iUsers.isSuccessful(true);
                    }
                } else {
                    iUsers.isSuccessful(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // update info
    public void updateUserInfo(UsersModel usersModel, HashMap<String, Object> dataUpdate, IUsers iUsers) {
        if (dataUpdate.containsKey(SessionManager.KEY_FULLNAME))
            usersModel.setFullName((String) dataUpdate.get(SessionManager.KEY_FULLNAME));
        if (dataUpdate.containsKey(SessionManager.KEY_PHONE))
            usersModel.setPhoneNumber((String) dataUpdate.get(SessionManager.KEY_PHONE));
        if (dataUpdate.containsKey(SessionManager.KEY_EMAIL))
            usersModel.setEmail((String) dataUpdate.get(SessionManager.KEY_EMAIL));
        if (dataUpdate.containsKey(SessionManager.KEY_ADDRESS))
            usersModel.setAddress((String) dataUpdate.get(SessionManager.KEY_ADDRESS));
        if (dataUpdate.containsKey(SessionManager.KEY_AVATAR)) {
            String oldAvatar = usersModel.getAvatar();
            String newAvatar = (String) dataUpdate.remove("newAvatar");
            usersModel.setAvatar(newAvatar);
            mRef.child(usersModel.getUid()).setValue(usersModel).addOnCompleteListener(user -> {
                if (user.isSuccessful()) {
                    byte[] avatarByte = (byte[]) Objects.requireNonNull(dataUpdate.remove(SessionManager.KEY_AVATAR));
                    assert newAvatar != null;
                    mStorageRef.child(newAvatar).putBytes(avatarByte);
                    dataUpdate.put(SessionManager.KEY_AVATAR, newAvatar);
                    sessionManager.updateSessionInfo(dataUpdate);
                    iUsers.isSuccessful(true);
                    if (oldAvatar != null) {
                        mStorageRef.child(oldAvatar).delete();
                    }
                } else {
                    iUsers.isSuccessful(false);
                }
            });
        } else {
            mRef.child(usersModel.getUid()).setValue(usersModel).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    iUsers.isSuccessful(true);
                    sessionManager.updateSessionInfo(dataUpdate);
                } else {
                    iUsers.isSuccessful(false);
                }
            });
        }
    }

    public void setFavorite(String uid, List<String> favorites, IUsers iUsers) {
        mRef.child(uid).child("favorites").setValue(favorites).addOnCompleteListener(task -> {
            iUsers.isSuccessful(task.isSuccessful());
            HashMap<String, Object> values = new HashMap<>();
            values.put(SessionManager.KEY_FAVORITES, favorites);
            sessionManager.updateSessionInfo(values);
        });
    }

    //    sessionManager
    public boolean checkLogin() {
        return sessionManager.isLogin();
    }

    public void logout() {
        sessionManager.logOut();
    }

    public UsersModel getUserDetail() {
        return sessionManager.getUserDetail();
    }

    public Object getInfo(String key,boolean isHashSet) {
        return sessionManager.getInfo(key,isHashSet);
    }
}
