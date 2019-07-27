package com.tewsila.client;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.facebook.accountkit.ui.SkinManager.Skin.CLASSIC;


/**
 * Created by neo on 12/09/2016.
 */
public class extras extends BaseActivity implements ageinterface {

    String kind="client";

    String code="";
    int i1=0;
    int position=1;

    String phone;
  FirebaseAuth auth;
    String age="";
    String model="";
    String regnumber="";
    private Toolbar toolbar;
     ProgressDialog progress;
    String email ;
    String familyname;
    String mody="";

    String password ;
    SharedPreferences sharedPref;

    public static int APP_REQUEST_CODE = 99;

    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
firebaseuser firyuser;

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



        setContentView(R.layout.extras);
        setTitle(getResources().getString(R.string.title_activity_register));
         sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
     auth=FirebaseAuth.getInstance();
        String languageToLoad = sharedPref.getString("language","fr"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        firyuser=new firebaseuser();
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extras.this.onBackPressed();
            }
        });
        RegisterActivity reg = new RegisterActivity();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.add(R.id.f1,reg);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();






        SharedPreferences sp=getSharedPreferences("smstimes",MODE_PRIVATE);
        SharedPreferences.Editor edi=sp.edit();
        edi.apply();
        edi.putInt("smstimes",0);
        edi.commit();










    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


            super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == APP_REQUEST_CODE&&data!=null) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            progressDialog.dismiss();

            if (loginResult.getError() != null) {
            } else {
                if (loginResult.getAccessToken() != null) {
                } else {
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        if (phoneNumber != null) {
                            phone=phoneNumber.getPhoneNumber();
                            if (!phone.startsWith("0"))
                                phone="0"+phone;
                            check();


                        }

                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        Toast.makeText(extras.this, "err "+error.getUserFacingMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            // Surface the result to your user in an appropriate way.
            /*Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();*/
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
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
    @Override
    public void next(String age,String string,Bitmap bitmap) {



        //  Toast.makeText(extras.this,age, Toast.LENGTH_SHORT).show();
        this.age=age;
        if (bitmap!=null)
            uploadImage(phone,bitmap);

        sendsms();








    }

    @Override
    public void done() {
        signup();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
           // super.onBackPressed();
            //additional code
            final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle(getResources().getString(R.string.twarning));
            alertbox.setMessage(getResources().getString(R.string.warning));

            alertbox.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            alertbox.setNeutralButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            alertbox.show();
        } else {

            super.onBackPressed();
        }

    }


    @Override
    public void f1(String familyname, String password, String email, int position) {
        this.familyname=familyname;
        this.password=password;
        this.email=email;
        this.position=position;
      /*  agedialog dia333 = new agedialog();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.replace(R.id.f1,dia333,"age");
        ft.addToBackStack("age");
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
      */
        sendsms();

    }



   public void signup() {




            progressDialog = new ProgressDialog(extras.this,
                  R.style.AppTheme_Dark_Dialog);
          progressDialog.setIndeterminate(true);
          progressDialog.setMessage(getResources().getString(R.string.creating));
       progressDialog.setCancelable(false);
          progressDialog.show();




          // TODO: Implement your own signup logic here.

          new android.os.Handler().postDelayed(
                  new Runnable() {
                      public void run() {
                          // On complete call either onSignupSuccess or onSignupFailed
                          // depending on success

                          // onSignupFailed();
                          auth.createUserWithEmailAndPassword(phone+"@tewsila.com",phone)

                                  .addOnCompleteListener(extras.this, new OnCompleteListener<AuthResult>() {
                                      @Override
                                      public void onComplete(@NonNull Task<AuthResult> task) {

                                          if (task.isSuccessful()) {
                                              auth.signInWithEmailAndPassword(phone+"@tewsila.com", phone).addOnCompleteListener(extras.this, new OnCompleteListener<AuthResult>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<AuthResult> task) {

                                                      if (task.isSuccessful()){
                                                          //TODO
                                                          databaseReference= FirebaseDatabase.getInstance().getReference();
                                                          firebaseUser=auth.getCurrentUser();

                                                          if (firebaseUser!=null) {
                                                               firyuser.uid=firebaseUser.getUid();
                                                               firyuser.email=firebaseUser.getEmail();

                                                              databaseReference.child("user").child(phone).setValue(firyuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                  @Override
                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                      if (task.isSuccessful()){
                                                                          databaseReference.child("arrived").child(phone).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                              @Override
                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                  if (task.isSuccessful()){

                                                                                      register();
                                                                                      databaseReference.child("user").child(phone).child("name").setValue(familyname);
                                                                                      databaseReference.child("user").child(phone).child("loc").child("lat").setValue(0.0);
                                                                                      databaseReference.child("user").child(phone).child("loc").child("lang").setValue(0.0);
                                                                                  }
                                                                              }
                                                                          });

                                                                      }else{
                                                                          progressDialog.dismiss();

                                                                      }

                                                                  }
                                                              });

                                                          }else
                                                              progressDialog.dismiss();


                                                      }else
                                                          progressDialog.dismiss();


                                                  }
                                              });
                                          } else {
                                              progressDialog.dismiss();
                                              // If sign in fails, display a message to the user.
                                              Toast.makeText(extras.this, "Authentication failed.",
                                                      Toast.LENGTH_SHORT).show();
                                          }

                                      }
                                  });

                      /*    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(extras.this, new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){


                                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(extras.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()){
                                             //TODO


                                            }

                                        }
                                    });


                                }
                                  else{
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(extras.this);
                                    builder3.setMessage(task.getException().getMessage())
                                            .setNegativeButton(getResources().getString(R.string.retry), null)
                                            .create()
                                            .show();

                                }
                              }

                          });*/






                      }
                  }, 1000);
      }

      public void showError(){

          AlertDialog.Builder builder3 = new AlertDialog.Builder(extras.this);
          builder3.setCancelable(false);
          builder3.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  register();
              }
          });
          builder3.setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  databaseReference.child("user").child(phone).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          auth.getCurrentUser().delete();

                      }
                  });
              }
          });

          builder3.setMessage(getResources().getString(R.string.error))
                  .create()
                  .show();
      }

    public void register(){
        RequestQueue queue;


        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               showError();




            }
        };


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success=false;
                    success = jsonResponse.getBoolean("success");
                    if (success) {

                        FirebaseMessaging.getInstance().subscribeToTopic("news");



                        AlertDialog.Builder builder = new AlertDialog.Builder(extras.this);
                        builder.setMessage(getResources().getString(R.string.success))
                                .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                     upload();
                                    }
                                })
                                .create()
                                .setCancelable(false);
                        builder.show();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(extras.this);
                        builder.setMessage(getResources().getString(R.string.numberused))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .create()
                                .show();


                    }
                } catch (JSONException e) {

                  showError();

                }
            }
        };


        RegisterRequest registerRequest = new RegisterRequest(position,phone,model,regnumber,kind,familyname, phone, age,email,mody, responseListener,errorListener,ipaddress.getUrl(this));
        queue = Volley.newRequestQueue(extras.this);
        registerRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(registerRequest);
    }
    public void upload(){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(extras.this);
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

                            FirebaseAuth.getInstance().signInWithEmailAndPassword(phone+"@tewsila.com",phone).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        UserLocalStore localStore=new UserLocalStore(extras.this);
                                        User user = new User(email,kind, phone, id, name, age, username, password, model, "","-1", "", "", "", "",0,sex);
                                        localStore.storeUserData(user);
                                        localStore.setUserLoggedIn(true);
                                        Intent intent = new Intent(extras.this, MapsActivity.class);

                                        extras.this.startActivity(intent);


                                        finish();

                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(extras.this);
                                        builder.setMessage(getResources().getString(R.string.loginfailed))
                                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                                .create()
                                                .show();


                                    }
                                }
                            });


                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(extras.this);
                            builder.setMessage(getResources().getString(R.string.loginfailed))
                                    .setNegativeButton(getResources().getString(R.string.retry), null)
                                    .create()
                                    .show();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(extras.this);
                        builder.setMessage(getResources().getString(R.string.loginfailed))
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();}

                } catch (JSONException e) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(extras.this);
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(phone, responseListener,errorListener,ipaddress.getUrl(this));
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(loginRequest);
    }
    public void check(){
        RequestQueue queue;




        progress = ProgressDialog.show(this, getResources().getString(R.string.loading),
                null, true);


        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.dismiss();

        }
        };


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progress.dismiss();

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success=false;
                    success = jsonResponse.getBoolean("success");
                    if (success) {
                        signup();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(extras.this);
                        builder.setMessage(getResources().getString(R.string.numberused))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                } catch (JSONException e) {


                    AlertDialog.Builder builder3 = new AlertDialog.Builder(extras.this);
                    builder3.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
            }
        };


        checkrequest registerRequest = new checkrequest(phone,email, responseListener,errorListener,ipaddress.getUrl(this));
        queue = Volley.newRequestQueue(extras.this);
        registerRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(registerRequest);
    }


    public void sendsms(){

        progressDialog = new ProgressDialog(extras.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        phoneLogin();


    }
    public void sendsms_twilio(final ProgressDialog progressDialog){
        code=getResources().getString(R.string.smscode);

        RequestQueue queue = Volley.newRequestQueue(this);

        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Bundle args44 = new Bundle();
                progressDialog.dismiss();
                args44.putInt("code", i1);
                args44.putString("phone", phone);
                sms dia44 = new sms();
                dia44.setArguments(args44);

                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.add(R.id.f1,dia44,"sms");
                ft2.addToBackStack("sms");

                ft2.commit();
                getSupportFragmentManager().executePendingTransactions();
                setTitle(getResources().getString(R.string.verification));

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


            }

        };

        smsrequest sms = new smsrequest( phone,code, responseListener,errorListener,ipaddress.getUrl(this));
        sms.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sms);
    }

    public void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN)
                        .setFacebookNotificationsEnabled(true)
                        .setVoiceCallbackNotificationsEnabled(true)
                ; // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        UIManager uiManager;

// Skin is CLASSIC, CONTEMPORARY, or TRANSLUCENT

        uiManager = new SkinManager(CLASSIC,getResources().getColor(R.color.colorPrimary));

        configurationBuilder.setUIManager(uiManager);
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }
}
