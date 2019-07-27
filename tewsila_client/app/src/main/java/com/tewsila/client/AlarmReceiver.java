package com.tewsila.client; /**
 * Created by neo on 17/02/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;

    void scheduleAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent yourIntent = new Intent(context, AlarmReceiver.class);
        //TODO configure your intent
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, yourIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                    System.currentTimeMillis()+60*1000*5,
                    alarmIntent
            );
        }
        else{
            alarmMgr.setExact(
                    AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                    System.currentTimeMillis()+60*1000*5,
                    alarmIntent
            );


        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = context.getSharedPreferences("request", 0);
        Intent service = new Intent(context, locationservice.class);
        // Start the service, keeping the device awake while it is launching.


      context.startService(service);
        scheduleAlarm(context);





    }




}

