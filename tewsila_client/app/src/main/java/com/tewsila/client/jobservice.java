package com.tewsila.client;

/**
 * Created by neo on 04/05/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class jobservice extends JobService
{
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 5f;
    DatabaseReference databaseReference;
    UserLocalStore locale;
    User user;
    RequestQueue locationqueue;
    Handler handler;
    Runnable myRunnable;



    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {


        locale=new UserLocalStore(getApplicationContext());
        user=locale.getLoggedInUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("user");
        initializeLocationManager();




        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName = getPackageName();
        if (Build.VERSION.SDK_INT >= 23 && !pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }


        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }







            handler = new Handler();

            myRunnable = new Runnable() {
                @Override
                public void run() {
                     jobFinished(job,false);
                    handler.removeCallbacks(myRunnable);
                    if (mLocationManager != null) {
                        for (int i = 0; i < mLocationListeners.length; i++) {
                            try {
                                mLocationManager.removeUpdates(mLocationListeners[i]);
                            } catch (Exception ex) {
                              //  Log.i(TAG, "fail to remove location listners, ignore", ex);
                            }
                        }
                    }
                }
            };

            handler.postDelayed(myRunnable, 10*60*1000);



            return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        handler.removeCallbacks(myRunnable);
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                     mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {

                   }
            }
        }

        return true;
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;
        public LocationListener(String provider)
        {
             mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation.set(location);
       //    send_gps(user.id,location.getLatitude(),location.getLongitude());

            databaseReference.child(user.phone).child("loc").setValue(new loc(Double.toString(location.getLatitude()),(Double.toString(location.getLongitude()))));

        }

        @Override
        public void onProviderDisabled(String provider)
        {

           }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };







    private void initializeLocationManager() {
         if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}