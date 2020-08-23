package com.example.onsitetask5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AlertReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number = intent.getStringExtra("phoneNumber");
        String message = intent.getStringExtra("message");
        Log.i("reached","here");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,null,message,null,null);
        Intent intent2 = new Intent(context,AlertService.class);
        context.stopService(intent2);

    }
}
