package com.tewsila.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by neo on 30/01/2017.
 */
public class restore extends BaseActivity {
    private Button send;
   private EditText etemail,etphone;
    private String code;
   private Toolbar toolbar;
    ProgressDialog progressDialog;
    private String email;
    private String phone;

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
        setContentView(R.layout.restore);
        setTitle(getResources().getString(R.string.recover));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restore.this.onBackPressed();
            }
        });

        send= (Button) findViewById(R.id.button17);
        etemail= (EditText) findViewById(R.id.editText7);
        etphone= (EditText) findViewById(R.id.editText8);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=etemail.getText().toString();
                phone=etphone.getText().toString();

                SharedPreferences sp=getSharedPreferences("prtimes",MODE_PRIVATE);
                SharedPreferences.Editor edi=sp.edit();
                edi.apply();
                edi.putInt("prtimes",sp.getInt("prstimes",0)+1);
                edi.commit();
                if (sp.getInt("prtimes",0)<4) {


                    if (validate()) {
                        checkrestore();
                    }

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(restore.this);

                    builder.setMessage(getResources().getString(R.string.more2)); // Want to enable?
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();

                }


            }
        });
    }
    public void checkrestore(){
        RequestQueue queue;







        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success=false;
                    success = jsonResponse.getBoolean("success");
                    if (success) {
                          progressDialog = new ProgressDialog(restore.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage(getResources().getString(R.string.sending));
                        progressDialog.show();




                        // TODO: Implement your own signup logic here.

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        reqpass(email);

                                    }
                                }, 3000);


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(restore.this);
                        builder.setMessage(getResources().getString(R.string.noemail))
                                .setNegativeButton(getResources().getString(R.string.retry),null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {


                    AlertDialog.Builder builder3 = new AlertDialog.Builder(restore.this);
                    builder3.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
            }
        };


        checkrequest2 registerRequest = new checkrequest2(phone,email, responseListener,ipaddress.getUrl(this));
        queue = Volley.newRequestQueue(restore.this);
        queue.add(registerRequest);
    }
    public boolean validate() {
        boolean valid = true;






        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError(getResources().getString(R.string.valideemail));
            valid = false;
        } else {
            etemail.setError(null);
        }

        if (phone.isEmpty() || phone.length()!=10||!Patterns.PHONE.matcher(phone).matches()) {
            etphone.setError(getResources().getString(R.string.validenumber));
            valid = false;
        } else {
            etphone.setError(null);
        }


        return valid;
    }

    public void reqpass(String email){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(restore.this);
                            builder.setMessage(getResources().getString(R.string.emailsent))
                                    .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .create();
                            builder.setCancelable(false);
                            if (!restore.this.isFinishing())
                            builder.show();
                        }
                    }
                });






    }


}
