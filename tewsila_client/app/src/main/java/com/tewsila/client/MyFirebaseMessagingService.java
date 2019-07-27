package com.tewsila.client;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.WindowManager;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by neo on 28/03/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SharedPreferences location;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.tewsila)
                .build();
     //   startForeground(14,notification);
        location=getSharedPreferences("locationupdates",0);
        editor=location.edit();
        editor.commit();

    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @SuppressLint("WrongConstant")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

   //     sendNotification("54"/*remoteMessage.getData().get("loc")*/);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
     //   Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().get("kind").equals("location"))

            {
              if (remoteMessage.getData().get("loc").equals("true")){


                  FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getBaseContext()));
                  Job myJob = dispatcher.newJobBuilder()
                          // the JobService that will be called
                          .setService(jobservice.class)
                          // uniquely identifies the job
                          .setTag("lockylocky")
                          // one-off job
                          .setRecurring(false)
                          // don't persist past a device reboot
                          .setLifetime(Lifetime.FOREVER)
                          // start between 0 and 60 seconds from now
                          .setTrigger(Trigger.executionWindow(0, 30))
                          // don't overwrite an existing job with the same tag
                          .setReplaceCurrent(true)
                          // retry with exponential backoff
                          .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                          // constraints that need to be satisfied for the job to run
                          .setConstraints(
                                  // only run on an unmetered network
                                  Constraint.ON_ANY_NETWORK
                          )

                          .build();



                  dispatcher.mustSchedule(myJob);


              }

            }
            else if (remoteMessage.getData().get("kind").equals("change")){
                UserLocalStore local = new UserLocalStore(this);



                User user = local.getLoggedInUser();
                User user2 = new User(remoteMessage.getData().get("email"),user.kind, remoteMessage.getData().get("phone"), user.id, remoteMessage.getData().get("name"), user.age, remoteMessage.getData().get("username"), user.password, user.model, user.taxiname, user.taxiid, user.pickup, user.destination, user.time, user.driverphone,user.rating,user.sex);
                local.storeUserData(user2);
                user = local.getLoggedInUser();
            }
            else if (remoteMessage.getData().get("kind").equals("arrived")){
                SharedPreferences sp=getSharedPreferences("activitystate",MODE_PRIVATE);
                if (!sp.getBoolean("active",false)) {

                    Intent vukaniActivity = new Intent(getApplicationContext(), MapsActivity.class);
                    PowerManager pmy = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wakeLock = pmy.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                    wakeLock.acquire();
                    KeyguardManager manager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                    KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
                    lock.disableKeyguard();


                    vukaniActivity.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +FLAG_ACTIVITY_NEW_TASK+ WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

                    startActivity(vukaniActivity);


                    wakeLock.release();
                }
            }

            else if (remoteMessage.getData().get("kind").equals("disable")){
                UserLocalStore local = new UserLocalStore(this);


                    local.clearUserData();
                    local.setUserLoggedIn(false);

                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();
                auth.signOut();


                MapsActivity.fa.finish();


            }
            else if (remoteMessage.getData().get("kind").equals("news")){
                Intent intent = new Intent(this, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"tewsila_news")
                        .setSmallIcon(R.mipmap.tewsila)
                        .setContentTitle("Tewsila")
                        .setContentText(remoteMessage.getData().get("message"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(1945, mBuilder.build());


            }


        }
       // sendNotification();
        // Check if message contains a data payload.


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]



}
