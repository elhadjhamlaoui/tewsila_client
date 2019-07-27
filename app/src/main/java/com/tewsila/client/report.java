package com.tewsila.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by neo on 02/06/2017.
 */

public class report extends BaseActivity{
    UserLocalStore local;
    User user;
    private Toolbar toolbar;
    TextView pickup,destination;
        EditText text;
    Button send;
    RequestQueue queue;
    int rideid;
    ProgressDialog progressDialog;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         local = new UserLocalStore(this);

         user = local.getLoggedInUser();

        setContentView(R.layout.report);
        setTitle(getResources().getString(R.string.reportactivity));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report.this.onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pickup=(TextView)findViewById(R.id.textView29);
        destination=(TextView)findViewById(R.id.textView31);
        text=(EditText)findViewById(R.id.editText13);
        send=(Button) findViewById(R.id.send);
         progressDialog = new ProgressDialog(report.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.sendingreport));
        progressDialog.setCancelable(true);

        rideid=getIntent().getIntExtra("id",0);
        pickup.setText(getIntent().getStringExtra("pickup"));
        destination.setText(getIntent().getStringExtra("destination"));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.show();
                    upload(text.getText().toString(), rideid);

                }
            }
        });


    }

    public boolean validate() {
        boolean valid = true;


        String report = text.getText().toString();



        if (report.isEmpty() || pickup.length()<3) {
            text.setError(getResources().getString(R.string.atleast));
            valid = false;
        } else {
            text.setError(null);
        }





        return valid;
    }

    public void upload(String report,int id){


        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(com.tewsila.client.report.this);
                        builder2.setMessage(getResources().getString(R.string.reportsuccess))
                                .create()
                                .show();

                        progressDialog.dismiss();

                    }
                    else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(com.tewsila.client.report.this);
                        builder2.setMessage(getResources().getString(R.string.error))
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {


                    AlertDialog.Builder builder2 = new AlertDialog.Builder(com.tewsila.client.report.this);
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                    progressDialog.dismiss();
                }
            }
        };

        report_request reportRequest = new report_request(report, id,user.kind, responseListener,ipaddress.getUrl(this));
         queue = Volley.newRequestQueue(com.tewsila.client.report.this);
        queue.add(reportRequest);
    }
}
