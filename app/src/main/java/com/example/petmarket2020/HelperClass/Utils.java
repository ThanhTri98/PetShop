package com.example.petmarket2020.HelperClass;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static void hiddenKeyboard(Activity context) {
        if (context.getCurrentFocus() == null) return;
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.KOREA).format(new Date());
    }
}
