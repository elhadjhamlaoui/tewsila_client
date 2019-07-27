package com.tewsila.client;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import io.fabric.sdk.android.services.common.SafeToast;

/**
 * Created by neo on 10/02/2017.
 */
public class Settings extends BaseActivity implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
   Uri ringtone;
    static ListView listView;
    SharedPreferences sharedPref;
    SharedPreferences.Editor rEditor;
    User user;
    String[] string;
    DatePickerDialog datePickerDialog;
    ArrayList<String> textlist;
    String gender="";
    UserLocalStore localStore;
    static Boolean fb=false;
    Adapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(newBase);
        String languageToLoad = sharedPref.getString("language", "fr"); // your language
        newLocale = new Locale(languageToLoad);

        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user=localStore.getLoggedInUser();
        gender=user.sex;
        string= new String[]{getResources().getString(R.string.language),getResources().getString(R.string.email)+": "+user.email,getResources().getString(R.string.phone)+": "+user.phone,getResources().getString(R.string.name)+": "+user.name,getResources().getString(R.string.birthdate)+": "+user.age,getResources().getString(R.string.gender)+": "+user.sex};

        textlist=new ArrayList<>();

        for (String text:string){

            textlist.add(text);

        }
        adapter=new Adapter(Settings.this,textlist);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        for(UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()){

            if(user.getProviderId().equals("facebook.com")){
                fb=true;
            }

            if(user.getProviderId().equals("google.com")){
                //logged with google

            }


        }
         sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
         rEditor = sharedPref.edit();

        localStore=new UserLocalStore(Settings.this);
        user=localStore.getLoggedInUser();

         datePickerDialog = new DatePickerDialog(
                this, Settings.this, 1900, 0, 1);

        String[] string={getResources().getString(R.string.language),getResources().getString(R.string.email)+": "+user.email,getResources().getString(R.string.phone)+": "+user.phone,getResources().getString(R.string.name)+": "+user.name,getResources().getString(R.string.birthdate)+": "+user.age,getResources().getString(R.string.gender)+": "+user.sex};

        setTitle(getResources().getString(R.string.title_activity_settings));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.this.onBackPressed();
            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){

        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);

      textlist=new ArrayList<>();

        for (String text:string){

            textlist.add(text);

        }



        listView= (ListView) findViewById(R.id.listView5);
        adapter=new Adapter(Settings.this,textlist);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }
    public void change(final String age,final String sex){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(Settings.this);
                builder2.setMessage(getResources().getString(R.string.error))
                        .setNegativeButton(getResources().getString(R.string.retry), null)
                        .create()
                        .show();
            }
        };
        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    user = new User(user.email, user.kind, user.phone, user.id, user.name, age, user.username, user.password, user.model, user.taxiname, user.taxiid, user.pickup, user.destination, user.time, user.driverphone, user.rating,sex);
                    localStore.storeUserData(user);
                  String[] string={getResources().getString(R.string.language),getResources().getString(R.string.email)+": "+user.email,getResources().getString(R.string.phone)+": "+user.phone,getResources().getString(R.string.name)+": "+user.name,getResources().getString(R.string.birthdate)+": "+user.age,getResources().getString(R.string.gender)+": "+user.sex};


                    textlist=new ArrayList<>();

                    for (String text:string){

                        textlist.add(text);

                    }
                    adapter=new Adapter(Settings.this,textlist);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {

                    android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(Settings.this);
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
            }
        };

        editRequest edit = new editRequest(user.phone,user.name,user.email,age,gender, responseListener,errorListener,ipaddress.getUrl(this));
        RequestQueue queue = Volley.newRequestQueue(Settings.this);
        queue.add(edit);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

       if (i==0){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
            builder.setTitle(getResources().getString(R.string.language));
            ArrayList<String> displayValues=new ArrayList<>();
            displayValues.add(getResources().getString(R.string.english));
            displayValues.add(getResources().getString(R.string.french));
            displayValues.add(getResources().getString(R.string.arabic));


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.this,android.R.layout.simple_list_item_1,displayValues);
            builder.setNegativeButton(getResources().getString(R.string.bcancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (which == 0)
                    {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(Settings.this);
                        builderInner.setMessage(getResources().getString(R.string.confirmlang));
                        builderInner.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rEditor.putString("language","en");
                                rEditor.commit();
                                Intent mStartActivity = new Intent(Settings.this, MapsActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(Settings.this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager) Settings.this.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);

                            }
                        });
                        builderInner.setCancelable(false);
                        builderInner.show();

                    }
                    else if (which == 1)
                    {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(Settings.this);
                        builderInner.setMessage(getResources().getString(R.string.confirmlang));
                        builderInner.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rEditor.putString("language","fr");
                                rEditor.commit();
                                Intent mStartActivity = new Intent(Settings.this, MapsActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(Settings.this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager) Settings.this.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);

                            }
                        });
                        builderInner.setCancelable(false);
                        builderInner.show();

                    }
                    else if (which == 2)
                    {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(Settings.this);
                        builderInner.setMessage(getResources().getString(R.string.confirmlang));
                        builderInner.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                rEditor.putString("language","ar");
                                rEditor.commit();
                                Intent mStartActivity = new Intent(Settings.this, MapsActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(Settings.this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager) Settings.this.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);

                            }
                        });
                        builderInner.setCancelable(false);
                        builderInner.show();

                    }


                }
            });


            builder.show();

        }
        else if (i==3)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
            ArrayList<String> displayValues=new ArrayList<>();
            displayValues.add(getResources().getString(R.string.change));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.this,android.R.layout.simple_list_item_1,displayValues);
            builder.setNegativeButton(getResources().getString(R.string.bcancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (which == 0)
                    {
                       Intent intent=new Intent(Settings.this,editActivity.class);
                       startActivity(intent);

                    }
                    dialog.dismiss();




                }
            });


            builder.show();


        }
        else if (i==4){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
            ArrayList<String> displayValues=new ArrayList<>();
            displayValues.add(getResources().getString(R.string.change));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.this,android.R.layout.simple_list_item_1,displayValues);
            builder.setNegativeButton(getResources().getString(R.string.bcancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (which == 0)
                    {
                        datePickerDialog.show();


                    }
                    dialog.dismiss();




                }
            });


            builder.show();


        }else if (i==5){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setTitle(getResources().getString(R.string.gender));

                    String[] genders = {getResources().getString(R.string.male), getResources().getString(R.string.female)};
                    builder.setItems(genders, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:

                                    gender="male";
                                    dialog.dismiss();

                                    break;
                                case 1:
                                    gender="female";
                                    dialog.dismiss();


                                    break;

                            }
                            change(user.age,gender);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    rEditor.putString("ring", ringtone.toString());
                    rEditor.commit();
                    // Toast.makeText(getBaseContext(),RingtoneManager.URI_COLUMN_INDEX,
                    // Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    change(Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year),gender);
    }


    static class Adapter extends ArrayAdapter<String> {
        Context context;

        ArrayList<String> text=new ArrayList<>();

        Adapter(Context c,ArrayList<String> text) {
            super(c,R.layout.settingsraw,R.id.settingstext,text);
            this.context=c;
            this.text=text;

        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View raw=convertView;


            if(raw==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                raw = inflater.inflate(R.layout.settingsraw, parent, false);

            }


            ViewHolder holder = new ViewHolder();

            holder.text=(TextView) raw.findViewById(R.id.settingstext);



            if(convertView!=null){
                convertView.setTag(holder);}

            if(position<text.size()) {
                holder.text.setText(text.get(position));

                if ((position==2)&&listView.getChildAt(position)!=null) {


                    listView.getChildAt(position).setEnabled(false);
                    TextView texy=(TextView)listView.getChildAt(position).findViewById(R.id.settingstext);
                    texy.setTextColor(ContextCompat.getColor(context,R.color.place_autocomplete_search_hint));

                }


            }


            return raw;

        }

    }
    static class ViewHolder {
        TextView text;


    }
}
