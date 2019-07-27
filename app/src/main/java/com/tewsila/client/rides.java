package com.tewsila.client;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import static android.support.v7.widget.RecyclerView.GONE;
import static android.support.v7.widget.RecyclerView.INVISIBLE;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.VISIBLE;

public class rides extends BaseActivity  {
    public static ArrayList<String> completedpickuplist;

    public static ArrayList<String> completed_platlng;
    public static ArrayList<String> completed_dlatlng;

    public static ArrayList<String> completeddestinationlist;
    public static ArrayList<String> completedtimelist;
    public static ArrayList<String> completedcostlist;
    public static ArrayList<String> completedviplist;
    public static ArrayList<Integer> completedidlist;
    public static ArrayList<String> completedname;
    public static ArrayList<String> completedphone;


    public static ArrayList<String> canceled_platlng;
    public static ArrayList<String> canceled_dlatlng;
    public static ArrayList<String> canceledpickuplist;
    public static ArrayList<String> canceleddestinationlist;

    public static ArrayList<String> canceledtimelist;
    public static ArrayList<String> canceledcostlist;
    public static ArrayList<String> canceledviplist;
    public static ArrayList<Integer> canceledidlist;
    public static ArrayList<String> canceledname;
    public static ArrayList<String> canceledphone;



    public static ArrayList<String> favoritepickuplist;
    public static ArrayList<String> favoritedestinationlist;
    public static ArrayList<String> favoritetimelist;
    public static ArrayList<String> favoritecostlist;
    public static ArrayList<String> favoriteviplist;


    public static ArrayList<String> favorit_platlng;
    public static ArrayList<String> favorit_dlatlng;





    public static Adapter canceledadapter,completedadapter,favoriteadapter;
    static int section=1;
    static ListView listview;
    static User user;
   static SQLiteDatabase db_write,db_read;
    static UserLocalStore localStore;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;
    static DatabaseReference databaseReference;
   static Boolean editButtonState=false;
    ContentValues values;
    static FloatingActionButton showremove,breturn;
   static  ViewHolder holder;
    static LocationManager locationManager;
    static FeedReaderDbHelper mDbHelper;
    @Override
    public void onBackPressed() {
        if (editButtonState&&section==1&&favoriteadapter!=null&&showremove!=null&&listview!=null) {
           if (favoritepickuplist.size()!=0) {
               editButtonState = false;
               favoriteadapter.notifyDataSetChanged();

               showremove.show();
               breturn.hide();

           //    listview.setBackgroundColor(getResources().getColor(R.color.white));


           }
           else  {

               breturn.hide();
               editButtonState = false;
               favoriteadapter.notifyDataSetChanged();
           }


        }
            else
                super.onBackPressed();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras()!=null){
            if(intent.getExtras().getString("class").equals("newfavorite")){
                favoritepickuplist.add(intent.getExtras().getString("pickup"));

                favoritedestinationlist.add(intent.getExtras().getString("destination"));
                favoriteviplist.add(intent.getExtras().getString("mode"));
                favoriteadapter.notifyDataSetChanged();

                values.put(FeedReaderContract.FeedEntry.pickup, intent.getExtras().getString("pickup"));
                values.put(FeedReaderContract.FeedEntry.destination, intent.getExtras().getString("destination"));
                values.put(FeedReaderContract.FeedEntry.platlng, intent.getExtras().getString("platlng"));
                values.put(FeedReaderContract.FeedEntry.dlatlng, intent.getExtras().getString("dlatlng"));
                values.put(FeedReaderContract.FeedEntry.vip, intent.getExtras().getString("mode"));

                if (section==1)
                showremove.show();
                editButtonState=false;

                long newRowId = db_write.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

            }

        }
    }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rideslayout);
        setTitle(getResources().getString(R.string.ridestitle));
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                rides.this.onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

          holder=new ViewHolder();



        localStore=new UserLocalStore(rides.this);
        user=localStore.getLoggedInUser();

         mDbHelper = new FeedReaderDbHelper(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
         tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

     completedpickuplist=new ArrayList<>();
        completeddestinationlist=new ArrayList<>();
         completedtimelist=new ArrayList<>();
        completedcostlist=new ArrayList<>();
        completedviplist=new ArrayList<>();
        completedidlist=new ArrayList<>();
        completedname=new ArrayList<>();
        completedphone=new ArrayList<>();
        completed_platlng=new ArrayList<>();
        completed_dlatlng=new ArrayList<>();

         canceledpickuplist=new ArrayList<>();
        canceleddestinationlist=new ArrayList<>();
        canceledtimelist=new ArrayList<>();
        canceledcostlist=new ArrayList<>();
        canceledviplist=new ArrayList<>();
        canceledidlist=new ArrayList<>();
        canceledname=new ArrayList<>();
        canceledphone=new ArrayList<>();
        canceled_platlng=new ArrayList<>();
        canceled_dlatlng=new ArrayList<>();

        favoritedestinationlist=new ArrayList<>();
        favoritetimelist=new ArrayList<>();
        favoritepickuplist=new ArrayList<>();
        favoritecostlist=new ArrayList<>();
        favoriteviplist=new ArrayList<>();

        favorit_platlng=new ArrayList<>();
        favorit_dlatlng=new ArrayList<>();

        // Gets the data repository in write mode
         db_write = mDbHelper.getWritableDatabase();
        db_read=mDbHelper.getReadableDatabase();
// Create a new map of values, where column names are the keys
         values = new ContentValues();





        databaseReference= FirebaseDatabase.getInstance().getReference().child("user");

// Insert the new row, returning the primary key value of the new row


    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
        section=position+1;
            if (section==1&&favoritepickuplist.size()!=0&&!editButtonState)
                showremove.show();
           }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });




// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.pickup,
                FeedReaderContract.FeedEntry.destination,FeedReaderContract.FeedEntry.vip,
                FeedReaderContract.FeedEntry.destination,FeedReaderContract.FeedEntry.platlng,
                FeedReaderContract.FeedEntry.destination,FeedReaderContract.FeedEntry.dlatlng

        };

// Filter results WHERE "title" = 'My Title'

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry._ID + " ASC";

        Cursor cursor = db_read.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        if (cursor.moveToFirst()){
            do{
                try
                {
                    String pic = cursor.getString(
                            cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.pickup));
                    favoritepickuplist.add(pic);
                    String des = cursor.getString(
                            cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.destination));
                    favoritedestinationlist.add(des);
                    String vip = cursor.getString(
                            cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.vip));
                    favoriteviplist.add(vip);
                    String platlng = cursor.getString(
                            cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.platlng));
                    favorit_platlng.add(platlng);
                    String dlatlng = cursor.getString(
                            cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.dlatlng));

                    favorit_dlatlng.add(dlatlng);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }


            }while(cursor.moveToNext());
        }
        cursor.close();


     }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AdapterView.OnItemClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */


        FloatingActionButton fab2;

        SwipeRefreshLayout refreshLayout;


        private static final String ARG_SECTION_NUMBER = "section_number";



        public PlaceholderFragment() {


        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
           // section=sectionNumber;

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {





        }
        public  boolean isNetworkAvailable(){
            final ConnectivityManager connectivityManager = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 final Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.ridesfragment, container, false);
             listview= rootView.findViewById(R.id.listview);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                     if (section == 1) {
                         AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                         String[] genders = {getContext().getResources().getString(R.string.request_taxi)};
                         builder.setItems(genders, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 switch (which) {
                                     case 0:

                                         if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true&&isNetworkAvailable()==true){

                                             databaseReference.child(user.phone).child("assined").addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                                     if (!dataSnapshot.getValue().toString().equals("true")) {

                                                         Intent dintent = new Intent(getContext(), MapsActivity.class);

                                                         dintent.putExtra("class", "rides");
                                                         dintent.putExtra("pickup", favoritepickuplist.get(position));
                                                         dintent.putExtra("destination", favoritedestinationlist.get(position));
                                                         dintent.putExtra("platlng", favorit_platlng.get(position));
                                                         dintent.putExtra("dlatlng", favorit_dlatlng.get(position));
                                                         dintent.putExtra("vip", favoriteviplist.get(position));


                                                         getContext().startActivity(dintent);
                                                     }
                                                 }

                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }
                                             });

                                         }
                                         if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false ) {

                                             android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

                                             builder.setMessage(R.string.nogps); // Want to enable?
                                             builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialogInterface, int i) {

                                                     getContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                                 }
                                             });
                                             builder.setCancelable(false);
                                             builder.create().show();
                                             return;
                                         }
                                         if (isNetworkAvailable()==false){
                                             android.app.AlertDialog.Builder builder3 = new android.app.AlertDialog.Builder(getContext());
                                             builder3.setMessage(getResources().getString(R.string.noconnection))
                                                     .setNegativeButton(getResources().getString(R.string.retry), null)
                                                     .create()
                                                     .show();
                                         }


                                         break;

                                 }
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



                    } else if (section==2){

                         AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                         String[] genders = {getContext().getResources().getString(R.string.addtofavorite),getContext().getResources().getString(R.string.reportactivity)};
                         builder.setItems(genders, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 if (which==0){
                                     ContentValues values2 = new ContentValues();

                                     favoritepickuplist.add(completedpickuplist.get(position));

                                     favoritedestinationlist.add(completeddestinationlist.get(position));
                                     favoriteviplist.add(completedviplist.get(position));

                                     favorit_platlng.add(completed_platlng.get(position));
                                     favorit_dlatlng.add(completed_dlatlng.get(position));

                                     favoriteadapter.notifyDataSetChanged();

                                     values2.put(FeedReaderContract.FeedEntry.pickup, completedpickuplist.get(position));
                                     values2.put(FeedReaderContract.FeedEntry.destination, completeddestinationlist.get(position));
                                     values2.put(FeedReaderContract.FeedEntry.platlng, completed_platlng.get(position));
                                     values2.put(FeedReaderContract.FeedEntry.dlatlng, completed_dlatlng.get(position));

                                     values2.put(FeedReaderContract.FeedEntry.vip, completedviplist.get(position));

                                     db_write.beginTransaction();
                                     db_write.insertOrThrow(FeedReaderContract.FeedEntry.TABLE_NAME, null, values2);
                                     db_write.setTransactionSuccessful();
                                     db_write.endTransaction();


                                     AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                                     builderInner.setMessage(getContext().getResources().getString(R.string.done));

                                     builderInner.setPositiveButton(getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog,int which) {
                                             dialog.dismiss();
                                         }
                                     });
                                     builderInner.show();


                                 }else if (which==1){
                                     Intent reportintent=new Intent(getContext(),report.class);
                                     reportintent.putExtra("pickup", completedpickuplist.get(position));
                                     reportintent.putExtra("destination", completeddestinationlist.get(position));
                                     reportintent.putExtra("id", completedidlist.get(position));
                                     reportintent.putExtra("name", completedname.get(position));
                                     reportintent.putExtra("phone", completedphone.get(position));

                                     getContext().startActivity(reportintent);





                                 }
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
                     else if (section==3){
                         AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                         String[] genders = {getContext().getResources().getString(R.string.reportactivity)};
                         builder.setItems(genders, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 Intent reportintent=new Intent(getContext(),report.class);
                                 reportintent.putExtra("pickup", canceledpickuplist.get(position));
                                 reportintent.putExtra("destination", canceleddestinationlist.get(position));
                                 reportintent.putExtra("id", canceledidlist.get(position));
                                 reportintent.putExtra("name", canceledname.get(position));
                                 reportintent.putExtra("phone", canceledphone.get(position));

                                 getContext().startActivity(reportintent);

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
            });



      // section=getArguments().getInt(ARG_SECTION_NUMBER);


if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
    //listview.setOnItemClickListener(this);
    editButtonState=false;
    showremove= rootView.findViewById(R.id.showremove);
    breturn= rootView.findViewById(R.id.breturn);
    refreshLayout = rootView.findViewById(R.id.swiperefresh);
    refreshLayout.setActivated(false);
    refreshLayout.setEnabled(false);

    if (favoritepickuplist.size()!=0){
         showremove.show();
    }


  favoriteadapter= new Adapter(getContext(), favoritepickuplist, favoritedestinationlist,favoritetimelist,favoritecostlist,favoriteviplist,null,null,"fav");


    listview.setAdapter(favoriteadapter);
    breturn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (favoritepickuplist.size()!=0) {
                editButtonState = false;
                favoriteadapter.notifyDataSetChanged();

               showremove.show();
                breturn.hide();

                //    listview.setBackgroundColor(getResources().getColor(R.color.white));


            }
            else  {

                breturn.hide();

                editButtonState = false;
                favoriteadapter.notifyDataSetChanged();
            }
        }
    });
    showremove.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            editButtonState = true;
            favoriteadapter.notifyDataSetChanged();
            showremove.hide();

            breturn.show();
            //listview.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    });
 //   adapter = new Adapter(getContext(), completedpickuplist, completeddestinationlist,completedtimelist);
  //  listview.setAdapter(adapter);

}

else if (getArguments().getInt(ARG_SECTION_NUMBER)==3){

    refreshLayout = rootView.findViewById(R.id.swiperefresh);
    listview.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int topRowVerticalPosition =
                    (listview == null || listview.getChildCount() == 0) ?
                            0 : listview.getChildAt(0).getTop();
            refreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
        }
    });

    refreshLayout = rootView.findViewById(R.id.swiperefresh);
    refreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {


                    upload(section);

                }
            }
    );
    canceledadapter = new Adapter(getContext(), canceledpickuplist, canceleddestinationlist,canceledtimelist,canceledcostlist,canceledviplist,canceledname,canceledphone,"can");
    listview.setAdapter(canceledadapter);
    fab2= rootView.findViewById(R.id.fab2);
     fab2.show();
    upload(2);
    upload(3);

    fab2.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
                 /*   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                          .setAction("Action", null).show();*/
            fab2.hide();

            upload(section);


        }
    });

}
    else {
    refreshLayout = rootView.findViewById(R.id.swiperefresh);
    listview.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int topRowVerticalPosition =
                    (listview == null || listview.getChildCount() == 0) ?
                            0 : listview.getChildAt(0).getTop();
            refreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
        }
    });

    refreshLayout = rootView.findViewById(R.id.swiperefresh);
    refreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {


                    upload(section);

                }
            }
    );
    completedadapter = new Adapter(getContext(), completedpickuplist, completeddestinationlist,completedtimelist,completedcostlist,completedviplist,completedname,completedphone,"com");
    listview.setAdapter(completedadapter);
    fab2= rootView.findViewById(R.id.fab2);
     fab2.show();
    fab2.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
                 /*   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
            fab2.hide();
            upload(section);


        }
    });

    }
            return rootView;
        }
        public  void upload(final int number){

            // Response received from the server
            Response.ErrorListener errorlistener= new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    refreshLayout.setRefreshing(false);
                    fab2.show();

                }
            };
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JSONArray arrayResponse = null;
                    try {
                        arrayResponse = new JSONArray("");
                    } catch (JSONException e) {

                    }
                    try {
                         arrayResponse = new JSONArray(response);
                    } catch (JSONException e) {

                    }
                    int count = 0;
                    if (number==2) {
                        completedpickuplist.clear();
                        completeddestinationlist.clear();
                        completed_platlng.clear();
                        completed_dlatlng.clear();
                        completedtimelist.clear();
                        completedcostlist.clear();
                        completedviplist.clear();
                        completedidlist.clear();
                        completedname.clear();
                        completedphone.clear();

                    }
                    else if (number==3){
                        canceledpickuplist.clear();
                        canceleddestinationlist.clear();

                        canceled_platlng.clear();
                        canceled_dlatlng.clear();

                        canceledtimelist.clear();
                        canceledcostlist.clear();
                        canceledviplist.clear();
                        canceledidlist.clear();
                        canceledname.clear();
                        canceledphone.clear();


                    }

                    if (arrayResponse!=null) {


                        while (count < arrayResponse.length()) {
                            try {


                                JSONObject json = arrayResponse.getJSONObject(count);

                                String pickuplocation = json.getString("pickup");
                                String destinationlocation = json.getString("destination");
                                String platlng = json.getString("platlng");
                                String dlatlng = json.getString("dlatlng");
                                String time = json.getString("date");
                                String cost = json.getString("cost");
                                String vip = json.getString("mode");
                                String phone = json.getString("phone");

                                String name = json.getString("name");

                                int id = json.getInt("id");
                                if (number == 2) {
                                    completedpickuplist.add(pickuplocation);
                                    completeddestinationlist.add(destinationlocation);

                                    completed_platlng.add(platlng);
                                    completed_dlatlng.add(dlatlng);

                                    completedtimelist.add(time);
                                    completedcostlist.add(cost);
                                    completedviplist.add(vip);
                                    completedidlist.add(id);
                                    completedname.add(name);
                                    completedphone.add(phone);

                                    completedadapter.notifyDataSetChanged();
                                } else if (number == 3) {
                                    canceledpickuplist.add(pickuplocation);
                                    canceleddestinationlist.add(destinationlocation);

                                    canceled_platlng.add(platlng);
                                    canceled_dlatlng.add(dlatlng);

                                    canceledtimelist.add(time);
                                    canceledcostlist.add(cost);
                                    canceledviplist.add(vip);
                                    canceledidlist.add(id);
                                    canceledname.add(name);
                                    canceledphone.add(phone);

                                    canceledadapter.notifyDataSetChanged();
                                }

                                count++;

                            } catch (JSONException e) {


                            }


                        }
                    }
                    refreshLayout.setRefreshing(false);
                    fab2.show();

                }

            };

            ridesrequest loginRequest = new ridesrequest(user.phone, user.kind,number, responseListener,errorlistener,ipaddress.getUrl(getContext()));
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(loginRequest);

        }



    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.favorite);
                case 1:
                    return  getResources().getString(R.string.completed);
                case 2:
                    return  getResources().getString(R.string.canceled);
            }
            return null;
        }
    }
    static class Adapter extends ArrayAdapter<String> {
        Context context;
        String tab;
        ArrayList<String> pickup=new ArrayList<>();
        ArrayList<String> costlist=new ArrayList<>();
        ArrayList<String> viplist=new ArrayList<>();
        ArrayList<String> timelist=new ArrayList<>();
        ArrayList<String> destination=new ArrayList<>();
        ArrayList<String> name=new ArrayList<>();
        ArrayList<String> phone=new ArrayList<>();

        StorageReference storageReference;
        Adapter(Context c,ArrayList<String> pickup,ArrayList<String> destination,ArrayList<String> timelist,ArrayList<String> costlist,ArrayList<String> viplist,ArrayList<String> name,ArrayList<String> phone,String tab) {
            super(c,R.layout.ridesraw,R.id.pickuptext,pickup);
            this.context=c;
            this.tab=tab;
            this.pickup=pickup;
            this.timelist=timelist;
            this.destination=destination;
            this.costlist=costlist;
            this.phone=phone;

            this.viplist=viplist;
            this.name=name;


        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View raw=convertView;


            if(raw==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                raw = inflater.inflate(R.layout.ridesraw, parent, false);

            }
             holder = new ViewHolder();

            holder.pickup2= raw.findViewById(R.id.pickuptext);
            holder.cost= raw.findViewById(R.id.cost);
            holder.time= raw.findViewById(R.id.timetext);
            holder.remove= raw.findViewById(R.id.bremove);
            holder.viptext= raw.findViewById(R.id.viptext);
            holder.name= raw.findViewById(R.id.name);
            holder.picture= raw.findViewById(R.id.picture);

            holder.destination2= raw.findViewById(R.id.destinationtext);

            if (!tab.equals("fav")) {
                holder.time.setVisibility(VISIBLE);

                holder.remove.hide();
                holder.cost.setVisibility(VISIBLE);
                holder.name.setVisibility(VISIBLE);
                holder.picture.setVisibility(VISIBLE);


            }
            else {
                holder.cost.setVisibility(GONE);
                holder.time.setVisibility(GONE);
                holder.name.setVisibility(GONE);
                holder.picture.setVisibility(GONE);




                if(editButtonState)
                {

                    holder.remove.show();
                    holder.remove.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String selection = FeedReaderContract.FeedEntry.platlng + " LIKE ? AND "+FeedReaderContract.FeedEntry.dlatlng+" LIKE ?";

                            String[] selectionArgs = {favorit_platlng.get(position),favorit_dlatlng.get(position) };
                            db_write.beginTransaction();
                            db_write.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
                            db_write.setTransactionSuccessful();

                            db_write.endTransaction();

                            favoritepickuplist.remove(position);
                            favoritedestinationlist.remove(position);
                            favoriteviplist.remove(position);

                            favorit_platlng.remove(position);
                            favorit_dlatlng.remove(position);

                            favoriteadapter.notifyDataSetChanged();
                            if (favoritepickuplist.size()==0) {

                                showremove.hide();
                                editButtonState=false;


                                breturn.hide();
                            }
                        }
                    });
                    notifyDataSetChanged();

                }
                else

                holder.remove.hide();

            }

            if(convertView!=null){
                convertView.setTag(holder);}

            if(position<pickup.size()&&position<destination.size()) {

                holder.pickup2.setText(pickup.get(position));
                holder.destination2.setText(destination.get(position));

                if (viplist.get(position).equals("vip"))
                    holder.viptext.setVisibility(VISIBLE);


                else
                    holder.viptext.setVisibility(INVISIBLE);

                if (!tab.equals("fav")) {
                    holder.time.setText(timelist.get(position));
                    holder.cost.setText(costlist.get(position)+" DA");
                    holder.name.setText(name.get(position));

                    storageReference = FirebaseStorage.getInstance().getReference().child("images/"+phone.get(position)+".jpg");
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()&&is_app_Finishing((Activity)getContext()))
                                GlideApp.with(getContext())
                                        .load(storageReference)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(holder.picture);
                        }
                    });


                }

            }


            return raw;

        }

    }
    public static boolean is_app_Finishing(Activity context){
        return !context.isFinishing();
    }
    static class ViewHolder {
        TextView pickup2;
        FloatingActionButton remove;
        TextView destination2;
        TextView cost;
        TextView viptext;
        TextView time;
        TextView name;
        ImageView picture;


    }

}
