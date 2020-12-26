package com.example.petmarket2020.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {
    private SharedPreferences usersSession;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_LOGIN = "login";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_UID = "uid";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_DOB = "dateOfBirth";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PHONE = "phoneNumber";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        usersSession = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String uid, String pwd, String fullName, String dob, String phone, String gender) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_PWD, pwd);
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_GENDER, gender);
        editor.apply();
    }

    public Users getUserDetail() {
        Users users = new Users();
        users.setFullName(usersSession.getString(KEY_FULLNAME, null));
        users.setDateOfBirth(usersSession.getString(KEY_DOB, null));
        users.setUid(usersSession.getString(KEY_UID, null));
        users.setPwd(usersSession.getString(KEY_PWD, null));
        users.setPhoneNumber(usersSession.getString(KEY_PHONE, null));
        users.setGender(usersSession.getString(KEY_GENDER, null));
        return users;
    }

    public boolean checkLogin() {
        return usersSession.getBoolean(IS_LOGIN, false);
    }

    public void logOut() {
        editor.clear();
        editor.apply();
    }
}
