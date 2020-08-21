package com.example.onsitetask5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number = intent.getStringExtra("phoneNumber");
        String message = intent.getStringExtra("message");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,null,message,null,null);

    }
}
