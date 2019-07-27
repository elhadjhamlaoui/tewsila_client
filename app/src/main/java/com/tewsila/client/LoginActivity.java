package com.tewsila.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LoginActivity extends BaseActivity {

    UserLocalStore localStore;
    RequestQueue queue;
    UserLocalStore local;
    EditText iphone ;
     EditText etPassword ;
    private Toolbar toolbar;
    ProgressDialog progressDialog;
    Button bLogin;


    // Highlight the selected item has been done by NavigationView

        // Close the navigation drawer





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
        local = new UserLocalStore(this);


        setContentView(R.layout.activity_login2);
        setTitle(getResources().getString(R.string.Logintitle));




        localStore=new UserLocalStore(this);
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

         iphone = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView forget = (TextView) findViewById(R.id.tvRegisterLink);
          bLogin = (Button) findViewById(R.id.bSignIn);


       // mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

       // nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
      //  setupDrawerContent(nvDrawer);
        //drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
       // mDrawer.addDrawerListener(drawerToggle);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.onBackPressed();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resIntent = new Intent(LoginActivity.this, restore.class);
                LoginActivity.this.startActivity(resIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();


            }
        });
    }


    public boolean validate() {
            boolean valid = true;


            String mobile = iphone.getText().toString();
            String password = etPassword.getText().toString();


            if (mobile.isEmpty() || mobile.length()!=10) {
                iphone.setError(getResources().getString(R.string.validenumber));
                valid = false;
            } else {
                iphone.setError(null);
            }
           if (password.isEmpty() || password.length()<6) {
               etPassword.setError(getResources().getString(R.string.between));
            valid = false;
        } else {
               etPassword.setError(null);
        }





        return valid;
    }
    public void login() {


        if (!validate()) {
            onSignupFailed();
            return;
        }

        bLogin.setEnabled(false);

          progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.signing));
        progressDialog.setCancelable(false);
        progressDialog.show();


        String mobile = iphone.getText().toString();
        String password = etPassword.getText().toString();


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                       /* final String phonenumber = iphone.getText().toString();
                        final String password = etPassword.getText().toString();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword()*/

                        upload();



                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        bLogin.setEnabled(true);
        setResult(RESULT_OK, null);

    }

    public void onSignupFailed() {

        bLogin.setEnabled(true);
    }
    public void upload(){
        final String phonenumber = iphone.getText().toString();
        final String password = etPassword.getText().toString();

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
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

                    if (success) {

                        if (jsonResponse.getString("kind").equals("client")) {
                            final String name = jsonResponse.getString("name");
                            final  String age = jsonResponse.getString("age");
                            final  int id = jsonResponse.getInt("user_id");
                            final String phone = jsonResponse.getString("phone");
                            final String kind = jsonResponse.getString("kind");
                            final  String email = jsonResponse.getString("email");
                            final  String username = jsonResponse.getString("username");
                            final String model = jsonResponse.getString("model");
                            final String sex = jsonResponse.getString("sex");

                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        User user = new User(email,kind, phone, id, name, age, username, password, model, "","-1", "", "", "", "",0,sex);
                                        localStore.storeUserData(user);
                                        localStore.setUserLoggedIn(true);
                                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                        progressDialog.dismiss();

                                        LoginActivity.this.startActivity(intent);


                                        finish();

                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage(getResources().getString(R.string.loginfailed))
                                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                                .create()
                                                .show();
                                        progressDialog.dismiss();


                                    }
                                }
                            });


                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(getResources().getString(R.string.loginfailed))
                                    .setNegativeButton(getResources().getString(R.string.retry), null)
                                    .create()
                                    .show();
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(getResources().getString(R.string.loginfailed))
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                    progressDialog.dismiss();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(phonenumber, responseListener,errorListener,ipaddress.getUrl(this));
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }
}
