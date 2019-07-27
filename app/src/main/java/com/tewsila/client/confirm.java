package com.tewsila.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jcodec.common.RunLength;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by neo on 24/12/startbase16.
 */
public class confirm extends DialogFragment {
    TextView pickupp;
    TextView destinationn;
    TextView totaltext;
    TextView timetext;
    TextView reduction,red_val;
    TextView credit_text;
    EditText promo;
    Button send;

    UserLocalStore localStore;
    Dialog dialog;
    int old = -1;
    Button validate;
    User user;
    int credit=0;
     boolean credit_bool=false;
    int costint=0;
    double reductionValue;
    int cost;
    Communicator communicator;

    @Override
    public void onStart() {

        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = super.onCreateDialog(savedInstanceState);
     //   dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }
    @Override
    public void onAttach(Activity activity) {
         communicator = (Communicator) activity;


        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.confirm, null);


         localStore=new UserLocalStore(getActivity());

         user=localStore.getLoggedInUser();
        send= view.findViewById(R.id.button16);

        send.setClickable(false);
        credit_text= view.findViewById(R.id.credit);

        reductionValue=1-(double)getArguments().getInt("reduction")/100;

        red_val=view.findViewById(R.id.red_val);
        pickupp= view.findViewById(R.id.textView33);
        destinationn= view.findViewById(R.id.textView40);
        promo= view.findViewById(R.id.promo);
       validate=view.findViewById(R.id.validate);
        totaltext= view.findViewById(R.id.textView67);
        reduction= view.findViewById(R.id.reduction);

        timetext= view.findViewById(R.id.textView61);



        pickupp.setText(getArguments().getString("pickup"));
        destinationn.setText(getArguments().getString("destination"));
        timetext.setText(getArguments().getString("time"));



        costint= getArguments().getInt("costint");

        totaltext.setText(getArguments().getString("total"));

        cost=costint;

        if (getArguments().getBoolean("found",false)&&getArguments().getInt("reduction")!=0){
            cost=(int)((costint)*reductionValue);
            cost=cost-(cost%10);

            reduction.setVisibility(View.VISIBLE);
            reduction.setText(Integer.toString(cost)+" DA");
            red_val.setVisibility(View.VISIBLE);
            red_val.setText("-"+Integer.toString(getArguments().getInt("reduction"))+"%");
            totaltext.setTextColor(getResources().getColor(R.color.gray));
            totaltext.setPaintFlags(totaltext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        FirebaseDatabase.getInstance().getReference().child("user").child(user.phone).child("credit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                send.setClickable(true);
                 if (dataSnapshot.getValue()!=null){
                     credit=Integer.parseInt(dataSnapshot.getValue().toString());
                 }
                //credit_text.setVisibility(View.VISIBLE);
                credit_bool=cost<credit;
                if (credit_bool)
                    credit_text.setText(getResources().getString(R.string.payment)+getResources().getString(R.string.credit)+" ( "+Integer.toString(credit)+" DA )");
                else
                    credit_text.setText(getResources().getString(R.string.payment)+"Cash");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (promo.getText().toString().length()>0)
                {
                    final ProgressDialog progressDialog=new ProgressDialog(getActivity());
                    progressDialog.setMessage(getResources().getString(R.string.verification));
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference().child("promo_codes").child(promo.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressDialog.dismiss();
                            if (dataSnapshot.getValue()!=null){
                                int limit = Integer.parseInt(dataSnapshot.child("limit").getValue().toString());
                                int min = Integer.parseInt(dataSnapshot.child("min").getValue().toString());

                                Object value = dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue();
                                if (value==null||Integer.parseInt(value.toString())<limit){
                                    if (value==null)
                                        old=0;
                                    else
                                        old=Integer.parseInt(value.toString());

                                    reductionValue=1-(double)Integer.parseInt(dataSnapshot.child("reduction").getValue().toString())/100;


                                    cost=(int)((costint)*reductionValue);

                                    cost=cost-(cost%10);
                                    if (cost<min){
                                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                        builder.setNeutralButton(getString(R.string.ok),null);
                                        builder.setMessage(getString(R.string.minimum)+" "+ Integer.toString(min)+" DA");
                                        builder.show();
                                        cost=min;
                                        old=-1;

                                    }else{
                                        validate.setClickable(false);

                                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                        builder.setNeutralButton(getString(R.string.ok),null);
                                        builder.setMessage(getResources().getString(R.string.promosuccess)+" "+dataSnapshot.child("reduction").getValue().toString()+" %");
                                        builder.show();

                                        reduction.setVisibility(View.VISIBLE);
                                        reduction.setText(Integer.toString(cost)+" DA");
                                        red_val.setVisibility(View.VISIBLE);
                                        red_val.setText("-"+dataSnapshot.child("reduction").getValue().toString()+"%");
                                        totaltext.setTextColor(getResources().getColor(R.color.gray));
                                        totaltext.setPaintFlags(totaltext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



                                        credit_bool=cost<credit;
                                        if (credit_bool)
                                            credit_text.setText(getResources().getString(R.string.payment)+getResources().getString(R.string.credit)+" ( "+Integer.toString(credit)+" DA )");
                                        else
                                            credit_text.setText(getResources().getString(R.string.payment)+"Cash");


                                        FirebaseDatabase.getInstance().getReference().child("promo_codes").child(promo.getText().toString()).child("usage").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().setValue(Integer.parseInt(dataSnapshot.getValue().toString())+1);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }





                                }else{
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                    builder.setNeutralButton("ok",null);
                                    builder.setMessage(getResources().getString(R.string.wrongcode));
                                    builder.show();
                                }


                            }else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                builder.setNeutralButton("ok",null);
                                builder.setMessage(getResources().getString(R.string.wrongcode));
                                builder.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar=Calendar.getInstance();
                String year=Integer.toString(calendar.get(Calendar.YEAR));
                String month=Integer.toString(calendar.get(Calendar.MONTH)+1);
                String day=Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));



                Map mParent = new HashMap();
                mParent.put("client", user.phone);
                mParent.put("pickup", getArguments().getString("pickup"));
                mParent.put("destination", getArguments().getString("destination"));
                mParent.put("cost", cost);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usage").child(year).child(month).child(day);

                String key = reference.push().getKey();
                reference.child(key).setValue(mParent);
                key = year+"/"+month+"/"+day+"/"+key;
                if (old!=-1)
                    FirebaseDatabase.getInstance().getReference().child("promo_codes").child(promo.getText().toString()).child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(old+1);

                if (getArguments().getString("kind").equals("prebook"))
              communicator.change2(getArguments().getString("date"),getArguments().getString("ptime"),getArguments().getInt("seats",0),getArguments().getString("pickup"),getArguments().getString("destination"),getArguments().getBoolean("vip",false),cost,getArguments().getBoolean("found",false),getArguments().getInt("red_value"),getArguments().getString("price"),credit_bool,timetext.getText().toString(),key);
                else  {

                    communicator.change(getArguments().getInt("seats",0),getArguments().getString("pickup"),getArguments().getString("destination"),getArguments().getBoolean("vip",false),cost,"book",getArguments().getBoolean("found",false),getArguments().getInt("red_value"),getArguments().getString("price"),credit_bool,timetext.getText().toString(),key);

                }

                   dismiss();

            }
        });

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return view;
    }





}
