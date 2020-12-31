package com.example.petmarket2020.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;


public class SessionManager {
    private final SharedPreferences usersSession;
    private final SharedPreferences.Editor editor;

    private static final String PREF_LOGIN = "login";
    private static final String IS_LOGIN = "isLogin";
    public static final String KEY_UID = "uid";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DOB = "dateOfBirth";
    public static final String KEY_COINS = "coins";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_FAVORITES = "favorites";
    public static final String KEY_VERIFY = "isEmailVerify";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_JOIN = "joinDate";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        usersSession = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    //     uid,  pwd,  fullName,  email,  gender,  dateOfBirth,  phoneNumber,  avatar
    public void createLoginSession(Users users, int acType) {
//        acType = 1 -> uid pwd; 2 -> google; 3 -> facebook
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_UID, users.getUid());
        editor.putString(KEY_PWD, users.getPwd());
        editor.putString(KEY_EMAIL, users.getEmail());
        editor.putString(KEY_FULLNAME, users.getFullName());
        editor.putBoolean(KEY_VERIFY, users.isEmailVerify());
        editor.putString(KEY_JOIN, users.getJoinDate());
        if (acType == 1) {
            editor.putString(KEY_GENDER, users.getGender());
            editor.putString(KEY_DOB, users.getDateOfBirth());
            editor.putString(KEY_COINS, users.getCoins() + "");
            editor.putString(KEY_LATITUDE, users.getLatitude() + "");
            editor.putString(KEY_LONGITUDE, users.getLongitude() + "");
            editor.putString(KEY_ADDRESS, users.getAddress());
            editor.putStringSet(KEY_FAVORITES, new HashSet<>(users.getFavorites()));
            editor.putString(KEY_PHONE, users.getPhoneNumber());
            editor.putString(KEY_AVATAR, users.getAvatar());
        }
        editor.apply();
    }

    public void setInfo(String fullName, String email, String address, String avatar) {
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_AVATAR, avatar);
        editor.apply();
    }


    public Users getUserDetail() {
        Users users = new Users();
        users.setUid(usersSession.getString(KEY_UID, null));
        users.setPwd(usersSession.getString(KEY_PWD, null));
        users.setFullName(usersSession.getString(KEY_FULLNAME, null));
        users.setEmail(usersSession.getString(KEY_EMAIL, null));
        users.setGender(usersSession.getString(KEY_GENDER, null));
        users.setDateOfBirth(usersSession.getString(KEY_DOB, null));
        users.setCoins(Double.parseDouble(usersSession.getString(KEY_COINS, "0")));
        users.setLatitude(Double.parseDouble(usersSession.getString(KEY_LATITUDE, "0")));
        users.setLongitude(Double.parseDouble(usersSession.getString(KEY_LONGITUDE, "0")));
        users.setAddress(usersSession.getString(KEY_ADDRESS, null));
        users.setFavorites(new ArrayList<>(usersSession.getStringSet(KEY_FAVORITES, new HashSet<>())));
        users.setPhoneNumber(usersSession.getString(KEY_PHONE, null));
        users.setAvatar(usersSession.getString(KEY_AVATAR, null));
        users.setEmailVerify(usersSession.getBoolean(KEY_VERIFY, false));
        users.setJoinDate(usersSession.getString(KEY_JOIN, null));
        return users;
    }

    public boolean isLogin() {
        return usersSession.getBoolean(IS_LOGIN, false);
    }

    public void logOut() {
        editor.clear();
        editor.apply();
    }
}
