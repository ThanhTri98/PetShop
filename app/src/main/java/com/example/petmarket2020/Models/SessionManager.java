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
    public static final String KEY_USER = "USER";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        usersSession = context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE);
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

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
