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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by neo on 10/02/2017.
 */
public class changepass extends BaseActivity {
    Toolbar toolbar;
    User user;
    UserLocalStore locale;
    EditText old,newpass,retype;
    Button save;
    FirebaseUser fuser ;
    AuthCredential credential;

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

        setContentView(R.layout.changepass);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepass.this.onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        old= findViewById(R.id.editText5);

        newpass= findViewById(R.id.editText9);
        retype= findViewById(R.id.editText10);

        save= findViewById(R.id.button15);
locale=new UserLocalStore(changepass.this);
     user=locale.getLoggedInUser();


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider
                .getCredential(user.email, user.password);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("pctimes",MODE_PRIVATE);
                SharedPreferences.Editor edi=sp.edit();
                edi.apply();
                edi.putInt("pctimes",sp.getInt("pctimes",0)+1);
                edi.commit();
                if (sp.getInt("pctimes",0)<4) {

                    if (validate()) {
                        final ProgressDialog progressDialog = new ProgressDialog(changepass.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage(getResources().getString(R.string.loading));
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                                    progressDialog.dismiss();
                                                    user=new User(user.email,user.kind, user.phone, user.id, user.name, user.age, user.username, newpass.getText().toString(), user.model, user.taxiname, user.taxiid, user.pickup, user.destination, user.time, user.driverphone,user.rating,user.sex);
                                                    locale.storeUserData(user);

                                                    fuser.reauthenticate(credential)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        fuser.updatePassword(newpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {

                                                                                } else {


                                                                                }
                                                                            }
                                                                        });
                                                                    } else {

                                                                    }
                                                                }
                                                            });
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(changepass.this);
                                                    builder.setMessage(getResources().getString(R.string.changedpass))
                                                            .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    finish();

                                                                }
                                                            })
                                                            .create();
                                                    builder.setCancelable(false);
                                                    builder.show();
                                    }
                                }, 3000);


                    }







                  }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(changepass.this);

                    builder.setMessage(getResources().getString(R.string.more)); // Want to enable?
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();

                }


            }
        });



    }
    public boolean validate() {
        boolean valid = true;



        String password = old.getText().toString();

        String password2 = newpass.getText().toString();
        String password3 = retype.getText().toString();


        if (!password.equals(user.password)) {
            old.setError(getResources().getString(R.string.wrong));
            valid = false;
        } else {
            old.setError(null);
        }

        if (password2.isEmpty() || password2.length() < 6 || password2.length() > 10) {
            newpass.setError(getResources().getString(R.string.between));
            valid = false;
        } else {
            newpass.setError(null);
        }


        if (!password3.equals(password2)) {
            retype.setError(getResources().getString(R.string.same));
            valid = false;
        } else {
            retype.setError(null);
        }




        return valid;
    }
}
