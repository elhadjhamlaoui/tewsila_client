package com.tewsila.client;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by neo on 27/01/2017.
 */
public class RegisterActivity extends Fragment {
    int position=1;

    EditText etFamilyname,etFirstname;

  ageinterface agy;


    EditText etEmail;
    EditText etPassword;
    TextView terms;
    CheckBox checkBox;
     RelativeLayout gender;
     TextView tgender;
    Button next;
    @Override
    public void onAttach(Activity activity) {
        agy = (ageinterface) activity;


        super.onAttach(activity);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activityregister, null);
        etFamilyname= view.findViewById(R.id.etFamilyname);
        etFirstname= view.findViewById(R.id.etFirstname);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        etEmail= view.findViewById(R.id.email);
        etPassword= view.findViewById(R.id.etPassword);

        next= view.findViewById(R.id.btRegister);

        terms= view.findViewById(R.id.termstext);
        checkBox= view.findViewById(R.id.checkBox);

        terms.setText(Html.fromHtml("<p><u>"+getResources().getString(R.string.terms)+"</u></p>"));


         gender = view.findViewById(R.id.etGender);
         tgender = view.findViewById(R.id.gender);


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("settings").child("terms").child("client").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.getValue().toString();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));

                        try {
                            startActivity(i);

                        }catch (ActivityNotFoundException e){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // signup();
                if (validate()) {

                    agy.f1(etFirstname.getText().toString()+" "+etFamilyname.getText().toString(),etPassword.getText().toString(),etEmail.getText().toString(),position);

                }
                else{

                }



            }

        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.gender));

                String[] gender = {getResources().getString(R.string.male), getResources().getString(R.string.female)};
                builder.setItems(gender, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                    position=0;
                                tgender.setText(getResources().getString(R.string.male));
                                dialog.dismiss();

                                break;
                            case 1:
                                position=1;
                                tgender.setText(getResources().getString(R.string.female));
                                dialog.dismiss();


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


            }
        });





        return view;


    }
    public boolean validate() {
        boolean valid = true;
        String firstname = etFirstname.getText().toString();

        String familyname = etFamilyname.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (familyname.isEmpty() || familyname.length() < 3) {
            etFamilyname.setError(getResources().getString(R.string.atleast));
            valid = false;
        } else {
            etFamilyname.setError(null);
        }
        if (firstname.isEmpty() || firstname.length() < 3) {
            etFirstname.setError(getResources().getString(R.string.atleast));
            valid = false;
        } else {
            etFirstname.setError(null);
        }



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getResources().getString(R.string.valideemail));
            valid = false;
        } else {
            etEmail.setError(null);
        }


        /*if (password.isEmpty() || password.length()<6) {
            etPassword.setError(getResources().getString(R.string.between));
            valid = false;
        } else {
            etPassword.setError(null);
        }*/

        if (!checkBox.isChecked()){

            checkBox.setError(getResources().getString(R.string.terms));
            valid=false;
        }else {

            checkBox.setError(null);

        }
       /* if (tgender.getText().toString().equals(getResources().getString(R.string.gender))){

            tgender.setError("");
            valid=false;
        }else {

            tgender.setError(null);

        }*/



        return valid;
    }
}
