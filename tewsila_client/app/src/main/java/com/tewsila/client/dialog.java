package com.tewsila.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.GeoApiContext;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by neo on 21/09/2016.
 */
public class dialog extends BaseActivity implements AdapterView.OnItemClickListener {
    int seats=1;
    private TextView destinationn,pickupp;
    ProgressDialog progressDialog;
    GeoApiContext context;
    NumberPicker picker;
    static String lat="",lang="";
    String urlstring="";
    double plat;
    double plng;
    double dlat;
    double dlng;
    String durationstring;
    long durationvalue;
    long distancevalue=0;
    boolean selected=false;

    private Toolbar toolbar;
    TextView viptext,ecotext;
    RequestQueue durationrequest;
    Boolean vip=false;
    LinearLayout vip_layout,eco_layout;

    Button request ;
    String address="",address2="";
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
  public void onBackPressed() {
      Intent cintent = new Intent(dialog.this, MapsActivity.class);
      dialog.this.startActivity(cintent);

      finish();
  }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    setContentView(R.layout.dialog);
        setTitle("Tewsila");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad = sharedPref.getString("language","fr"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        picker= (NumberPicker) findViewById(R.id.numberPicker55);
        vip_layout= findViewById(R.id.vip_layout);
        eco_layout=  findViewById(R.id.eco_layout);

        viptext= findViewById(R.id.vip);
        ecotext=  findViewById(R.id.eco);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.this.onBackPressed();
            }
        });
        progressDialog = new ProgressDialog(dialog.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);


        dlat=getIntent().getDoubleExtra("dlat",0.0);
        dlng=getIntent().getDoubleExtra("dlng",0.0);
        plat=getIntent().getDoubleExtra("plat",0.0);
        plng=getIntent().getDoubleExtra("plng",0.0);

        pickupp = (TextView) findViewById(R.id.pickup);
        destinationn = (TextView) findViewById(R.id.destination);
   lat=Double.toString(getIntent().getDoubleExtra("lat", 1));
           lang=Double.toString(getIntent().getDoubleExtra("lang", 1));
        if (getIntent().getExtras()!=null&&getIntent().getExtras().getString("kind")!=null) {
            if (!getIntent().getExtras().getString("kind").equals("favorite")) {
                pickupp.setText(getIntent().getExtras().getString("pickup"));
                destinationn.setText(getIntent().getExtras().getString("destination"));

            }
            else {
                pickupp.setText(getIntent().getExtras().getString("pickup"));
                destinationn.setText(getIntent().getExtras().getString("destination"));
                if (getIntent().getExtras().getString("vip").equals("vip"))
                    this.vip=true;
                else
                    this.vip=false;
            }
        }



        eco_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ecotext.setTextColor(getResources().getColor(R.color.colorPrimary));
                viptext.setTextColor(getResources().getColor(R.color.black));
                selected=true;

                vip = false;
            }
        });

        vip_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(dialog.this);
                builder.setTitle(R.string.soon);

                builder.setMessage(R.string.soon_mes);
                builder.setPositiveButton(R.string.ok, null);
                builder.create().show();
         /*
                viptext.setTextColor(getResources().getColor(R.color.colorPrimary));
                ecotext.setTextColor(getResources().getColor(R.color.black));
                selected=true;

                vip = true;

                */
            }
        });


        request=(Button)findViewById(R.id.button2);

        picker.setMaxValue(4);
        picker.setMinValue(1);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                dialog.this.seats=i1;

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (selected){
                    request.setEnabled(false);
                    distance();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(dialog.this);

                        builder.setMessage(R.string.select_mode); // Want to enable?
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();

                    }
                }
              

            }
        });


    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }



    public boolean validate() {
        boolean valid = true;

        String pickup = pickupp.getText().toString();
        ;
        String destination = destinationn.getText().toString();

        if (pickup.isEmpty() || pickup.length() < 3) {
            pickupp.setError(getResources().getString(R.string.atleast));
            valid = false;
        } else {
            pickupp.setError(null);
        }
        if (destination.isEmpty() || destination.length() < 3) {
            destinationn.setError(getResources().getString(R.string.atleast));
            valid = false;
        } else {
            destinationn.setError(null);
        }



        



        return valid;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.cancel();
    }


    private void distance ()
    {


        progressDialog.show();


        durationrequest = Volley.newRequestQueue(this);

        Response.Listener responselistener= new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    request.setEnabled(true);

                    JSONObject response2=new JSONObject();
                    try {
                        response2 = new JSONObject(response);
                    } catch (JSONException e) {


                    }


                    try {

                        JSONObject objRoute = response2.getJSONArray("rows").getJSONObject(0);
                        JSONObject objLegs = objRoute.getJSONArray("elements").getJSONObject(0);
                        JSONObject durationob = objLegs.getJSONObject("duration");
                        JSONObject distanceob = objLegs.getJSONObject("distance");

                        distancevalue=distanceob.getLong("value");
                        durationstring = durationob.getString("text");
                        durationvalue = durationob.getLong("value");
                        progressDialog.dismiss();



                            Intent dintent = new Intent(dialog.this, MapsActivity.class);
                            dintent.putExtra("class", "dialog");


                            Bundle args33 = new Bundle();
                            if (getIntent().getStringExtra("kind").equals("prebook")) {
                                dintent.putExtra("ptime", getIntent().getStringExtra("time"));

                                dintent.putExtra("date", getIntent().getStringExtra("date"));
                                dintent.putExtra("kind", "prebook");
                            }
                            else if (getIntent().getStringExtra("kind").equals("book")) {
                                dintent.putExtra("kind", "book");
                            }
                            else if (getIntent().getStringExtra("kind").equals("favorite"))
                                dintent.putExtra("kind", "favorite");
                            dintent.putExtra("distance", distancevalue);
                            dintent.putExtra("time", durationstring);
                        dintent.putExtra("timeint", durationvalue);

                        dintent.putExtra("pickup", pickupp.getText().toString());


                        dintent.putExtra("pickup", pickupp.getText().toString());
                            dintent.putExtra("destination", destinationn.getText().toString());
                            dintent.putExtra("seats", dialog.this.seats);
                            dintent.putExtra("vip", dialog.this.vip);
                            dintent.putExtra("pickup", pickupp.getText().toString());
                            progressDialog.dismiss();

                            dialog.this.startActivity(dintent);

                            finish();



                    } catch (JSONException e) {

                        progressDialog.dismiss();

                    }


                    durationrequest.stop();


                }
            };
        Response.ErrorListener errorlistener= new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();
                    request.setEnabled(true);
                    durationrequest.stop();

                }
            };


        DurationRequest request = new DurationRequest(plat,plng,dlat,dlng,responselistener,errorlistener,ipaddress.getUrl(this));

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        durationrequest.add(request);
    }
}
