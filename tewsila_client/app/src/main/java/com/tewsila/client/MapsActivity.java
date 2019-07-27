package com.tewsila.client;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Communicator, GoogleMap.OnMarkerClickListener {
    String model;
    int number = 0;
    LinearLayout booklayout;
    int distancevalue;

    CircleImageView driverpicture;
    TextView drivernametext,matricule;
    String driverphone;
    Window win, win2;
    int driverrating;
    ProgressDialog fav_progress;
    int driverrides;
    CardView pickuptimelyout;
    CardView durationlayout;
    TextView ptext,dtext;
    TextView durationtextview;
    String durationy;
    Boolean vip = false;
    Marker mymarker;
    String driverstring;
    RelativeLayout service;
    CardView taxicon;
    int state=1;
    private GoogleMap mMap;
    AlarmManager alarmMgr1, alarmMgr2, alarmMgr3;
    UserLocalStore local;
    Handler handler2;
    SharedPreferences pickupanddestination, oldidstore, olduserstore;
    String pickup = "/", destination = "/";
    static double dLatitude;
    static double dLongitude;
    SharedPreferences sharedPref2, oldmode;
    double taxidistance = 0.0;
    TextView pickuptext, destinationtext;
    String mode = "//";
    MarkerOptions options, options2;
    int rating;
    private ArrayList<String> taxiidlist;

    private ArrayList<String> blacklist;

    private ArrayList<Double> durationlist;
    private ArrayList<Double> newdurationlist;
    int id = 0, seats = 0;


    Button request, prebook;
    CardView connection;
    ValueEventListener loclistener;
    String arrived = "";
    int reduction=10;

    LocationManager locationManager;
    SharedPreferences arrivy;
    SharedPreferences.Editor edity;
    RelativeLayout taxirequested;
    RelativeLayout taxiassined;
    CardView taxiassined2;
    Boolean assined = false;
    Boolean requested = false;
    HashMap<String,Marker> driverlist;
    FloatingActionButton cancel_request;
    double dtaxilat = 0.0, dtaxilang = 0.0;
    DatabaseReference con;
    User user;
    String driverid = "-1";
    Uri imageUri;
    Bitmap bitmap;
    CircleImageView picture;
    SharedPreferences.Editor userLocalDatabaseEditor;
    SharedPreferences.Editor oldidstoreeditor;
    SharedPreferences.Editor oldmodeeditor;
    Handler handler;
    SearchView searchView;
    String taxiid = "-1";
    String assinedid = "-1";
    double pickuplat, pickuplang, destinationlat, destinationlang;
    TextView carmodel, rating_textview, pickuptime,price_text;
    Runnable myRunnable;
    FloatingActionButton makecall, hidepicdes;
    Vibrator v;
    Uri ringtone;
    PowerManager pmy;
    SharedPreferences sharedPref,old_taxiid;
    GoogleApiClient mGoogleApiClient;
    Calendar calendar;
    Marker pmarker, desmarker;
    android.location.LocationListener locationlistener;

    ratingdialog ratdia;
    private DrawerLayout mDrawer;
    private PowerManager.WakeLock wl;
    UserLocalStore localStore;
    PolylineOptions lineOptions = null;
    Polyline polyline;

    ValueEventListener fblistener, fblistener3, driveridlistener;
    public static Activity fa;
    LocationManager locationmanager;
    int k = 0;
    Boolean pick_bool=false,dest_bool=false;
    DatabaseReference connectedRef;
    int bigobigo = 0;
    float bearing = 0;
    Location prevLoc, newLoc;
    private ActionBarDrawerToggle drawerToggle;
    FloatingActionButton cancel;
    FirebaseAuth auth;
    DatabaseReference databaseReference, databaseReference2;
    FirebaseDatabase db;
    int ratingold = 0, rides = 1;
    String rideid = "";
    Intent alintent1, alintent2, alintent3;
    PendingIntent alarmIntent2, alarmIntent3;
    int minDistance = 30;
    CardView pickup_layout,destination_layout;
    User olduser;
    PendingIntent alarmIntent1;
    SharedPreferences.Editor editor;
    FloatingActionButton blocation;
    FloatingActionButton bdrawer;
    FloatingActionButton taxibutton;
    RatingBar ratingbar;
    Poly poly;
    int cost = 0;
    FusedLocationProviderClient mFusedLocationClient;
    AlertDialog builder22;

    double driverlat = 0.0, driverlang = 0.0;

    protected void onStart() {
        if (locationlistener != null && locationmanager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationmanager.requestLocationUpdates("gps", 10000, 5, locationlistener);

        }
        super.onStart();
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        pmy = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pmy.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "bbb");
        wl.acquire();
        win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        String mode="economy";
        if (intent.getBooleanExtra("vip", false))
            mode="vip";
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("class") != null) {
                if (intent.getExtras().getString("class").equals("rides")){

                    ptext.setText(intent.getStringExtra("pickup"));
                    dtext.setText(intent.getStringExtra("destination"));

                    String platlng=intent.getStringExtra("platlng");
                    String dlatlng=intent.getStringExtra("dlatlng");

                    pickuplat=Double.parseDouble(platlng.split(",")[0]);
                    pickuplang=Double.parseDouble(platlng.split(",")[1]);

                    destinationlat=Double.parseDouble(dlatlng.split(",")[0]);
                    destinationlang=Double.parseDouble(dlatlng.split(",")[1]);


                    databaseReference.child(user.phone).child("pickuplat").setValue(pickuplat).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                databaseReference.child(user.phone).child("pickuplang").setValue(pickuplang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            pick_bool=true;
                                    }
                                });

                        }
                    });
                    databaseReference.child(user.phone).child("deslat").setValue(destinationlat).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                databaseReference.child(user.phone).child("deslang").setValue(destinationlang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            dest_bool=true;

                                    }
                                });

                        }
                    });
                    if (pmarker!=null&&desmarker!=null) {
                        pmarker.remove();
                        desmarker.remove();
                    }
                    options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup));
                    options2.position(new LatLng(pickuplat, pickuplang));
                    pmarker = mMap.addMarker(options2);
                    options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.selector));
                    options2.position(new LatLng(destinationlat, destinationlang));
                    desmarker = mMap.addMarker(options2);
                    if (distance(pickuplat, pickuplang, destinationlat, destinationlang) < 1000 && pickuplat != 0.0 && pickuplang != 0.0 && destinationlat != 0.0 && destinationlang != 0.0) {


                        DirectionRequest(pickuplat,pickuplang,destinationlat,destinationlang);


                    }



                }
                else if (intent.getExtras().getString("class").equals("dialog")) {

                    if (destinationlang<2)

                        databaseReference2.child("places").child("31").child(mode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double lat,lng;
                                int dis;
                                boolean found=false;
                                int red_value=0;
                                String price="0";

                                for (DataSnapshot snap : dataSnapshot.getChildren()){

                                    try {

                                        String[]loc=snap.child("loc").getValue().toString().split(",");
                                        lat=Double.parseDouble(loc[0]);
                                        lng=Double.parseDouble(loc[1]);
                                        dis=Integer.parseInt(snap.child("dis").getValue().toString());

                                        if (CalculationByDistance2(new LatLng(lat,lng),new LatLng(destinationlat,destinationlang))<dis){
                                            red_value=Integer.parseInt(snap.child("red_value").getValue().toString());
                                            price=snap.child("price").getValue().toString();
                                            found=true;
                                            break;
                                        }


                                    }catch (NumberFormatException e){


                                    }
                                }
                                estimation(intent,red_value,price,found);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    else
                        databaseReference2.child("places").child("16").child(mode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double lat,lng;
                                int dis;
                                boolean found=false;
                                int red_value=0;
                                String price="0";

                                for (DataSnapshot snap : dataSnapshot.getChildren()){

                                    try {
                                        String[]loc=snap.child("loc").getValue().toString().split(",");
                                        lat=Double.parseDouble(loc[1]);
                                        lng=Double.parseDouble(loc[0]);
                                        dis=Integer.parseInt(snap.child("dis").getValue().toString());
                                        if ((CalculationByDistance(new LatLng(lat,lng),new LatLng(destinationlat,destinationlang))*1000)<dis){
                                            red_value=Integer.parseInt(snap.child("red_value").getValue().toString());
                                            price=snap.child("price").getValue().toString();

                                            found=true;
                                            break;
                                        }



                                    }catch (NumberFormatException e){

                                    }
                                }
                                estimation(intent,red_value,price,found);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                }
                else if (intent.getExtras().getString("class").equals("prebook") && !requested && !assined) {
                    databaseReference.child(user.phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            pickup = dataSnapshot.child("prepickup").getValue().toString();
                            destination = dataSnapshot.child("predestination").getValue().toString();

                            try {
                                MapsActivity.this.seats = Integer.parseInt(dataSnapshot.child("preseats").getValue().toString());
                                MapsActivity.this.cost = Integer.parseInt(dataSnapshot.child("precost").getValue().toString());

                            } catch (NumberFormatException e) {

                            }


                           MapsActivity.this.mode = dataSnapshot.child("premode").getValue().toString();
                            databaseReference.child(user.phone).child("cost").setValue(MapsActivity.this.cost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!pickup.equals("") && !destination.equals("") && pickup != null && destination != null) {
                                        pickuptime.setVisibility(View.VISIBLE);

                                        requested = true;
                                        userLocalDatabaseEditor.putString("pickup", pickup);
                                        userLocalDatabaseEditor.putString("destination", destination);
                                        userLocalDatabaseEditor.commit();

                                        databaseReference.child(user.phone).child("mode").setValue(MapsActivity.this.mode);
                                        databaseReference.child(user.phone).child("seats").setValue(seats);
                                        databaseReference.child(user.phone).child("pickup").setValue(pickup);
                                        databaseReference.child(user.phone).child("found").setValue(dataSnapshot.child("prefound").getValue().toString());

                                        databaseReference.child(user.phone).child("credit_bool").setValue(dataSnapshot.child("pre_credit").getValue());
                                        databaseReference.child(user.phone).child("red_value").setValue(dataSnapshot.child("pre_red_value").getValue().toString());
                                        databaseReference.child(user.phone).child("price").setValue(dataSnapshot.child("pre_price").getValue().toString());
                                        databaseReference.child(user.phone).child("duration").setValue(dataSnapshot.child("pre_duration").getValue().toString());
                                        databaseReference.child(user.phone).child("key").setValue(dataSnapshot.child("pre_key").getValue().toString());

                                        SharedPreferences.Editor editor= getSharedPreferences("place_price",0).edit();
                                        editor.apply();
                                        editor.putString("price",dataSnapshot.child("pre_price").getValue().toString());
                                        editor.apply();

                                        oldmodeeditor.putString("mode", MapsActivity.this.mode);
                                        oldmodeeditor.commit();
                                        databaseReference.child(user.phone).child("destination").setValue(destination);

                                        databaseReference.child(user.phone).child("arrived").setValue("");

                                        taxirequested.setVisibility(View.VISIBLE);
                                        booklayout.setVisibility(View.GONE);
                                        service.setVisibility(View.INVISIBLE);
                                        arrange();

                                        // pgeocoder(1);

                                    }
                                }
                            });


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    // sendmail(user.email,getResources().getString(R.string.newrequestt),"<html><body>"+getResources().getString(R.string.newRequest)+"<br><br>"+pickup+"<br><br>"+destination+"</body></html>");


                }

            }


        }
        wl.release();

    }

    protected void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();

        super.onStop();
    }


    @Override
    protected void onDestroy() {

        if (handler != null)
            handler.removeCallbacks(myRunnable);


        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_maps, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.ssearch).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        // searchView.setSuggestionsAdapter(new GooglePlacesAutocompleteAdapter(MapsActivity.this, R.layout.list_item));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.maptypeHYBRID:
                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;
                }
            case R.id.maptypeNONE:
                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    return true;
                }
            case R.id.maptypeNORMAL:
                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    return true;
                }
            case R.id.maptypeSATELLITE:
                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;
                }
            case R.id.maptypeTERRAIN:
                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;
                }

        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {


        switch (menuItem.getItemId()) {


            case R.id.settings:

                Intent sintent = new Intent(MapsActivity.this, Settings.class);


                MapsActivity.this.startActivity(sintent);


                break;
            case R.id.rides:
                Intent reportintent = new Intent(MapsActivity.this, com.tewsila.client.rides.class);

                MapsActivity.this.startActivity(reportintent);

                break;
            case R.id.navshare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getResources().getString(R.string.share_text)+" https://play.google.com/store/apps/details?id="+getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
            case R.id.navdriver:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.tewsila.driver")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.tewsila.driver")));
                }

                break;
            case R.id.navlogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);


                builder.setMessage(R.string.wantlogout); // Want to enable?
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requested = false;
                        auth.signOut();
                        assined = false;
                      /*  if (alarmMgr1!=null&&alarmMgr2!=null&&alarmMgr3!=null){

                            alarmMgr1.cancel(alarmIntent1);
                            alarmMgr2.cancel(alarmIntent2);
                            alarmMgr3.cancel(alarmIntent3);

                        }*/
                        if (local != null) {
                            local.clearUserData();
                            local.setUserLoggedIn(false);
                        }

                        if (userLocalDatabaseEditor != null) {
                            userLocalDatabaseEditor.clear();
                            userLocalDatabaseEditor.commit();
                        }


                        pickup = "/";
                        destination = "/";

                        seats = 0;



                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(myRunnable);
                        Intent intent = new Intent(MapsActivity.this, splash.class);
                        MapsActivity.this.startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.create().show();

                break;


            case R.id.contact:

                final android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(this);
                builder2.setTitle(getResources().getString(R.string.contactus));
                ArrayList<String> displayValues=new ArrayList<>();
                displayValues.add(getResources().getString(R.string.email));
                displayValues.add("0560 967 967");


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,displayValues);
                builder2.setNegativeButton(getResources().getString(R.string.bcancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0)
                        {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "Support@tewsila.com", null));

                            MapsActivity.this.startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.sendemail)));
                        }
                        else if (which == 1)
                        {
                            Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0560967967"));
                            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            MapsActivity.this.startActivity(callintent);

                        }



                    }
                });


                builder2.show();


                break;
            default:


        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, null, R.string.drawer_open, R.string.drawer_close);
    }

    public boolean isNetworkAvailable() {
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) MapsActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        } catch (NullPointerException e) {

        }

        return false;
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MapsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (mMap!=null) {


                    if (poly.contains_oran(place.getLatLng())){
                        pickuplat=place.getLatLng().latitude;
                        pickuplang=place.getLatLng().longitude;
                        if (!place.getName().toString().contains("'"))
                            pickup=place.getName()+" "+place.getAddress();
                        else
                            pickup=place.getAddress().toString();

                        ptext.setText(pickup);

                        //nearby(Double.toString(pickuplat),Double.toString(pickuplang),ptext,0);

                        if (!dtext.getText().toString().equals(getResources().getString(R.string.destination)))
                            pgeocoder(0);

                        databaseReference.child(user.phone).child("pickuplat").setValue(pickuplat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    databaseReference.child(user.phone).child("pickuplang").setValue(pickuplang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                pick_bool=true;
                                        }
                                    });

                            }
                        });
                        databaseReference.child(user.phone).child("pickuplang").setValue(pickuplang);
                    }else{
                        final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                        alertDialog.setMessage(getResources().getString(R.string.nocoverage));
                        alertDialog.show();
                    }


                }


            }
        } else if (requestCode == 222) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (mMap!=null) {




                    if (poly.contains(place.getLatLng())){
                        destinationlat=place.getLatLng().latitude;
                        destinationlang=place.getLatLng().longitude;
                        if (!place.getName().toString().contains("'"))
                            destination=place.getName()+" "+place.getAddress();
                        else
                            destination=place.getAddress().toString();

                        dtext.setText(destination);
                        // nearby(Double.toString(destinationlat),Double.toString(destinationlang),dtext,1);

                        if (!ptext.getText().toString().equals(getResources().getString(R.string.pickup))) {

                            pgeocoder(0);
                        }

                        databaseReference.child(user.phone).child("deslat").setValue(destinationlat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    databaseReference.child(user.phone).child("deslang").setValue(destinationlang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                dest_bool=true;

                                        }
                                    });

                            }
                        });
                    }else{
                        final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                        alertDialog.setMessage(getResources().getString(R.string.nocoverage));
                        alertDialog.show();
                    }


                }
            }




        }
        else  if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                picture.setImageBitmap(bitmap);
                uploadImage(user.phone,bitmap);

            } catch (IOException e) {


            }
        }

        else if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);
            ContentResolver cr = getContentResolver();

            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(cr, selectedImage);

                picture.setImageBitmap(bitmap);
                uploadImage(user.phone,bitmap);

            } catch (Exception e) {



            }
        }
    }

    private void uploadImage(String phone,Bitmap bitmap){


            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            StorageReference mountainsRef = storageRef.child("images").child(phone+".jpg");


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                                   }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    downloadImage(user.phone,picture);

                }
            });

    }

    public void getDeviceImei() {
       try{
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission") String deviceid = mTelephonyManager.getDeviceId();
        SharedPreferences prefs = getSharedPreferences("com.tewsila.client", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            databaseReference.child(user.phone).child("imei").setValue(deviceid);
            prefs.edit().putBoolean("firstrun", false).commit();
        }

       }catch (NullPointerException e){
           e.printStackTrace();
       }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        local = new UserLocalStore(this);

        if (local.getLoggedInUser() != null) {
            setContentView(R.layout.activity_main_client2);
            sharedPref2 = getSharedPreferences("prebooky", 0);
            oldmode = getSharedPreferences("mode", 0);
            driverlist=new HashMap<>();
             poly = new Poly(this);
             oldmodeeditor = oldmode.edit();
            oldmodeeditor.apply();
            editor = sharedPref2.edit();
            editor.apply();
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            arrivy = getSharedPreferences("arrived", 0);
            edity = arrivy.edit();
            number = sharedPref2.getInt("number", 0);

            fa = this;


            Uri dringtone= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = Uri.parse(sharedPref.getString("ring", dringtone.toString()));
            user = local.getLoggedInUser();
            olduser = local.getLoggedInUser();


            locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);

            pmy = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pmy.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "bbb");


            if (user.kind.equals("client")) {
                oldidstore = getSharedPreferences("oldid", 0);
                oldidstoreeditor = oldidstore.edit();
                olduserstore = getSharedPreferences("dolduser", 0);
                localStore = new UserLocalStore(this);
                ratdia = new ratingdialog();
                pickupanddestination = getSharedPreferences("pad", 0);
                userLocalDatabaseEditor = pickupanddestination.edit();


                // toolbar = (Toolbar) findViewById(R.id.toolbar);
                //setSupportActionBar(toolbar);

                // Find our drawer view
                mDrawer = findViewById(R.id.drawer_layout);
                NavigationView nvDrawer = findViewById(R.id.nvView);
                // Setup drawer view


                setupDrawerContent(nvDrawer);
                drawerToggle = setupDrawerToggle();

                // Tie DrawerLayout events to the ActionBarToggle
                mDrawer.addDrawerListener(drawerToggle);
                nvDrawer.setItemIconTintList(getResources().getColorStateList(R.color.selector));
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);


                View header = nvDrawer.getHeaderView(0);
                picture = header.findViewById(R.id.imageView);
                TextView text = header.findViewById(R.id.textView18);
                text.setText(user.name);
                downloadImage(user.phone,picture);

                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MapsActivity.this);
                        builder.setTitle("Pick one");
                        ArrayList<String> displayValues=new ArrayList<>();
                        displayValues.add(getResources().getString(R.string.camera));
                        displayValues.add(getResources().getString(R.string.galery));


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,android.R.layout.simple_list_item_1,displayValues);
                        builder.setTitle("");
                        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(photo));
                                    imageUri = Uri.fromFile(photo);
                                    startActivityForResult(intent, 0);
                                    dialog.dismiss();


                                }else
                                {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                                    dialog.dismiss();
                                }
                            }
                        });


                        builder.show();
                    }
                });




            }
        }



        pickup_layout = findViewById(R.id.pickup);

        destination_layout = findViewById(R.id.destination);

        request = findViewById(R.id.login_request);
        pickuptimelyout = findViewById(R.id.pickuptimelayout);

        prebook = findViewById(R.id.button14);

        taxirequested = findViewById(R.id.relativeLayout);
        booklayout = findViewById(R.id.booklayout);
        taxiassined = findViewById(R.id.taxiassined);
        taxiassined2 = findViewById(R.id.cardy);

        cancel_request = findViewById(R.id.cancel_request);


        // fake = (FloatingActionButton) findViewById(R.id.fake);

        pickuptime = findViewById(R.id.textView13);
        price_text = findViewById(R.id.price_text);

        durationtextview = findViewById(R.id.durationtextview);
        durationlayout = findViewById(R.id.durationlayout);

        pickuptext = findViewById(R.id.pppp);


        destinationtext = findViewById(R.id.dddd);

        ptext = findViewById(R.id.pickuptext);


        dtext = findViewById(R.id.destext);

        service = findViewById(R.id.service);
        taxicon = findViewById(R.id.taxicon);
        rating_textview = findViewById(R.id.rating_text);
        blocation = findViewById(R.id.blocation);
            bdrawer = findViewById(R.id.bdrawer);
        taxibutton = findViewById(R.id.taxibutton);

        hidepicdes = findViewById(R.id.hidepicdes);
        ratingbar = findViewById(R.id.ratingBar2);

        carmodel = findViewById(R.id.carmodel);
        drivernametext = findViewById(R.id.drivername);
        matricule=findViewById(R.id.matricule);
        driverpicture = findViewById(R.id.driverpicture);

        connection = findViewById(R.id.connection);
        cancel = findViewById(R.id.cancel);
        makecall = findViewById(R.id.button7);


        v = (Vibrator) getSystemService(MapsActivity.this.VIBRATOR_SERVICE);

        //  mPlayer2= MediaPlayer.create(MapsActivity.this, R.raw.beep);
        taxiidlist = new ArrayList<>();
        blacklist = new ArrayList<>();
        durationlist = new ArrayList<>();
        newdurationlist = new ArrayList<>();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        local = new UserLocalStore(this);

        user = local.getLoggedInUser();

            if (db == null) {
                db = FirebaseDatabase.getInstance();
                // db.setPersistenceEnabled(true);
                auth = FirebaseAuth.getInstance();

                databaseReference = db.getReference().child("user");

                databaseReference2 = db.getReference();
                databaseReference.keepSynced(true);


            }

            // startService(new Intent(this, locationservice.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        11121);

            } else {
                getDeviceImei();
            }
        }else{
            getDeviceImei();

        }
            connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");


            String token = FirebaseInstanceId.getInstance().getToken();


            databaseReference.child(user.phone).child("token").setValue(token);


            // myConnectionsRef = databaseReference2.child("users").child("connections");

// stores the timestamp of my last disconnect (the last time I was seen online)
            //  final DatabaseReference lastOnlineRef = database.getReference("/users/joe/lastOnline");

            databaseReference.child(user.phone).child("canceled").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        if (dataSnapshot.getValue().toString().equals("true")) {

                            databaseReference.child(assinedid).child("loc").removeEventListener(fblistener);
                            databaseReference.child(assinedid).child("connected").removeEventListener(fblistener3);


                            final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                            alertDialog.setMessage(getResources().getString(R.string.ridecanceled));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    databaseReference.child(user.phone).child("canceled").setValue("false");

                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            pickup_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    DecimalFormat twoDForm = new DecimalFormat("#.#######");
                    double dlat1=pickuplat-0.0020000;
                    double dlat2=pickuplat+0.0020000;
                    double dlng1=pickuplang-0.0020000;
                    double dlng2=pickuplang+0.0020000;

                    try {
                        dlat1=Double.valueOf(twoDForm.format(dlat1));
                        dlat2=Double.valueOf(twoDForm.format(dlat2));
                        dlng1=Double.valueOf(twoDForm.format(dlng1));
                        dlng2=Double.valueOf(twoDForm.format(dlng2));
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }


                    if (pickuplat!=0.0)
                    builder.setLatLngBounds(new LatLngBounds(new LatLng(dlat1,dlng1),new LatLng(dlat2,dlng2)));

                    try {
                        startActivityForResult(builder.build(MapsActivity.this), 111);
                     //   ptext.setText(getResources().getString(R.string.pickup));

                    } catch (GooglePlayServicesRepairableException e) {

                    } catch (GooglePlayServicesNotAvailableException e) {

                    }
                }
            });
            destination_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    DecimalFormat twoDForm = new DecimalFormat("#.#######");
                    double dlat1=destinationlat-0.0020000;
                    double dlat2=destinationlat+0.0020000;
                    double dlng1=destinationlang-0.0020000;
                    double dlng2=destinationlang+0.0020000;

                    try{
                        dlat1=Double.valueOf(twoDForm.format(dlat1));
                        dlat2=Double.valueOf(twoDForm.format(dlat2));
                        dlng1=Double.valueOf(twoDForm.format(dlng1));
                        dlng2=Double.valueOf(twoDForm.format(dlng2));
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }


                    if (destinationlat!=0.0)
                        builder.setLatLngBounds(new LatLngBounds(new LatLng(dlat1,dlng1),new LatLng(dlat2,dlng2)));

                    try {
                        startActivityForResult(builder.build(MapsActivity.this), 222);
                       // dtext.setText(getResources().getString(R.string.destination));
                     } catch (GooglePlayServicesRepairableException e) {

                    } catch (GooglePlayServicesNotAvailableException e) {

                    }
                }
            });
            databaseReference.child(user.phone).child("rideid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null)
                        rideid = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        // add this device to my connections list
                        // this value could contain info about the device or a timestamp too
                        con = databaseReference.child(user.phone).child("connected");
                        con.setValue(Boolean.TRUE);

                        // when this device disconnects, remove it
                        con.onDisconnect().setValue(Boolean.FALSE);

                        // when I disconnect, update the last time I was seen online
                        //        lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    //  System.err.println("Listener was cancelled at .info/connected");
                }
            });


            databaseReference.child(user.phone).child("taxiid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        assinedid = dataSnapshot.getValue().toString();
                    if (assinedid.length() > 5) {

                        old_taxiid=getSharedPreferences("old_taxiid",0);
                        SharedPreferences.Editor editor=old_taxiid.edit();
                        editor.putString("taxiid",assinedid);
                        editor.apply();
                    } else {


                        if (assined) {
                            databaseReference.child(user.phone).child("assined").setValue("false");

                            requested = false;
                            assined = false;
                            taxiassined.setVisibility(View.INVISIBLE);
                            mMap.clear();
                            taxibutton.hide();

                            pickup = "/";
                            destination = "/";

                            userLocalDatabaseEditor.clear();
                            userLocalDatabaseEditor.commit();

                            seats = 0;

                            booklayout.setVisibility(View.VISIBLE);
                            service.setVisibility(View.VISIBLE);
                            dest_bool=false;
                            pick_bool=false;
                            ptext.setText(getResources().getString(R.string.pickup));
                            dtext.setText(getResources().getString(R.string.destination));


                        }

                    }


                    databaseReference.child(user.phone).child("assined").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            assined = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                            if (dataSnapshot.getValue().toString().equals("true") && assinedid.length() > 5) {


                                databaseReference.child(assinedid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child("taxiid").getValue()!=null&&dataSnapshot.child("taxiid").getValue().toString().equals(user.phone)){
                                            try{


                                                try {
                                                    rating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());

                                                    driverrides = Integer.parseInt(dataSnapshot.child("rides").getValue().toString());
                                                } catch (NumberFormatException e) {

                                                }
                                                model = dataSnapshot.child("model").getValue().toString();
                                                driverstring=dataSnapshot.child("name").getValue().toString();
                                                drivernametext.setText(driverstring);
                                                String matr=dataSnapshot.child("regnum").getValue().toString();
                                                StringBuilder result = new StringBuilder();
                                                for (int i = 0; i < matr.length(); i++) {
                                                    if (i==5) {
                                                        result.append(" ");
                                                    }
                                                    if (i==8) {
                                                        result.append(" ");
                                                    }

                                                    result.append(matr.charAt(i));
                                                }


                                                dtaxilat = Double.parseDouble(dataSnapshot.child("loc").child("lat").getValue().toString());

                                                dtaxilang = Double.parseDouble(dataSnapshot.child("loc").child("lang").getValue().toString());


                                                durationreq();


                                                matricule.setText(result.toString());
                                                requested = false;




                                                driverphone = assinedid;
                                                ratingold = rating;
                                                if (driverrides != 0) {
                                                    rides = driverrides;
                                                }
                                                NumberFormat formatter = NumberFormat.getNumberInstance();
                                                formatter.setMinimumFractionDigits(2);
                                                formatter.setMaximumFractionDigits(2);
                                                rating_textview.setText(formatter.format((float) ratingold / rides));

                                                ratingbar.setNumStars(5);
                                                ratingbar.setRating((float) ratingold / rides);
                                                ratingbar.setClickable(false);

                                                carmodel.setText(model);
                                                downloadImage(assinedid,driverpicture);
                                                taxirequested.setVisibility(View.INVISIBLE);
                                                taxibutton.show();

                                                taxiassined.setVisibility(View.VISIBLE);
                                                booklayout.setVisibility(View.GONE);
                                                service.setVisibility(View.INVISIBLE);

                                                databaseReference.child(assinedid).child("loc").addValueEventListener(fblistener);

                                                databaseReference.child(assinedid).child("connected").addValueEventListener(fblistener3);


                                                databaseReference.child(user.phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        pickup = dataSnapshot.child("pickup").getValue().toString();

                                                        destination = dataSnapshot.child("destination").getValue().toString();
                                                        try{
                                                            pickuplat=Double.parseDouble(dataSnapshot.child("pickuplat").getValue().toString());
                                                            pickuplang=Double.parseDouble(dataSnapshot.child("pickuplang").getValue().toString());
                                                            destinationlat=Double.parseDouble(dataSnapshot.child("deslat").getValue().toString());
                                                            destinationlang=Double.parseDouble(dataSnapshot.child("deslang").getValue().toString());

                                                        }catch (NullPointerException e){

                                                        }
                                                        pickuptext.setText(pickup);
                                                        destinationtext.setText(destination);
                                                        price_text.setText(dataSnapshot.child("cost").getValue().toString()+" DA");

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }catch (NullPointerException e){

                                            }catch (NumberFormatException e){

                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                            if (dataSnapshot.getValue().toString().equals("false")) {
                                taxibutton.hide();


                                taxiassined.setVisibility(View.INVISIBLE);
                                dest_bool=false;
                                pick_bool=false;
                                ptext.setText(getResources().getString(R.string.pickup));
                                dtext.setText(getResources().getString(R.string.destination));

                                bigobigo = 0;



                            }
                            if (requested) {

                                taxirequested.setVisibility(View.VISIBLE);
                                booklayout.setVisibility(View.GONE);
                                service.setVisibility(View.INVISIBLE);
                            }


                            if (dataSnapshot.getValue().toString().equals("false") && !requested) {

                                booklayout.setVisibility(View.VISIBLE);
                                service.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.child(user.phone).child("arrived").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrived = dataSnapshot.getValue().toString();
                    if (arrived.equals("begin")) {
                        cancel_request.setClickable(false);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(MapsActivity.this)
                                        .setSmallIcon(R.mipmap.tewsila)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setContentText(getResources().getString(R.string.begined));

                        int mNotificationId = 222;
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());


                        pickuptimelyout.setVisibility(View.GONE);
                        durationlayout.setVisibility(View.VISIBLE);
                        DurationRequest();
                        pgeocoder(0);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                        builder.setMessage(R.string.begined); // Want to enable?
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });
                        if (!MapsActivity.this.isFinishing()) {
                            builder.create().show();

                        }

                    } else if (arrived.equals("end")) {
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(MapsActivity.this)
                                        .setSmallIcon(R.mipmap.tewsila)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setContentText(getResources().getString(R.string.endtrip));

                        int mNotificationId = 222;
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());

                        mMap.clear();
                        edity.putInt("number2", 0);
                        edity.commit();
                        databaseReference.child(assinedid).child("loc").removeEventListener(fblistener);
                        databaseReference.child(assinedid).child("connected").removeEventListener(fblistener3);

                        databaseReference.child(user.phone).child("taxiid").setValue("-1");
                        databaseReference.child(user.phone).child("assined").setValue("false");
                        assinedid = "-1";
                        requested = false;
                        assined = false;

                        taxibutton.hide();

                        taxiassined.setVisibility(View.INVISIBLE);
                        pickup = "/";
                        destination = "/";
                        userLocalDatabaseEditor.clear();
                        userLocalDatabaseEditor.commit();
                        seats = 0;

                        booklayout.setVisibility(View.VISIBLE);
                        service.setVisibility(View.VISIBLE);
                        bill();
                        driverlist.clear();
                        cancel_request.setClickable(true);


                    } else if (arrived.equals("arrived")) {
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(MapsActivity.this)
                                        .setSmallIcon(R.mipmap.tewsila)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setContentText(getResources().getString(R.string.arrivedmessage));

                        int mNotificationId = 222;
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());

                        databaseReference2.child("arrived").child(user.phone).setValue("");
                        try {
                            pickuptimelyout.setVisibility(View.GONE);
                            win2 = getWindow();
                            win2.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                            win2.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                            arrived = "";
                            Uri currentUri = ringtone;
                            final Ringtone ringtony = RingtoneManager.getRingtone(MapsActivity.this,
                                    currentUri);
                            pgeocoder(0);



                            builder22 = new AlertDialog.Builder(MapsActivity.this).setMessage(getResources().getString(R.string.arrivedmessage))
                                    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ringtony.stop();


                                        }
                                    })
                                    .create();
                            if (!MapsActivity.this.isFinishing()) {
                                builder22.show();

                                v.vibrate(2000);

                                if (ringtony != null) {
                                    new Handler().post(new Runnable() {

                                        @Override
                                        public void run() {
                                            ringtony.play();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            // This will catch any exception, because they are all descended from Exception
                        }

                        cancel_request.setClickable(true);

                    } else {
                        pickuptimelyout.setVisibility(View.VISIBLE);
                        cancel_request.setClickable(true);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            newLoc = new Location("");
            prevLoc = new Location("");
            loclistener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //  new LongOperation().execute(dataSnapshot);
                    if (mMap!=null&&!assined){


                        MarkerOptions options;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            try {
                            if (data.child("loc").child("lat").getValue().toString().length()>4&&data.child("loc").child("lang").getValue().toString().length()>4) {
                                LatLng latLng = new LatLng(Double.parseDouble(data.child("loc").child("lat").getValue().toString()), Double.parseDouble(data.child("loc").child("lang").getValue().toString()));
                                double distance = CalculationByDistance(new LatLng(dLatitude, dLongitude), latLng);
                                if (data.getKey().length() > 5) {
                                    if (distance < 30) {
                                        if (data.child("mode").getValue().toString().equals("vip"))
                                            options = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.staximarker2)).position(latLng);
                                        else
                                            options = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.staximarker)).position(latLng);

                                        if (driverlist.containsKey(data.getKey())) {
                                            if (data.child("connected").getValue().toString().equals("true")&&data.child("available").getValue().toString().equals("true")&&!data.child("assined").getValue().toString().equals("true")) {
                                                animateMarker(driverlist.get(data.getKey()), latLng, new LatLngInterpolator.Linear());
                                                driverlist.get(data.getKey()).setRotation(Float.parseFloat(data.child("loc").child("bearing").getValue().toString()));

                                            }
                                            else {
                                                driverlist.get(data.getKey()).remove();
                                                driverlist.remove(data.getKey());
                                            }

                                        } else {

                                            if (data.child("connected").getValue().toString().equals("true")&&data.child("available").getValue().toString().equals("true")&&!data.child("assined").getValue().toString().equals("true"))

                                                driverlist.put(data.getKey(), mMap.addMarker(options));

                                        }

                                    }


                                }
                            }
                        } catch (NumberFormatException e) {


                        }
                          catch (NullPointerException e) {


                        }
                        }



                }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            fblistener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (assined) {
                        try {
                            dtaxilat = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());

                            dtaxilang = Double.parseDouble(dataSnapshot.child("lang").getValue().toString());
                            newLoc.setLatitude(dtaxilat);
                            newLoc.setLongitude(dtaxilang);

                            bearing = prevLoc.bearingTo(newLoc);

                            prevLoc.setLatitude(dtaxilat);
                            prevLoc.setLongitude(dtaxilang);


                        } catch (NumberFormatException e) {

                        }


                        if (bigobigo != 1) {
                            mMap.clear();
                            bigobigo = 1;
                            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.taximarker));
                            options.position(new LatLng(dtaxilat, dtaxilang));
                            mymarker = mMap.addMarker(options);
                            mymarker.setAnchor(0.5f, 0.5f);
                            mymarker.setFlat(true);
                            if (olduser != null) {
                                databaseReference.child(user.phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        pickup = dataSnapshot.child("pickup").getValue().toString();
                                        pickuplat=Double.parseDouble(dataSnapshot.child("pickuplat").getValue().toString());
                                        pickuplang=Double.parseDouble(dataSnapshot.child("pickuplang").getValue().toString());
                                        destinationlat=Double.parseDouble(dataSnapshot.child("deslat").getValue().toString());
                                        destinationlang=Double.parseDouble(dataSnapshot.child("deslang").getValue().toString());

                                        destination = dataSnapshot.child("destination").getValue().toString();
                                            pgeocoder(0);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                        if (mymarker != null) {
                           mymarker.setRotation(bearing);
                            animateMarker(mymarker, new LatLng(dtaxilat, dtaxilang), new LatLngInterpolator.Linear());

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };


            fblistener3 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue().toString().equals("true")) {

                        taxicon.setVisibility(View.GONE);


                    } else {


                        taxicon.setVisibility(View.VISIBLE);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };


            driveridlistener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        driverid = dataSnapshot.getValue().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            options = new MarkerOptions();
            options2 = new MarkerOptions();
            if (user.kind.equals("taxi")) {

            } else {


                request.setText(getResources().getString(R.string.request_taxi));

            }


            id = user.id;




            makecall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driverphone));
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    MapsActivity.this.startActivity(callintent);

                }
            });






            hidepicdes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taxiassined2.getVisibility() == View.VISIBLE) {
                        taxiassined2.setVisibility(View.GONE);

                        hidepicdes.setImageResource(R.drawable.showlayout);


                    } else {
                        taxiassined2.setVisibility(View.VISIBLE);
                        hidepicdes.setImageResource(R.drawable.hidelayout);


                    }


                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taxiid.length() > 5) {
                        databaseReference.child(taxiid).child("taxiid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    if (dataSnapshot.getValue().toString().equals(user.phone)) {

                                        databaseReference.child(taxiid).child("taxiid").setValue("-1");

                                        databaseReference.child(user.phone).child("taxiid").setValue("-1");
                                        databaseReference2.child("taxiid").child(taxiid).setValue("-1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });


                                    }


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                    pickup = "/";
                    destination = "/";
                    seats = 0;

                    userLocalDatabaseEditor.clear();
                    userLocalDatabaseEditor.commit();
                    taxirequested.setVisibility(View.INVISIBLE);
                    booklayout.setVisibility(View.VISIBLE);
                    service.setVisibility(View.VISIBLE);
                    requested = false;

                }
            });


        cancel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                alertDialog.setMessage(getResources().getString(R.string.sure));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.child(assinedid).child("taxiid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    if (dataSnapshot.getValue().toString().equals(user.phone)) {
                                        databaseReference.child(assinedid).child("canceled").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    cancel(taxiid,user.phone);

                                                    databaseReference.child(assinedid).child("taxiid").setValue("-1");
                                                    databaseReference.child(user.phone).child("cancel_num").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.getValue()==null)
                                                                databaseReference.child(user.phone).child("cancel_num").setValue(1);
                                                            else{
                                                            long value= (long) dataSnapshot.getValue();
                                                            dataSnapshot.getRef().setValue(value+1);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    databaseReference.child(assinedid).child("loc").removeEventListener(fblistener);
                                                    databaseReference.child(assinedid).child("connected").removeEventListener(fblistener3);
                                                    databaseReference.child(user.phone).child("arrived").setValue("");

                                                    databaseReference.child(user.phone).child("taxiid").setValue("-1");
                                                    databaseReference2.child("taxiid").child(assinedid).setValue("-1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });

                                                }
                                            }
                                        });


                                    }


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && isNetworkAvailable()&&!ptext.getText().toString().equals(getResources().getString(R.string.pickup))&&!dtext.getText().toString().equals(getResources().getString(R.string.destination))&&dest_bool&&pick_bool) {
                        newRequest();


                    }
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                        builder.setMessage(R.string.nogps); // Want to enable?
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                                MapsActivity.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                        return;
                    }
                    if (!isNetworkAvailable()) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(MapsActivity.this);
                        builder3.setMessage(getResources().getString(R.string.noconnection))
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();
                    }
                    if (ptext.getText().toString().equals(getResources().getString(R.string.pickup))||dtext.getText().toString().equals(getResources().getString(R.string.destination))||!pick_bool||!dest_bool) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(MapsActivity.this);
                        builder3.setMessage(getResources().getString(R.string.selectpd))
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();
                    }
                }
            });
            prebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    number = sharedPref2.getInt("number", 0);
                    if (!requested && !assined) {
                        if (authenticate()) {
                            if (number != 1) {


                                if (isNetworkAvailable() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&!ptext.getText().toString().equals(getResources().getString(R.string.pickup))&&!dtext.getText().toString().equals(getResources().getString(R.string.destination))) {
                                    com.tewsila.client.prebook dia2 = new prebook();

                                    Bundle args33 = new Bundle();


                                    dia2.setArguments(args33);
                                    dia2.show(getFragmentManager(), "dialog20041");
                                }

                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                                    builder.setMessage(R.string.nogps);
                                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            MapsActivity.this.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    });
                                    builder.setCancelable(false);
                                    builder.create().show();
                                    return;
                                }
                                if (!isNetworkAvailable()) {
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(MapsActivity.this);
                                    builder3.setMessage(getResources().getString(R.string.noconnection))
                                            .setNegativeButton(getResources().getString(R.string.retry), null)
                                            .create()
                                            .show();
                                }
                                if (ptext.getText().toString().equals(getResources().getString(R.string.pickup))||dtext.getText().toString().equals(getResources().getString(R.string.destination))) {
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(MapsActivity.this);
                                    builder3.setMessage(getResources().getString(R.string.selectpd))
                                            .setNegativeButton(getResources().getString(R.string.retry), null)
                                            .create()
                                            .show();
                                }


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                                builder.setMessage(R.string.allowed);
                                builder.setPositiveButton(R.string.ok, null);
                                builder.create().show();


                            }

                        }

                    }


                }
            });




    }

    @Override
    protected void onPause() {
        SharedPreferences sp = getSharedPreferences("activitystate", MODE_PRIVATE);
        SharedPreferences.Editor edi = sp.edit();
        edi.apply();
        edi.putBoolean("active", false);
        edi.commit();
        if (locationlistener != null && locationmanager != null)
            locationmanager.removeUpdates(locationlistener);
        if(assinedid.length()>5) {
            databaseReference.child(assinedid).child("loc").removeEventListener(fblistener);
            databaseReference.child(assinedid).child("connected").removeEventListener(fblistener3);
        }
        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("kind").equalTo("taxi").removeEventListener(loclistener);

        super.onPause();


    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        state=1;
        SharedPreferences sp = getSharedPreferences("activitystate", MODE_PRIVATE);
        SharedPreferences.Editor edi = sp.edit();
        edi.apply();
        edi.putBoolean("active", true);
        edi.commit();
           if(assinedid.length()>5) {
                databaseReference.child(assinedid).child("loc").addValueEventListener(fblistener);
                databaseReference.child(assinedid).child("connected").addValueEventListener(fblistener3);
            }

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("kind").equalTo("taxi").addValueEventListener(loclistener);



        bdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawer.isDrawerOpen(Gravity.START))
                    mDrawer.openDrawer(Gravity.START);
                else
                    mDrawer.closeDrawer(Gravity.END);


            }
        });
        blocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null&&mFusedLocationClient!=null) {
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        dLatitude = location.getLatitude();
                                        dLongitude = location.getLongitude();
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude),17  ));
                                    }
                                }
                            });

                }
            }
        });
        taxibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap!=null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dtaxilat, dtaxilang), 17));

            }
        });
        if (authenticate()) {


            handler = new Handler();

            myRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!isNetworkAvailable()) {


                        connection.setVisibility(View.VISIBLE);


                    }


                    if (isNetworkAvailable()) {
                        connection.setVisibility(View.GONE);


                    }




                        handler.postDelayed(this, 5000);




                }
            };

            handler.postDelayed(myRunnable, 5000);


        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings g = mMap.getUiSettings();

        g.setZoomControlsEnabled(true);
        g.setZoomGesturesEnabled(true);

        g.setMapToolbarEnabled(true);

        mMap.setOnMarkerClickListener(this);

        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
       mMap.setPadding(0,0,0,90);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }

        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
    }

    protected synchronized void buildGoogleApiClient() {




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


         locationmanager= (LocationManager) getSystemService(LOCATION_SERVICE);
         locationlistener=new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                dLatitude = location.getLatitude();
                dLongitude = location.getLongitude();

                if (state==1){state=0;
               // pickuplat=dLatitude;
               // pickuplang=dLongitude;
               // nearby(pickuplat,pickuplang,ptext);
                }
                if (assined) {
                    databaseReference.child(user.phone).child("loc").setValue(new loc(Double.toString(location.getLatitude()),(Double.toString(location.getLongitude()))));

                }


                if (arrived.equals("begin")) {


                   // durationreq4();
                    LatLng latLng2= new LatLng(dLatitude, dLongitude);
                    //zoom to current position:
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng2).zoom(17).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
         locationmanager.requestLocationUpdates("gps",10000,5,locationlistener);
         mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            dLatitude = location.getLatitude();
                            dLongitude = location.getLongitude();
                            pickuplat=dLatitude;
                            pickuplang=dLongitude;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude),17  ));
                            nearby(pickuplat,pickuplang,ptext);
                    }
                    }
                });
      /*   mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                                dLatitude = location.getLatitude();
                                dLongitude = location.getLongitude();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude),17  ));

                        }
                    }
                });


*/


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                  //  Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }

                askForPermission(Manifest.permission.CALL_PHONE,1945);

                return;
            }
            case 1945: {


                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,1946);
                return;
            }
            case 1946: {


                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,1947);
                return;
            }
            case 1947: {



                return;
            }
            case (11121):{

                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED)getDeviceImei();
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
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





    public void request_driver(final String id, final String clientid) {
        driverid=user.phone;
        databaseReference.child(id).child("taxiid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){

                    if (dataSnapshot.getValue().toString().length()<5){
                        blacklist.add(id);
                        databaseReference.child(user.phone).child("timestamp").setValue(System.currentTimeMillis()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    databaseReference2.child("taxiid").child(id).setValue(clientid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                databaseReference.child(user.phone).child("taxiid").setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            databaseReference.child(id).child("taxiid").setValue(clientid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        databaseReference.child(id).child("taxiid").addValueEventListener(driveridlistener);

                                                                        taxiid=id;
                                                                        new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {


                                                                                post(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {


                                                                                        int i=0;
                                                                                        while (i< 30 &&driverid.length()>5&& !assined && requested) {

                                                                                            try {
                                                                                                Thread.sleep(1000);
                                                                                            } catch (InterruptedException e) {


                                                                                            }
                                                                                            i++;

                                                                                        }


                                                                                        databaseReference.child(id).child("taxiid").removeEventListener(driveridlistener);

                                                                                        if (!assined && requested) {



                                                                                            databaseReference.child(id).child("taxiid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                    if (dataSnapshot.getValue()!=null){
                                                                                                        if (dataSnapshot.getValue().toString().equals(user.phone)){

                                                                                                            databaseReference.child(id).child("taxiid").setValue("-1");

                                                                                                            databaseReference.child(user.phone).child("taxiid").setValue("-1");
                                                                                                            databaseReference2.child("taxiid").child(id).setValue("-1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                }
                                                                                                            });


                                                                                                        }



                                                                                                    }



                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(DatabaseError databaseError) {
                                                                                                }
                                                                                            });

                                                                                            handler2.post(new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    get_drivers();

                                                                                                }
                                                                                            });
                                                                                        }

                                                                                    }
                                                                                });


                                                                            }


                                                                        }).start();




                                                                    }
                                                                }
                                                            });


                                                        }
                                                    }
                                                });


                                            }
                                        }
                                    });
                                }
                            }
                        });




                    }
                    else{
                        get_drivers();

                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             }
        });



    }


    public void sendrating(String comment, final int rating, final String taxiid, String phone, final int credit, final int cost, final boolean credit_bool) {

        databaseReference.child(taxiid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {if (dataSnapshot.child("rating").getValue()!=null&&dataSnapshot.child("rides").getValue()!=null) {

                    MapsActivity.this.driverrating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
                    MapsActivity.this.driverrides = Integer.parseInt(dataSnapshot.child("rides").getValue().toString());
                    databaseReference.child(taxiid).child("rating").setValue(rating + driverrating);
                    databaseReference.child(taxiid).child("rides").setValue(driverrides + 1);
                }
                } catch (NumberFormatException e){

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (oldidstoreeditor != null) {
                        oldidstoreeditor.clear();
                        oldidstoreeditor.commit();
                    }
                    assined = false;
                    arrived = "";
                    databaseReference.child(user.phone).child("assined").setValue("false");


                    databaseReference.child(user.phone).child("arrived").setValue("");
                    if (credit_bool)
                    FirebaseDatabase.getInstance().getReference().child("user").child(user.phone).child("credit").setValue(credit-cost);

                } catch (JSONException e) {


                }
            }
        };

        ratingrequest ratingrequest = new ratingrequest(comment, rating, taxiid, phone,rideid, responseListener,ipaddress.getUrl(this));
        RequestQueue queue = Volley.newRequestQueue(this);
        ratingrequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ratingrequest.setShouldCache(false);

        queue.add(ratingrequest);

    }




    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this,22);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }





    @Override
    public void change2(String date, String time, int integer, String pickup, String destination, Boolean vip,int cost,Boolean found,int red_value,String price,boolean credit,String duration,String key) {
        // sendmail(user.email,getResources().getString(R.string.newrequesttp),"<html><body>"+getResources().getString(R.string.newRequest)+"<br><br>"+pickup+"<br><br>"+destination+"<br><br>"+getResources().getString(R.string.timeanddate)+"<br><br>"+date+"<br><br>"+time+"</body></html>");
        editor.putInt("number", 1);
        editor.commit();
        String[] tokens;
        tokens = date.split("-");
        String[] tokens2;
        tokens2 = time.split("-");
        if (!pickup.equals("") && !destination.equals("") && pickup != null && destination != null) {
            alarmMgr1 = (AlarmManager) MapsActivity.this.getSystemService(Context.ALARM_SERVICE);
            pickuptime.setVisibility(View.VISIBLE);

            alintent1 = new Intent(MapsActivity.this, AlarmReceiver1.class);
            alarmIntent1 = PendingIntent.getBroadcast(MapsActivity.this, 22231, alintent1, PendingIntent.FLAG_UPDATE_CURRENT);

            databaseReference.child(user.phone).child("prepickup").setValue(pickup);
            databaseReference.child(user.phone).child("predestination").setValue(destination);
            databaseReference.child(user.phone).child("prefound").setValue(found);
            databaseReference.child(user.phone).child("pre_red_value").setValue(red_value);
            databaseReference.child(user.phone).child("pre_price").setValue(price);
            databaseReference.child(user.phone).child("pre_credit").setValue(credit);
            databaseReference.child(user.phone).child("pre_duration").setValue(duration);
            databaseReference.child(user.phone).child("pre_key").setValue(key);


            if (vip)
            databaseReference.child(user.phone).child("premode").setValue("vip");
            else
                databaseReference.child(user.phone).child("premode").setValue("economy");

            databaseReference.child(user.phone).child("preseats").setValue(integer);
            databaseReference.child(user.phone).child("precost").setValue(cost);


            alarmMgr2 = (AlarmManager) MapsActivity.this.getSystemService(Context.ALARM_SERVICE);
            alintent2 = new Intent(MapsActivity.this, AlarmReceiver2.class);
            alarmIntent2 = PendingIntent.getBroadcast(MapsActivity.this, 22, alintent2, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr3 = (AlarmManager) MapsActivity.this.getSystemService(Context.ALARM_SERVICE);
            alintent3 = new Intent(MapsActivity.this, AlarmReceiver3.class);
            alarmIntent3 = PendingIntent.getBroadcast(MapsActivity.this, 888, alintent3, PendingIntent.FLAG_UPDATE_CURRENT);


            calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            try {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tokens2[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(tokens2[1]));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokens[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(tokens[1])-1);
                calendar.set(Calendar.YEAR, Integer.parseInt(tokens[2]));
            }catch (NumberFormatException e){

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              alarmMgr1.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 1800000, alarmIntent1);//30  min

            alarmMgr2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 900000, alarmIntent2);//15 min
                alarmMgr3.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent3);//5 min

            }
            else{

              alarmMgr1.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 1800000, alarmIntent1);//30  min

             alarmMgr2.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 900000, alarmIntent2);//15 min
                alarmMgr3.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent3);//5 min


            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);


            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            Date today =calendar.getTime();

            String reportDate = df.format(today);
            builder.setMessage(R.string.prebooksuccess+" "+reportDate);
            builder.setPositiveButton(R.string.ok, null);
            builder.create().show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(newBase);
        String languageToLoad = sharedPref.getString("language", "fr"); // your language
        newLocale = new Locale(languageToLoad);

        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

    @Override
    public void change(int integer, String pickup, String destination, Boolean vip, int cost, String mody,Boolean found,int red_value,String price,boolean credit,String duration,String key) {
        MapsActivity.this.cost = cost;
        // sendmail(user.email,getResources().getString(R.string.newrequestt),"<html><body>"+getResources().getString(R.string.newRequest)+"<br><br>"+pickup+"<br><br>"+destination+"</body></html>");


            if (vip)
                mode = "vip";
            else
                mode = "economy";



        // this.vip=vip;
        this.pickup = pickup;
        this.destination = destination;
        this.seats = integer;

        if (!pickup.equals("") && !destination.equals("") && pickup != null && destination != null) {
            pickuptime.setVisibility(View.VISIBLE);

            requested = true;
            userLocalDatabaseEditor.putString("pickup", pickup);
            userLocalDatabaseEditor.putString("destination", destination);
            userLocalDatabaseEditor.commit();
            databaseReference.child(user.phone).child("mode").setValue(MapsActivity.this.mode);
            databaseReference.child(user.phone).child("seats").setValue(integer);
            databaseReference.child(user.phone).child("pickup").setValue(pickup);
            databaseReference.child(user.phone).child("cost").setValue(cost);
            databaseReference.child(user.phone).child("found").setValue(found);
            databaseReference.child(user.phone).child("red_value").setValue(red_value);
            databaseReference.child(user.phone).child("price").setValue(price);
            databaseReference.child(user.phone).child("credit_bool").setValue(credit);
            databaseReference.child(user.phone).child("duration").setValue(duration);
            databaseReference.child(user.phone).child("key").setValue(key);

           SharedPreferences.Editor editor= getSharedPreferences("place_price",0).edit();
           editor.apply();
           editor.putString("price",price);
           editor.apply();
            databaseReference.child(user.phone).child("destination").setValue(destination);

               databaseReference.child(user.phone).child("arrived").setValue("");
            pickuptimelyout.setVisibility(View.VISIBLE);
            durationlayout.setVisibility(View.GONE);
            taxirequested.setVisibility(View.VISIBLE);
            booklayout.setVisibility(View.GONE);
            service.setVisibility(View.INVISIBLE);
            arrange();

        }

    }

    @Override
    public void rating(String comment, int rating, String taxiid,int credit,int cost,boolean credit_bool) {
        sendrating(comment, rating, taxiid, user.phone,credit,cost,credit_bool);


    }


    @Override
    public void book(String date, String time) {
        LatLng desy = new LatLng(0.0, 0.0);
        if (mMap != null) {
            desy = mMap.getCameraPosition().target;
        }

        Intent pintent = new Intent(MapsActivity.this, dialog.class);
        pintent.putExtra("lat", dLatitude);
        pintent.putExtra("lang", dLongitude);
        pintent.putExtra("deslat", desy.latitude);
        pintent.putExtra("deslang", desy.longitude);
        pintent.putExtra("pickup", ptext.getText().toString());
        pintent.putExtra("destination", dtext.getText().toString());

        pintent.putExtra("plat", pickuplat);
        pintent.putExtra("plng",pickuplang);
        pintent.putExtra("dlat",destinationlat);
        pintent.putExtra("dlng", destinationlang);

        pintent.putExtra("kind", "prebook");
        pintent.putExtra("time", time);
        pintent.putExtra("date", date);
        MapsActivity.this.startActivity(pintent);


    }

    public MapsActivity() {

    }




    public void durationreq() {


        databaseReference.child(user.phone).child("duration").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null)
                    pickuptime.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void DirectionRequest(double lat,double lng,double dlat,double dlng) {

        final RequestQueue requestQueue   = Volley.newRequestQueue(this);
        Response.ErrorListener errorlistener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);


                        JSONObject objRoute = jsonObject.getJSONArray("routes").getJSONObject(0);
                        JSONObject objLegs = objRoute.getJSONArray("legs").getJSONObject(0);
                        JSONObject durationob = objLegs.getJSONObject("duration");
                        durationy = durationob.getString("text");
                        JSONObject distanceob = objLegs.getJSONObject("distance");
                       distancevalue=distanceob.getInt("value");
                       durationtextview.setText(durationy);



                           ParserTask parserTask = new ParserTask();

                           // Invokes the thread for parsing the JSON data
                           parserTask.execute(response);





                } catch (JSONException e) {

                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                requestQueue.stop();


            }

        };

        DirectionRequest request = new DirectionRequest(lat,lng,dlat,dlng,responseListener,errorlistener,ipaddress.getUrl(this));

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }
    public void DurationRequest() {

        final RequestQueue requestQueue   = Volley.newRequestQueue(this);
        Response.ErrorListener errorlistener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);


                    JSONObject objRoute = jsonObject.getJSONArray("rows").getJSONObject(0);
                    JSONObject objLegs = objRoute.getJSONArray("elements").getJSONObject(0);
                    JSONObject durationob = objLegs.getJSONObject("duration");
                    JSONObject distanceob = objLegs.getJSONObject("distance");

                    distancevalue=distanceob.getInt("value");
                    durationy = durationob.getString("text");
                    durationtextview.setText(durationy);





                } catch (JSONException e) {

                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                requestQueue.stop();


            }

        };

        DurationRequest request = new DurationRequest(dLatitude,dLongitude,destinationlat,destinationlang,responseListener,errorlistener,ipaddress.getUrl(this));

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }

    public void arrange() {
        blacklist.clear();
        databaseReference2.child("settings").child("minDistance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    minDistance=Integer.parseInt(dataSnapshot.getValue().toString());

                    databaseReference.child(user.phone).child("loc").child("lat").setValue(Double.toString(dLatitude)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            databaseReference.child(user.phone).child("loc").child("lang").setValue(Double.toString(dLongitude)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        get_drivers();

                                }
                            });

                        }
                    });

                }catch (NumberFormatException e){

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    static void post(Runnable runnable) {
        System.out.println("post!");
        runnable.run();

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));


        return Radius * c;
    }
    public double CalculationByDistance2(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult * 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));


        return meter;
    }


    public void get_drivers() {

        taxiidlist.clear();


        newdurationlist.clear();
        durationlist.clear();

        databaseReference.orderByChild("kind").equalTo("taxi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (postSnapshot.child("connected") != null && postSnapshot.child("mode").getValue() != null && postSnapshot.child("loc").child("lat").getValue() != null &&  postSnapshot.child("loc").child("lang").getValue() != null&& postSnapshot.child("available").getValue() != null) {
                        if (postSnapshot.child("disabled").getValue() != null && postSnapshot.child("assined").getValue() != null && postSnapshot.child("taxiid").getValue() != null) {

                            if (postSnapshot.child("taxiid").getValue().toString().length()<5&& !postSnapshot.child("disabled").getValue().toString().equals("true") && !postSnapshot.child("assined").getValue().toString().equals("true") && postSnapshot.child("connected").getValue().toString().equals("true") && postSnapshot.child("mode").getValue().toString().equals(mode)&& postSnapshot.child("available").getValue().toString().equals("true")) {

                                if (!blacklist.contains(postSnapshot.getKey()))
                                {

                                    try {


                                        driverlat = Double.parseDouble(postSnapshot.child("loc").child("lat").getValue().toString());
                                        driverlang = Double.parseDouble(postSnapshot.child("loc").child("lang").getValue().toString());
                                    } catch (NumberFormatException e) {


                                    }

                                taxidistance = CalculationByDistance(new LatLng(dLatitude, dLongitude), new LatLng(driverlat, driverlang));
                                if (taxidistance < minDistance) {

                                    taxiidlist.add(postSnapshot.getKey());



                                    durationlist.add(taxidistance);
                                }


                                }
                            }
                        }

                    }

                }




                if (durationlist.size() != 0) {
                    newdurationlist = new ArrayList(durationlist);
                    Collections.sort(durationlist);
                    handler2 = new Handler();
                    find_closest();


                } else  {
                    databaseReference.child(user.phone).child("taxiid").setValue("-1");

                    pickup = "/";
                    destination = "/";
                    seats = 0;
                    userLocalDatabaseEditor.clear();
                    userLocalDatabaseEditor.commit();
                    taxirequested.setVisibility(View.INVISIBLE);
                    booklayout.setVisibility(View.VISIBLE);
                    service.setVisibility(View.VISIBLE);
                    requested = false;
                    final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setMessage(getResources().getString(R.string.sorry));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                        alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             }
        });


    }

    public void find_closest() {

            k = 0;
            while (k < newdurationlist.size() && durationlist.get(0) != newdurationlist.get(k))
                k++;

            if (k < taxiidlist.size()&& durationlist.get(0) == newdurationlist.get(k))
                request_driver(taxiidlist.get(k), user.phone);

    }

    public void downloadImage(String phone, final CircleImageView v1) {
        final StorageReference pathReference = FirebaseStorage.getInstance().getReference().child("images").child(phone+".jpg");
        pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                    GlideApp.with(MapsActivity.this)
                            .load(pathReference)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(v1);

            }
        });

    }



    public void pgeocoder(final int i) {

            if (pmarker!=null&&desmarker!=null) {
                pmarker.remove();
                desmarker.remove();
            }
            options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup));
            options2.position(new LatLng(pickuplat, pickuplang));

            pmarker = mMap.addMarker(options2);
            options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.selector));
            options2.position(new LatLng(destinationlat, destinationlang));
            desmarker = mMap.addMarker(options2);
            if (distance(pickuplat, pickuplang, destinationlat, destinationlang) < 1000 && pickuplat != 0.0 && pickuplang != 0.0 && destinationlat != 0.0 && destinationlang != 0.0) {
                DirectionRequest(pickuplat, pickuplang, destinationlat, destinationlang);
            }


        }
    public void nearby(double lat, double lng, final TextView textView) {


        final RequestQueue requestQueue   = Volley.newRequestQueue(this);
        Response.ErrorListener errorlistener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);


                    String name;

                    JSONObject objRoute = jsonObject.getJSONArray("results").getJSONObject(0);


                    name=objRoute.getString("name");
                    objRoute = jsonObject.getJSONArray("results").getJSONObject(1);
                    name=name+" "+objRoute.getString("name");

                    textView.setText(name);
                    pick_bool=true;
                } catch (JSONException e) {

                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                requestQueue.stop();


            }

        };

        NearbyRequest request = new NearbyRequest(lat,lng,responseListener,errorlistener,ipaddress.getUrl(this));

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////




    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }




    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (polyline!=null)
                polyline.remove();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){

            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions!=null)

                polyline= mMap.addPolyline(lineOptions);
        }
    }
    public  void newRequest(){

        Intent dintent = new Intent(MapsActivity.this, dialog.class);
        dintent.putExtra("lat", dLatitude);
        dintent.putExtra("lang", dLongitude);

        dintent.putExtra("pickup", ptext.getText().toString());
        dintent.putExtra("destination", dtext.getText().toString());

        dintent.putExtra("kind", "book");
        LatLng des = new LatLng(0.0, 0.0);

        if (mMap != null) {
            des = mMap.getCameraPosition().target;
        }

        dintent.putExtra("plat",pickuplat);
        dintent.putExtra("plng", pickuplang);
        dintent.putExtra("dlat", destinationlat);
        dintent.putExtra("dlng", destinationlang);
        MapsActivity.this.startActivity(dintent);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    /////////////////////////////////////////////////////////////////////////////////////
    public  void estimation(Intent intent, final int red_value, final String price, final boolean found){
        final Intent intenty=intent;

        double dis_price=0;
        double time_price=0;
        String mode="economy";
        if (intenty.getBooleanExtra("vip", false))
            mode="vip";

        oldmodeeditor.putString("mode", mode);
        oldmodeeditor.commit();

        if (!price.equals("0")){

            String[] price_array=price.split(",");
         dis_price=Double.parseDouble(price_array[0]);
         time_price=Double.parseDouble(price_array[1]);
        }

        final RequestQueue requestQueue   = Volley.newRequestQueue(this);
        Response.ErrorListener errorlistener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);


                    int reduction = jsonObject.getInt("reduction");
                    int costint = jsonObject.getInt("costint");
                    String total = jsonObject.getString("total");


                    confirm dia = new confirm();


                    Bundle args = new Bundle();

                    if (intenty.getStringExtra("kind").equals("prebook")) {

                        args.putString("ptime", intenty.getStringExtra("ptime"));
                        args.putString("date", intenty.getStringExtra("date"));
                        args.putString("kind", "prebook");
                    }
                    else if (intenty.getStringExtra("kind").equals("book"))
                    {
                        args.putString("kind", "book");
                    }
                    else
                        args.putString("kind", "favorite");

                    args.putString("time", intenty.getStringExtra("time"));
                    args.putDouble("lat", dLatitude);
                    args.putDouble("lang", dLongitude);
                    args.putBoolean("found",found);
                    args.putString("price",price);

                    args.putString("destination", intenty.getStringExtra("destination"));
                    args.putInt("seats", intenty.getIntExtra("seats", 0));
                    args.putBoolean("vip", intenty.getBooleanExtra("vip", false));
                    args.putString("pickup", intenty.getStringExtra("pickup"));

                    args.putInt("reduction", red_value);
                    args.putInt("costint", costint);
                    args.putString("total", total);

                    dia.setArguments(args);
                    if(!isDestroyed() && !isFinishing())
                        dia.show(getSupportFragmentManager(), "confirm");


                } catch (JSONException e) {

                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                requestQueue.stop();


            }

        };

        pricerequest request = new pricerequest(user.phone,intenty.getLongExtra("distance", 0),mode,intenty.getLongExtra("timeint",0),dLatitude,dLongitude,dis_price,time_price,pickuplat,pickuplang,destinationlat,destinationlang, responseListener,errorlistener,ipaddress.getUrl(this));

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }


    public  void bill(){
        final RequestQueue requestQueue   = Volley.newRequestQueue(this);
         String price= getSharedPreferences("place_price",0).getString("price","0");
        double dis_price=0;
        double time_price=0;
        if (!price.equals("0")){

            String[] price_array=price.split(",");
            dis_price=Double.parseDouble(price_array[0]);
            time_price=Double.parseDouble(price_array[1]);
        }
        // Response received from the server
        Response.ErrorListener errorlistener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        final long[] time = new long[1];
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);




                    if (jsonObject !=null)
                        reduction = jsonObject.getInt("reduction");

                    old_taxiid=getSharedPreferences("old_taxiid",0);
                    final Bundle args33 = new Bundle();
                    args33.putString("taxiid", old_taxiid.getString("taxiid",""));

                    databaseReference.child(old_taxiid.getString("taxiid","")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                args33.putInt("rating", Integer.parseInt(dataSnapshot.child("rating").getValue().toString()));
                                if ( Integer.parseInt(dataSnapshot.child("rides").getValue().toString())>1)
                                    args33.putInt("rides", Integer.parseInt(dataSnapshot.child("rides").getValue().toString()));
                                else
                                    args33.putInt("rides", 1);

                                args33.putString("model", dataSnapshot.child("model").getValue().toString());
                                args33.putString("mode", dataSnapshot.child("mode").getValue().toString());
                                args33.putString("name", dataSnapshot.child("name").getValue().toString());
                                args33.putString("regnum", dataSnapshot.child("regnum").getValue().toString());

                            }catch (NumberFormatException e ){



                            }
                            catch (NullPointerException e){


                            }
                            databaseReference.child(user.phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue()!=null) {
                                        if (!isFinishing()) {
                                            try {
                                                time[0] =Long.parseLong(dataSnapshot.child("endtime").getValue().toString())-Long.parseLong(dataSnapshot.child("begintime").getValue().toString());
                                                args33.putDouble("lat", Double.parseDouble(dataSnapshot.child("deslat").getValue().toString()));
                                                args33.putDouble("lang", Double.parseDouble(dataSnapshot.child("deslang").getValue().toString()));
                                                args33.putDouble("pickuplat", Double.parseDouble(dataSnapshot.child("pickuplat").getValue().toString()));
                                                args33.putDouble("pickuplang", Double.parseDouble(dataSnapshot.child("pickuplang").getValue().toString()));
                                                args33.putBoolean("found", Boolean.parseBoolean(dataSnapshot.child("found").getValue().toString()));
                                                args33.putInt("reduction", Integer.parseInt(dataSnapshot.child("red_value").getValue().toString()));
                                                args33.putString("price", dataSnapshot.child("price").getValue().toString());

                                                args33.putInt("cost", Integer.parseInt(dataSnapshot.child("cost").getValue().toString()));
                                                args33.putString("pickup", dataSnapshot.child("pickup").getValue().toString());
                                                args33.putString("destination", dataSnapshot.child("destination").getValue().toString());

                                                args33.putLong("begintime", Long.parseLong(dataSnapshot.child("begintime").getValue().toString()));
                                                args33.putLong("endtime", Long.parseLong(dataSnapshot.child("endtime").getValue().toString()));
                                                ratdia.setArguments(args33);
                                                if(!isDestroyed() && !isFinishing())
                                                    getSupportFragmentManager().beginTransaction().add(ratdia, "rating").commitAllowingStateLoss();

                                            }
                                            catch (NumberFormatException e){



                                            }catch (NullPointerException e){


                                            }


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (JSONException e) {

                } catch (NumberFormatException e) {


                }


                requestQueue.stop();


            }

        };
        pricerequest request = new pricerequest(user.phone,0,oldmode.getString("mode","economy"),time[0],dLatitude,dLongitude,dis_price,time_price,pickuplat,pickuplang,destinationlat,destinationlang, responseListener,errorlistener,ipaddress.getUrl(this));


        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.start();
        requestQueue.add(request);

    }





    static void animateMarker(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void cancel(String clientid, String myid) {


        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                } catch (JSONException e) {


                }
            }
        };

        cancelrequest requestc = new cancelrequest(clientid, myid, rideid, responseListener,ipaddress.getUrl(this));
        RequestQueue queue8 = Volley.newRequestQueue(this);
        requestc.setShouldCache(false);
        requestc.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue8.add(requestc);

    }
}
