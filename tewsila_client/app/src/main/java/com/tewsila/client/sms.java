package com.tewsila.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by neo on 26/01/2017.
 */
public class sms extends Fragment {
Button done;
    EditText code;
    int i=0;
  TextView progress;
    ageinterface agy;
    Button resend;
    String phone;

    @Override
    public void onAttach(Activity activity) {
      agy = (ageinterface) activity;


        super.onAttach(activity);
    }


    @Override
    public void onDestroy() {

        getActivity().setTitle(getResources().getString(R.string.title_activity_register));
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.sms, null);
        done= view.findViewById(R.id.button21);
        code= view.findViewById(R.id.editText6);
        resend= view.findViewById(R.id.resend);
        progress= view.findViewById(R.id.progress);
        phone=getArguments().getString("phone");

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                if (messageText.contains("[")&&messageText.contains("]")){
                    String result = messageText.substring(messageText.indexOf("[") + 1, messageText.indexOf("]"));
                    verify(result,phone);
                }

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (code.getText().toString().length()>5){
                    done.setClickable(false);

                verify(code.getText().toString(),phone);
                }else{
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
                    builder3.setMessage(getResources().getString(R.string.wrongcode))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();

                }


                
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getActivity().getSharedPreferences("smstimes",MODE_PRIVATE);
                SharedPreferences.Editor edi=sp.edit();
                edi.apply();
                edi.putInt("smstimes",sp.getInt("smstimes",0)+1);
                edi.commit();
                if (sp.getInt("smstimes",0)<3)
                sendsms();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.notyet); // Want to enable?
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "Support@tewsila.com", null));

                            getActivity().startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.sendemail)));
                        }
                    });
                    builder.create().show();

                }

            }
        });
         progress();

        return  view;

    }

    void progress(){

        resend.setClickable(false);
        i=0;
        final Handler handler=new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i<60){

                    i++;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setText(Integer.toString(60-i));
                            //   babe6(id);

                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }


                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        resend.setClickable(true);

                        //   babe6(id);

                    }
                });




            }
        }).start();

    }
    public void verify(String code,String phone){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                done.setClickable(true);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        agy.done();

                    }else{
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
                        builder3.setMessage(getResources().getString(R.string.wrongcode))
                                .setNegativeButton(getResources().getString(R.string.retry), null)
                                .create()
                                .show();

                    }


                } catch (JSONException e) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setMessage(getResources().getString(R.string.error))
                            .setNegativeButton(getResources().getString(R.string.retry), null)
                            .create()
                            .show();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                done.setClickable(true);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setMessage(getResources().getString(R.string.error))
                        .setNegativeButton(getResources().getString(R.string.retry), null)
                        .create()
                        .show();
            }

        };


        verify_request request = new verify_request( phone,code, responseListener,errorListener,ipaddress.getUrl(getActivity()));
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }
    public void sendsms(){
         String code=getResources().getString(R.string.smscode);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(R.string.smssent);
                builder.setPositiveButton((getResources().getString(R.string.ok)),null);
                builder.create().show();

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        };


        smsrequest sms = new smsrequest( phone,code, responseListener,errorListener,ipaddress.getUrl(getActivity()));

        queue.add(sms);


    }


}
