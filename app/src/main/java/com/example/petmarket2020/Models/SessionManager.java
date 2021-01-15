package com.example.petmarket2020.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


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
    public static final String KEY_EMAIL_VERIFY = "isEmailVerified";
    public static final String KEY_PHONE_VERIFY = "isPhoneVerified";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_JOIN = "joinDate";

    public static final String KEY_USER = "USER";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        usersSession = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createOrUpdateUserSession(UsersModel usersModel) {
        editor.putString(KEY_USER, new Gson().toJson(usersModel));
        editor.apply();
    }

    public UsersModel getUserSession() {
        if (usersSession.getString(KEY_USER, null) == null)
            return null;
        else
            return new Gson().fromJson(usersSession.getString(KEY_USER, null), UsersModel.class);
    }

    public boolean userIsExists() {
        return getUserSession() != null;
    }

    //     uid,  pwd,  fullName,  email,  gender,  dateOfBirth,  phoneNumber,  avatar
    public void createLoginSession(UsersModel usersModel, int acType) {
//        acType = 1 -> uid pwd; 2 -> google; 3 -> facebook
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_UID, usersModel.getUid());
        editor.putString(KEY_PWD, usersModel.getPwd());
        editor.putString(KEY_EMAIL, usersModel.getEmail());
        editor.putString(KEY_PHONE, usersModel.getPhoneNumber());
        editor.putString(KEY_FULLNAME, usersModel.getFullName());
        editor.putBoolean(KEY_EMAIL_VERIFY, usersModel.isEmailVerified());
        editor.putBoolean(KEY_PHONE_VERIFY, usersModel.isPhoneVerified());
        editor.putString(KEY_JOIN, usersModel.getJoinDate());
        editor.putString(KEY_AVATAR, usersModel.getAvatar());
        if (acType == 1) {
            editor.putString(KEY_GENDER, usersModel.getGender());
            editor.putString(KEY_DOB, usersModel.getDateOfBirth());
            editor.putString(KEY_COINS, String.valueOf(usersModel.getCoins()));
            editor.putString(KEY_LATITUDE, usersModel.getLatitude() + "");
            editor.putString(KEY_LONGITUDE, usersModel.getLongitude() + "");
            editor.putString(KEY_ADDRESS, usersModel.getAddress());
            if (usersModel.getFavorites() != null)
                editor.putStringSet(KEY_FAVORITES, new HashSet<>(usersModel.getFavorites()));
        }
        editor.apply();
    }

    public void updateSessionInfo(@NonNull HashMap<String, Object> values) {
        for (Map.Entry<String, Object> val : values.entrySet()) {
            if (val.getValue() instanceof String) {
//                Log.d("SESSION80", "String ne");
                editor.putString(val.getKey(), (String) val.getValue());
            } else if (val.getValue() instanceof Boolean) {
//                Log.d("SESSION85", "boolean ne");
                editor.putBoolean(val.getKey(), (boolean) val.getValue());
            } else {
                List<String> favorites = (List<String>) val.getValue();
                editor.putStringSet(KEY_FAVORITES, new HashSet<>(favorites));
            }
        }
        editor.apply();
    }

    public Object getInfo(String key, boolean isHashSet) {
        if (!isHashSet)
            return usersSession.getString(key, null);
        return usersSession.getStringSet(key, null);
    }

    public UsersModel getUserDetail() {
        UsersModel usersModel = new UsersModel();
        usersModel.setUid(usersSession.getString(KEY_UID, null));
        usersModel.setPwd(usersSession.getString(KEY_PWD, null));
        usersModel.setFullName(usersSession.getString(KEY_FULLNAME, null));
        usersModel.setEmail(usersSession.getString(KEY_EMAIL, null));
        usersModel.setGender(usersSession.getString(KEY_GENDER, null));
        usersModel.setDateOfBirth(usersSession.getString(KEY_DOB, null));
        usersModel.setCoins(Long.parseLong(usersSession.getString(KEY_COINS, "0")));
        usersModel.setLatitude(Double.parseDouble(usersSession.getString(KEY_LATITUDE, "0")));
        usersModel.setLongitude(Double.parseDouble(usersSession.getString(KEY_LONGITUDE, "0")));
        usersModel.setAddress(usersSession.getString(KEY_ADDRESS, null));
        if (usersSession.getStringSet(KEY_FAVORITES, null) != null)
            usersModel.setFavorites(new ArrayList<>(usersSession.getStringSet(KEY_FAVORITES, new HashSet<>())));
        usersModel.setPhoneNumber(usersSession.getString(KEY_PHONE, null));
        usersModel.setAvatar(usersSession.getString(KEY_AVATAR, null));
        usersModel.setEmailVerified(usersSession.getBoolean(KEY_EMAIL_VERIFY, false));
        usersModel.setPhoneVerified(usersSession.getBoolean(KEY_PHONE_VERIFY, false));
        usersModel.setJoinDate(usersSession.getString(KEY_JOIN, null));
        return usersModel;
    }

    public boolean isLogin() {
        return usersSession.getBoolean(IS_LOGIN, false);
    }

    public void logOut() {
        editor.clear();
        editor.apply();
    }
}
