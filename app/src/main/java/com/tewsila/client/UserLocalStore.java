package com.tewsila.client;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tundealao on 29/03/15.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("rating", user.rating);

        userLocalDatabaseEditor.putString("name", user.name);
        userLocalDatabaseEditor.putString("username", user.username);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.putString("kind", user.kind);
        userLocalDatabaseEditor.putString("model", user.model);
        userLocalDatabaseEditor.putString("age", user.age);
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putInt("id",user.id);
        userLocalDatabaseEditor.putString("phone",user.phone);
        userLocalDatabaseEditor.putString("taxiname", user.taxiname);
        userLocalDatabaseEditor.putString("pickup", user.pickup);
        userLocalDatabaseEditor.putString("destination", user.destination);
        userLocalDatabaseEditor.putString("time", user.time);
        userLocalDatabaseEditor.putString("driverphone", user.driverphone);
        userLocalDatabaseEditor.putString("taxiid",user.taxiid);
        userLocalDatabaseEditor.putString("sex",user.sex);

        userLocalDatabaseEditor.apply();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.apply();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }
        String email = userLocalDatabase.getString("email", "");
        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String age = userLocalDatabase.getString("age","");
        int id=userLocalDatabase.getInt("id",-1);
        String kind =userLocalDatabase.getString("kind", "");
        String phone = userLocalDatabase.getString("phone","");
        String model =userLocalDatabase.getString("model", "");

        String taxiname = userLocalDatabase.getString("taxiname", "");
        String pickup = userLocalDatabase.getString("pickup", "");
        String destination = userLocalDatabase.getString("destination", "");
        String time = userLocalDatabase.getString("time", "");
        int rating = userLocalDatabase.getInt("rating",-1);
        String driverphone = userLocalDatabase.getString("driverphone", "");
            String taxiid = userLocalDatabase.getString("taxiid","-1");
        String sex = userLocalDatabase.getString("sex","male");

        User user = new User(email,kind,phone,id,name, age, username, password,model,taxiname,taxiid,pickup,destination,time,driverphone,rating,sex);
        return user;
    }
}
