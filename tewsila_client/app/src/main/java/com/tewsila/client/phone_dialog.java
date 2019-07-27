package com.tewsila.client;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by neo on 26/01/2017.
 */
public class phone_dialog extends DialogFragment {
Button done,cancel;
    phoneinterface pi;
    String phone;
    EditText editText;


    @Override
    public void onAttach(Activity activity) {
        pi = (phoneinterface) activity;


        super.onAttach(activity);
    }
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

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.phone_layout, null);

        setCancelable(false);

        done=view.findViewById(R.id.done);
        cancel=view.findViewById(R.id.cancel);
        editText=view.findViewById(R.id.edit);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phone = editText.getText().toString();
                if (phone.isEmpty() || phone.length()!=10) {
                    editText.setError(getResources().getString(R.string.validenumber));
                } else {
                pi.phone(phone);
                dismiss();
                }


                
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                pi.dismiss();

                dismiss();


            }
        });


        return  view;

    }




}
