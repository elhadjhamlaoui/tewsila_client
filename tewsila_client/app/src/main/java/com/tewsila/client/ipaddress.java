package com.tewsila.client;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by neo on 11/07/2017.
 */

public class ipaddress extends Application {
    static SharedPreferences sharedPreferences;
    public static String getUrl(Context context){
        sharedPreferences=context.getSharedPreferences("ip",Context.MODE_PRIVATE);
        return sharedPreferences.getString("ip","http://18.216.46.49/");
    }}
