package com.example.petmarket2020.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

public class Utils {
    public static void HiddenKeyboard(Activity context) {
        if (context.getCurrentFocus() == null) return;
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean checkPermissionSms(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }
}
