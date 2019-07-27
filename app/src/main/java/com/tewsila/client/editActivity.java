package com.tewsila.client;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elhadj on 25/11/2017.
 */

public class editActivity extends BaseActivity {
   EditText name;
   Button save;
   User user;
    ProgressDialog dialog;
   UserLocalStore localStore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        setTitle(getResources().getString(R.string.edit));

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editActivity.this.onBackPressed();
            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){

        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        localStore=new UserLocalStore(this);
        user=localStore.getLoggedInUser();
        name=findViewById(R.id.name);
      //  phone=findViewById(R.id.phone);
        save=findViewById(R.id.save);

        name.setText(user.name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    dialog=new ProgressDialog(editActivity.this);
                    dialog.setMessage(getResources().getString(R.string.loading));
                    dialog.show();

                    save.setEnabled(false);
                    change();
                }

            }
        });


    }

    public void change2(String email){

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(editActivity.this);
                builder2.setMessage(getResources().getString(R.string.error))
                        .setNegativeButton(getResources().getString(R.string.retry), null)
                        .create()
                        .show();
                dialog.dismiss();

            }
        };
        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");



                } catch (JSONException e) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(editActivity.this);
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
                dialog.dismiss();
                finish();

            }
        };

        editRequest edit = new editRequest(user.phone,name.getText().toString(),email,user.age,user.sex, responseListener,errorListener,ipaddress.getUrl(this));
        RequestQueue queue = Volley.newRequestQueue(editActivity.this);
        queue.add(edit);
    }

    void change(){
        FirebaseDatabase.getInstance().getReference().child("user").child(user.phone).child("name").setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    user = new User(user.email, user.kind, user.phone, user.id, name.getText().toString(), user.age, user.username, user.password, user.model, user.taxiname, user.taxiid, user.pickup, user.destination, user.time, user.driverphone, user.rating,user.sex);
                    localStore.storeUserData(user);

               // if (!phone.getText().toString().equals(user.phone))
                  /*  FirebaseDatabase.getInstance().getReference().child("user").child(phone.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()==null){
                            FirebaseDatabase.getInstance().getReference().child("user").child(user.phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FirebaseDatabase.getInstance().getReference().child("user").child(phone.getText().toString()).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                user = new User(user.email, user.kind, phone.getText().toString(), user.id, user.name, user.age, user.username, user.password, user.model, user.taxiname, user.taxiid, user.pickup, user.destination, user.time, user.driverphone, user.rating);
                                                localStore.storeUserData(user);
                                                FirebaseDatabase.getInstance().getReference().child("user").child(user.phone).setValue(null);


                                            }
                                            save.setEnabled(true);

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    save.setEnabled(true);

                                }
                            });
                        }
                        else{
                            save.setEnabled(true);

                            AlertDialog.Builder builder = new AlertDialog.Builder(editActivity.this);
                            builder.setMessage(getResources().getString(R.string.numberused2))
                                    .setNegativeButton(getResources().getString(R.string.retry), null)
                                    .create()
                                    .show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        save.setEnabled(true);

                    }
                });*/
                }

            }
        });
        change2(user.email);
       /* FirebaseAuth.getInstance().getCurrentUser().updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                save.setEnabled(true);

                if (task.isSuccessful()){

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email.getText().toString(), user.password);
                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential);

                    user = new User(email.getText().toString(), user.kind, user.phone, user.id, user.name, user.age, user.username, user.password, user.model, user.taxiname, user.taxiid, user.pickup, user.destination, user.time, user.driverphone, user.rating,user.sex);
                    localStore.storeUserData(user);

                    change2(email.getText().toString());

                }
                else{
                    change2(user.email);

                    AlertDialog.Builder builder = new AlertDialog.Builder(editActivity.this);
                    builder.setMessage(getResources().getString(R.string.emailused))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }
            }
        });*/


    }
    boolean validate() {
        boolean valid = true;


        String nametext = name.getText().toString();




        if (nametext.isEmpty() || nametext.length() < 3 ) {
            name.setError(getResources().getString(R.string.atleast));
            valid = false;
        } else {
            name.setError(null);
        }




        return valid;
    }
}
