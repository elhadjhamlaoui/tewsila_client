package com.tewsila.client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;

/**
 * Created by neo on 16/09/2017.
 */

public class intro extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.intro);
        RelativeLayout splashIcon = (RelativeLayout) findViewById(R.id.intro);
        //animation icon at splash screen
        Animation fadeicon = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in);
        fadeicon.setDuration(2000);
        splashIcon.setAnimation(fadeicon);

        fadeicon.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //TODO: Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //TODO: Auto-generated method stub
                final DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

                reference.child("ipAddress").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        SharedPreferences sharedPreferences=getSharedPreferences("ip", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.commit();
                        editor.putString("ip",dataSnapshot.getValue().toString());
                        editor.commit();

                        Intent intent = new Intent(intro.this, splash.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //TODO: Auto-generated method stub
            }
        });

    }
}
