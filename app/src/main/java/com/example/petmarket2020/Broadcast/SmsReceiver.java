package com.example.petmarket2020.Broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.EditText;

import com.example.petmarket2020.Views.Register2ndActivity;

public class SmsReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String pdu_type = "pdus";
    private static final String OriginatingAddress = "PhoneCode";

    public static EditText[] codes;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdu = (Object[]) bundle.get(pdu_type);
                final SmsMessage[] smsMessage = new SmsMessage[pdu.length];
                if (smsMessage.length > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        smsMessage[0] = SmsMessage.createFromPdu((byte[]) pdu[0], format);
                    } else {
                        smsMessage[0] = SmsMessage.createFromPdu((byte[]) pdu[0]);
                    }
                    if (OriginatingAddress.equals(smsMessage[0].getOriginatingAddress())) {
                        String sms = smsMessage[0].getMessageBody();
                        String code = sms.substring(sms.length() - 6);
                        Register2ndActivity.codeResponse = code;
                        if (codes != null)
                            autoFill(code);
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void autoFill(String code) {
        int len = codes.length;
        for (int i = 0; i < len; i++) {
            codes[i].setText(code.charAt(i) + "");
        }
    }
}
