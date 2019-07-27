package com.tewsila.client;

/**
 * Created by neo on 17/02/2017.
 */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver2 extends BroadcastReceiver {
    UserLocalStore locale;

    Vibrator v;

    PowerManager.WakeLock wakeLock;
    PowerManager pmy;

    @Override
    public void onReceive(Context context, Intent intent) {

        context = context.getApplicationContext();

        v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        pmy = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pmy.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
        locale = new UserLocalStore(context);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.tewsila)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getResources().getString(R.string.alarm));

        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        v.vibrate(2000);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alintent = new Intent(context, AlarmReceiver2.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 22, alintent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(alarmIntent);


        wakeLock.release();


    }



}