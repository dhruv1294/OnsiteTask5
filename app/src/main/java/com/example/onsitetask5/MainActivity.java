package com.example.onsitetask5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private EditText phoneNumberEditText,messageEditText;
    private Button selectTimeButton,cancelMessageButton;
    private TextView msgInfo;
    public static final String CHANNEL_ID = "exampleServiceChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        }else{
            Log.i("permission","granted");
        }
        msgInfo = findViewById(R.id.msgInfo);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        messageEditText = findViewById(R.id.message);
        selectTimeButton = findViewById(R.id.timePickerButton);
        cancelMessageButton = findViewById(R.id.scheduleMessageButton);
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumberEditText.getText().toString();
                String message = messageEditText.getText().toString();
                if(TextUtils.isEmpty(number)||TextUtils.isEmpty(message)){
                    Toast.makeText(MainActivity.this, "Enter the data first", Toast.LENGTH_SHORT).show();
                }else {


                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        });
        cancelMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSchedule();
            }
        });
    }
    public void cancelSchedule(){
        /*AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReciever.class);
        intent.putExtra("phoneNumber",phoneNumberEditText.getText().toString());
        intent.putExtra("message",messageEditText.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);*/
        Intent intent = new Intent(this,AlertService.class);
        stopService(intent);
        Toast.makeText(MainActivity.this, "Cancelled successfully", Toast.LENGTH_SHORT).show();
        msgInfo.setText("To:(no body)\n Msg:(no message)\n Time:(no Time)");

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String number = phoneNumberEditText.getText().toString();
        String message = messageEditText.getText().toString();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        msgInfo.setText("To: "+number+"\n" +"msg: "+message+"\n"+"Time: "+ DateFormat.format("hh:mm aa",c));
        scheduleMessage(c);


    }

    public void scheduleMessage(Calendar c){
      //  AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertService.class);
        intent.putExtra("phoneNumber",phoneNumberEditText.getText().toString());
        intent.putExtra("message",messageEditText.getText().toString());
        intent.putExtra("hour",c.get(Calendar.HOUR_OF_DAY));
        intent.putExtra("min",c.get(Calendar.MINUTE));
        ContextCompat.startForegroundService(this,intent);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
      //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        Toast.makeText(MainActivity.this, "Scheduled successfully", Toast.LENGTH_SHORT).show();
        phoneNumberEditText.setText("");
        messageEditText.setText("");

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    private void createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }
}
