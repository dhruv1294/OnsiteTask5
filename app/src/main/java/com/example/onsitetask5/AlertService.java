package com.example.onsitetask5;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static com.example.onsitetask5.MainActivity.CHANNEL_ID;

public class AlertService extends Service {
    AlarmManager alarmManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        String number="",msg="";
        int hour=0,min=0;
        Calendar c = Calendar.getInstance();
        if(intent.getExtras()!=null){
            number = intent.getStringExtra("phoneNumber");
            msg = intent.getStringExtra("message");
            hour = intent.getIntExtra("hour",0);
            min = intent.getIntExtra("min",0);
            c.set(Calendar.HOUR_OF_DAY,hour);
            c.set(Calendar.MINUTE,min);
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SMS Sender")
                .setContentText("Msg will be sent on time")
                .setSmallIcon(R.drawable.ic_mail_black_24dp)
                .setContentIntent(pendingIntent2)
                .build();
        startForeground(1, notification);

        Intent intent2 = new Intent(this, AlertReciever.class);
        intent2.putExtra("phoneNumber",number);
        intent2.putExtra("message",msg);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent2,0);
         alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);





        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intentAlarm = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, 0);
        alarmManager.cancel(pendingIntent);
    }
}
