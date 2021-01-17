package com.example.petmarket2020.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class SessionManager {
    private final SharedPreferences usersSession;
    private final SharedPreferences.Editor editor;
    public static final String KEY_USER = "USER";
    public static final String KEY_POST_MANAGE_COUNT = "KEY_POST_MANAGE_COUNT";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        usersSession = context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createOrUpdateUserSession(UsersModel usersModel) {
        editor.putString(KEY_USER, new Gson().toJson(usersModel));
        editor.apply();
    }

    public void createOrUpdatePostOfUserSession(PostManageItemCount postManageItemCount) {
        editor.putString(KEY_POST_MANAGE_COUNT, new Gson().toJson(postManageItemCount));
        editor.apply();
    }

    public PostManageItemCount getPostOfUserSession() {
        if (usersSession.getString(KEY_POST_MANAGE_COUNT, null) == null)
            return null;
        else
            return new Gson().fromJson(usersSession.getString(KEY_POST_MANAGE_COUNT, null), PostManageItemCount.class);
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

    public static class PostManageItemCount {
        private int sellingCount;
        private int hiddenCount;
        private int refuseCount;
        private int waitingCount;

        public PostManageItemCount() {
        }

        public int getSellingCount() {
            return sellingCount;
        }

        public void setSellingCount(int sellingCount) {
            this.sellingCount = sellingCount;
        }

        public int getHiddenCount() {
            return hiddenCount;
        }

        public void setHiddenCount(int hiddenCount) {
            this.hiddenCount = hiddenCount;
        }

        public int getRefuseCount() {
            return refuseCount;
        }

        public void setRefuseCount(int refuseCount) {
            this.refuseCount = refuseCount;
        }

        public int getWaitingCount() {
            return waitingCount;
        }

        public void setWaitingCount(int waitingCount) {
            this.waitingCount = waitingCount;
        }
    }
}
