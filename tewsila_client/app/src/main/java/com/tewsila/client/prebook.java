package com.tewsila.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by neo on 26/12/2016.
 */
public class prebook extends DialogFragment {
    Communicator communicator;
DatePicker date;
    Dialog dialog;
    Button setdate,settime;
    TimePicker time;
    String sdate="",stime="";
    Calendar calendar;
    @Override
    public void onAttach(Activity activity) {
         communicator = (Communicator) activity;


        super.onAttach(activity);
    }
    @Override
    public void onStart() {

        super.onStart();
         dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.prebook, null);

        date=(DatePicker)view.findViewById(R.id.datePicker);
        time=(TimePicker)view.findViewById(R.id.timePicker);
        time.setIs24HourView(true);
        setdate=(Button) view.findViewById(R.id.button18);
        settime=(Button) view.findViewById(R.id.button17);
       date.setMinDate(System.currentTimeMillis());

        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settime.setVisibility(View.VISIBLE);
                date.setVisibility(View.INVISIBLE);
                setdate.setVisibility(View.INVISIBLE);
                time.setVisibility(View.VISIBLE);
                sdate = Integer.toString(date.getDayOfMonth())+"-"+Integer.toString(date.getMonth()+1)+"-"+Integer.toString(date.getYear());
              }
        });
        settime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    stime = Integer.toString(time.getHour())+"-"+Integer.toString(time.getMinute());
                }else
                    stime = Integer.toString(time.getCurrentHour())+"-"+Integer.toString(time.getCurrentMinute());

                calendar = Calendar.getInstance();
                //calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
                calendar.set(Calendar.MINUTE, time.getCurrentMinute());
                calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.YEAR, date.getYear());

                // Toast.makeText(getActivity(),sdate+" "+stime,Toast.LENGTH_SHORT).show();
                if ((calendar.getTimeInMillis()-System.currentTimeMillis())>=60*60*1000 ) {
                    communicator.book(sdate, stime);
                    dismiss();
                }
                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.late); // Want to enable?
                    builder.setPositiveButton(R.string.ok,null);
                    builder.create().show();



                }

            }
        });
        return  view;
    }
}
