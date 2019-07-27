package com.tewsila.client;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;

import static com.facebook.accountkit.ui.SkinManager.Skin.CLASSIC;


/**
 * Created by neo on 02/09/2017.
 */

public class splash extends BaseActivity implements ageinterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    FloatingActionButton retry;
    ImageView error;
    CallbackManager callbackManager;
    ProgressBar progressBar;
    phone_dialog dia;
    SmsRetrieverClient client;
    Button login,register;
    ProgressDialog progressDialog;
    GoogleApiClient googleApiClient;
    public static int APP_REQUEST_CODE = 99;
    public static int APP_REQUEST_CODE_LOGIN = 100;

    DatabaseReference databaseReference;
    String phone="";

    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Instabug.Builder(getApplication(), "5a3e50ae4b20b09e600b9c6a84652fba")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();
         googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        if(isNetworkAvailable()){
            connect();

        }
        else{

            setContentView(R.layout.connection_error);
            retry=findViewById(R.id.floatingActionButton);
            progressBar=findViewById(R.id.progressBar2);
            error=findViewById(R.id.imageView10);


        progressBar.setVisibility(View.INVISIBLE);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        connect();

                    }

                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void signIn(){
        auth.signInWithEmailAndPassword(phone+"@tewsila.com",phone).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    databaseReference=FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("user").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue()==null){
                                progressDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                                builder.setMessage(getResources().getString(R.string.loginfailed)+"163")
                                        .setNegativeButton(getResources().getString(R.string.retry), null)
                                        .create()
                                        .show();
                               /* final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                                final FirebaseUser user=auth.getCurrentUser();
                                final firebaseuser fireuser =new firebaseuser();
                                databaseReference.child("user").child(phone).setValue(fireuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            databaseReference.child("arrived").child(phone).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        signup2(phone);
                                                        if (user!=null)
                                                            databaseReference.child("user").child(phone).child("name").setValue(user.getDisplayName());
                                                        databaseReference.child("user").child(phone).child("loc").child("lat").setValue(0.0);
                                                        databaseReference.child("user").child(phone).child("loc").child("lang").setValue(0.0);
                                                    }else
                                                        progressDialog.dismiss();


                                                }
                                            });

                                        }else{
                                            progressDialog.dismiss();

                                        }

                                    }
                                });*/

                            }
                            else{
                                upload(phone);

                            }



                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();

                        }
                    });

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(splash.this, "hehe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE_LOGIN&&data!=null) {

            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            } else if (loginResult.wasCancelled()) {
                progressDialog.dismiss();


            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // Get Account Kit ID
                        final PhoneNumber phoneNumber = account.getPhoneNumber();
                        if (phoneNumber != null) {

                            phone=phoneNumber.getPhoneNumber();
                            if (!phone.startsWith("0"))
                                phone="0"+phone;
                            auth.fetchProvidersForEmail(phone+"@tewsila.com").addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if(task.isSuccessful()){
                                        ///////// getProviders().size() will return size 1. if email ID is available.
                                        if (task.getResult().getProviders().size()==1){
                                            signIn();

                                        }else{
                                            progressDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                                            builder.setMessage(getResources().getString(R.string.loginfailed)+"271")
                                                    .setNegativeButton(getResources().getString(R.string.retry), null)
                                                    .create()
                                                    .show();
                                        }
                                        /*else {
                                            auth.createUserWithEmailAndPassword(phone+"@tewsila.com",phone)
                                                    .addOnCompleteListener(splash.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                            if (task.isSuccessful()) {
                                                                signIn();

                                                            } else {
                                                                progressDialog.dismiss();
                                                                // If sign in fails, display a message to the user.
                                                                Toast.makeText(splash.this, "Authentication failed.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });
                                        }*/
                                    }
                                }
                            });



                        }else
                            progressDialog.dismiss();



                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        progressDialog.dismiss();


                    }
                });



            }

        }
        if (requestCode == APP_REQUEST_CODE&&data!=null) {

            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                LoginManager.getInstance().logOut();

                auth.signOut();

            } else if (loginResult.wasCancelled()) {
                progressDialog.dismiss();
                LoginManager.getInstance().logOut();

                auth.signOut();

                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // Get Account Kit ID
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        if (phoneNumber != null) {

                            phone=phoneNumber.getPhoneNumber();
                            if (!phone.startsWith("0"))
                                phone="0"+phone;

                            databaseReference.child("user").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getValue()==null){

                                        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                                        final FirebaseUser user=auth.getCurrentUser();
                                        final firebaseuser fireuser =new firebaseuser();
                                        fireuser.uid=user.getUid();
                                        fireuser.email=user.getEmail();

                                        databaseReference.child("uid").child(user.getUid()).setValue(phone);
                                        databaseReference.child("user").child(phone).setValue(fireuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    databaseReference.child("arrived").child(phone).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){

                                                                signup(user,phone);
                                                                if (user!=null)
                                                                    databaseReference.child("user").child(phone).child("name").setValue(user.getDisplayName());
                                                                databaseReference.child("user").child(phone).child("loc").child("lat").setValue(0.0);
                                                                databaseReference.child("user").child(phone).child("loc").child("lang").setValue(0.0);
                                                            }else
                                                                progressDialog.dismiss();

                                                        }
                                                    });

                                                }else{
                                                    progressDialog.dismiss();

                                                }

                                            }
                                        });

                                    }
                                    else{
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(splash.this);
                                        builder2.setMessage(getResources().getString(R.string.numberused2))
                                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                                .create()
                                                .show();
                                        progressDialog.dismiss();

                                        LoginManager.getInstance().logOut();

                                        auth.signOut();

                                    }



                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });
                        }else
                            progressDialog.dismiss();


                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        progressDialog.dismiss();
                    }
                });



            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }
      public void signup(FirebaseUser user, final String phone){
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                        builder.setMessage(getResources().getString(R.string.success))
                                .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .setCancelable(false);
                        builder.show();
                        upload(phone);



                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                        builder.setMessage(getResources().getString(R.string.numberused))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                        databaseReference.child("user").child(phone).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                auth.getCurrentUser().delete();

                            }
                        });

                    }
                } catch (JSONException e) {


                    showError();

                }
            }
        };


        RegisterRequest registerRequest = new RegisterRequest(0,phone,"","","client",user.getDisplayName(), phone,"",user.getEmail(),"", responseListener,errorListener,ipaddress.getUrl(this));
        queue = Volley.newRequestQueue(splash.this);
        queue.add(registerRequest);
    }

    public void showError(){
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        builder3.setCancelable(false);
        builder3.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signup(auth.getCurrentUser(),phone);
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

    public void sendsms(){

        phoneLogin();



    }




    public void upload(String phone){


        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();


                AlertDialog.Builder builder2 = new AlertDialog.Builder(splash.this);
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
                progressDialog.dismiss();
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

                            User user = new User(email,kind, phone, id, name, age, username, "", model, "","-1", "", "", "", "",0,sex);
                            UserLocalStore localStore=new UserLocalStore(splash.this);
                            localStore.storeUserData(user);
                            localStore.setUserLoggedIn(true);
                            Intent intent = new Intent(splash.this, MapsActivity.class);

                            splash.this.startActivity(intent);


                            finish();

                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                            builder.setMessage(getResources().getString(R.string.loginfailed)+"652")
                                    .setNegativeButton(getResources().getString(R.string.retry), null)
                                    .create()
                                    .show();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                        builder.setMessage(getResources().getString(R.string.loginfailed)+"660")
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();


                    }

                } catch (JSONException e) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(splash.this);
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(phone, responseListener,errorListener,ipaddress.getUrl(this));
        RequestQueue queue = Volley.newRequestQueue(splash.this);
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(loginRequest);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        progressDialog = new ProgressDialog(splash.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        progressDialog.setMessage(getResources().getString(R.string.verification));
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            databaseReference=FirebaseDatabase.getInstance().getReference();

                            FirebaseDatabase.getInstance().getReference().child("uid").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue()==null){
                                        sendsms();
                                    }else{



                                        upload(dataSnapshot.getValue().toString());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });


                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(splash.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    void connect(){
        final UserLocalStore local = new UserLocalStore(this);
        setContentView(R.layout.splash);
        auth=FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplication());
        AppEventsLogger.activateApp(getApplication());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken!=null&&(auth.getCurrentUser() != null)&&local.getLoggedInUser()!=null){
            handleFacebookAccessToken(accessToken);
        }
        else{

            if (auth.getCurrentUser() == null||local.getLoggedInUser()==null) {
                login= findViewById(R.id.login);
                register= findViewById(R.id.register);

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       progressDialog = new ProgressDialog(splash.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);

                        progressDialog.setMessage(getResources().getString(R.string.verification));
                        progressDialog.show();
                        final Intent intent = new Intent(splash.this, AccountKitActivity.class);
                        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                                        LoginType.PHONE,
                                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
                        // ... perform additional configuration ...
                        UIManager uiManager;

// Skin is CLASSIC, CONTEMPORARY, or TRANSLUCENT

                        uiManager = new SkinManager(CLASSIC,getResources().getColor(R.color.colorPrimary));

                        configurationBuilder.setUIManager(uiManager);
                        intent.putExtra(
                                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                                configurationBuilder.build());
                        startActivityForResult(intent, APP_REQUEST_CODE_LOGIN);


                    }
                });
                /*login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(splash.this, LoginActivity.class);
                        splash.this.startActivity(intent);
                    }
                });*/
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(splash.this, extras.class);
                        splash.this.startActivity(intent);
                    }
                });
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logOut();

                final String EMAIL = "email";

                final LoginButton loginButton = ( findViewById(R.id.login_button));
                loginButton.setReadPermissions(Arrays.asList(EMAIL));
                // If you are using in a fragment, call loginButton.setFragment(this);
                FirebaseAuth mAuth;
// ...
// Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();
                // Callback registration


                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        handleFacebookAccessToken(loginResult.getAccessToken());



                    }

                    @Override
                    public void onCancel() {



                    }

                    @Override
                    public void onError(FacebookException exception) {

                        Toast.makeText(splash.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }else{
                Intent intent = new Intent(splash.this, MapsActivity.class);
                finish();
                splash.this.startActivity(intent);
            }}

    }

    @Override
    public void next(String age, String string, Bitmap bitmap) {

    }



    @Override
    public void done() {
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user=auth.getCurrentUser();
        final firebaseuser fireuser =new firebaseuser();
        fireuser.uid=user.getUid();
        fireuser.email=user.getEmail();

        databaseReference.child("uid").child(user.getUid()).setValue(phone);

        databaseReference.child("user").child(phone).setValue(fireuser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    databaseReference.child("arrived").child(phone).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                signup(user,phone);
                                if (user!=null)
                                    databaseReference.child("user").child(phone).child("name").setValue(user.getDisplayName());
                                databaseReference.child("user").child(phone).child("loc").child("lat").setValue(0.0);
                                databaseReference.child("user").child(phone).child("loc").child("lang").setValue(0.0);
                            }
                        }
                    });

                }else{
                }

            }
        });
    }

    @Override
    public void f1(String familyname, String password, String email, int position) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





    public void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
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
