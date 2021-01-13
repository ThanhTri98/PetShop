package com.example.petmarket2020.HelperClass;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public static void hiddenKeyboard(Activity context) {
        if (context.getCurrentFocus() == null) return;
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getCurrentDate(boolean isTimeMil) {
        if (isTimeMil)
            return new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.KOREA).format(new Date());
        return new SimpleDateFormat("dd/MM/yyyy", Locale.KOREA).format(new Date());
    }

    public static String formatCurrencyVN(double price) {
        return currencyVN.format(price).substring(2) + " đ";
    }
}
