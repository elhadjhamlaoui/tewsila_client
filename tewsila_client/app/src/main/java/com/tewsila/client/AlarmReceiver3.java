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
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlarmReceiver3 extends BroadcastReceiver {
    User user;
    UserLocalStore locale;

    Vibrator v;
UserLocalStore local;
    PowerManager.WakeLock wakeLock;
    PowerManager pmy;
    DatabaseReference databaseReference;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(final Context context, Intent intent) {

        local=new UserLocalStore(context);

        locale = new UserLocalStore(context);
        user = locale.getLoggedInUser();


        databaseReference= FirebaseDatabase.getInstance().getReference().child("user");

        final Context finalContext = context;
        databaseReference.child(user.phone).child("assined").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue().toString().equals("true")){

                    if (authenticate()) {
                        sharedPref=context.getSharedPreferences("prebooky",0);
                        editor=sharedPref.edit();
                        editor.putInt("number",0);
                        editor.commit();
                        v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                        pmy = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                        wakeLock = pmy.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                        wakeLock.acquire();
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.mipmap.tewsila)
                                        .setContentTitle(context.getResources().getString(R.string.app_name))
                                        .setContentText(context.getResources().getString(R.string.alarm3));

                        int mNotificationId = 800;
// Gets an instance of the NotificationManager service
                        NotificationManager mNotifyMgr =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());

                        v.vibrate(2000);
                        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent alintent = new Intent(context, AlarmReceiver3.class);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 888, alintent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmMgr.cancel(alarmIntent);
                        final Intent vukaniActivity = new Intent(context, MapsActivity.class);
                        vukaniActivity.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // I've tried multiple different flags to no avail.
                        vukaniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        vukaniActivity.putExtra("class","prebook");
                        finalContext.getApplicationContext().startActivity(vukaniActivity);

                        wakeLock.release();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private boolean authenticate() {
        if (local.getLoggedInUser() == null) {

          /*  Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();*/
            return false;
        } else
            return true;
    }


}