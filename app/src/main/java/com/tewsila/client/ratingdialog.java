package com.tewsila.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by neo on 01/01/2017.
 */


public class ratingdialog extends DialogFragment {
    Dialog dialog;
    Button close;
    int rate=0;
    TextView ratingtext,carmodeltext,nametext;
    EditText comment;
    int cost;
    CircleImageView driverpicture;
    RatingBar ratingBar;
    boolean credit_bool=false;
    TextView pickupp;
    TextView destinationn;
    TextView total;
    TextView regnum;
    TextView durationtext;
    double duration=0.0;
    TextView reduction,red_val;
    long begintime=0,endtime=0;
    double reductionValue;
    Communicator communicator;
    TextView credit_text;
    int credit=0;
    Calendar calendar;
    UserLocalStore localStore;
    User user;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.ratingdialog, null);
        pickupp= view.findViewById(R.id.textView33);
        setCancelable(false);

        localStore=new UserLocalStore(getActivity());

        user=localStore.getLoggedInUser();

        calendar= Calendar.getInstance();

        destinationn= view.findViewById(R.id.textView40);
        comment=view.findViewById(R.id.comment);
        credit_text= view.findViewById(R.id.credit);

        total= view.findViewById(R.id.textView67);
        regnum= view.findViewById(R.id.textView19);
        reduction= view.findViewById(R.id.reduction);
        red_val= view.findViewById(R.id.red_val);

        carmodeltext= view.findViewById(R.id.textView7);
        nametext= view.findViewById(R.id.textView4);
        driverpicture= view.findViewById(R.id.imageView2);
        durationtext= view.findViewById(R.id.textView61);

        ratingtext= view.findViewById(R.id.textView77);

        close= view.findViewById(R.id.button16);

        close.setClickable(false);

        reductionValue=1-(double)getArguments().getInt("reduction")/100;



        begintime=getArguments().getLong("begintime");
        endtime=getArguments().getLong("endtime");


        duration= ((double) (endtime-begintime)/1000)/60;




       String s= String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(endtime-begintime),
                TimeUnit.MILLISECONDS.toSeconds(endtime-begintime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endtime-begintime))
        );
        durationtext.setText(s);


        ratingBar = view. findViewById(R.id.ratingBar);
        ratingBar.setRating(0);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                 rate = (int) rating;

            }
        });
        pickupp.setText(getArguments().getString("pickup"));
        String matr=getArguments().getString("regnum");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < matr.length(); i++) {
            if (i==5) {
                result.append(" ");
            }
            if (i==8) {
                result.append(" ");
            }

            result.append(matr.charAt(i));
        }
        regnum.setText(result);

        carmodeltext.setText(getArguments().getString("model"));
        nametext.setText(getArguments().getString("name"));
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        ratingtext.setText("| "+formatter.format((float)getArguments().getInt("rating") / getArguments().getInt("rides"))+"/5");



       //pgeocoder();

        pickupp.setText(getArguments().getString("pickup"));
        destinationn.setText(getArguments().getString("destination"));

        cost=getArguments().getInt("cost");

        total.setText(Integer.toString(cost)+" DA");

        if (getArguments().getBoolean("found",false)&&getArguments().getInt("reduction")!=0){
            int cost=(int)(getArguments().getInt("cost")/reductionValue);
            cost=cost-(cost%10);
            reduction.setVisibility(View.VISIBLE);
            reduction.setText(Integer.toString((getArguments().getInt("cost")))+" DA");
            total.setText(Integer.toString(cost)+" DA");
            total.setTextColor(getResources().getColor(R.color.gray));
            red_val.setVisibility(View.VISIBLE);
            red_val.setText("-"+Integer.toString(getArguments().getInt("reduction"))+"%");
            total.setPaintFlags(total.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        downloadImage(getArguments().getString("taxiid"));

        FirebaseDatabase.getInstance().getReference().child("user").child(user.phone).child("credit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                close.setClickable(true);

               // credit_text.setVisibility(View.VISIBLE);
                if (dataSnapshot.getValue()!=null){
                    credit=Integer.parseInt(dataSnapshot.getValue().toString());
                }
                credit_bool=cost<credit;
                if (credit_bool){
                    credit_text.setText(getResources().getString(R.string.payment)+getResources().getString(R.string.credit)+" ( "+Integer.toString(credit)+" DA )");
                }
                else
                    credit_text.setText(getResources().getString(R.string.payment)+"Cash");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (rate>0) {
                    communicator.rating(comment.getText().toString(), rate, getArguments().getString("taxiid"),credit,cost,credit_bool);
                    ratingBar.setRating(0);

                    dismiss();
                }else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.forgetrating); // Want to enable?
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }

            }
        });



        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return  view;
    }



    public void downloadImage(String phone) {
        final StorageReference pathReference = FirebaseStorage.getInstance().getReference().child("images").child(phone+".jpg");
        pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                    GlideApp.with(getContext())
                            .load(pathReference)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(driverpicture);
            }
        });





}
}

