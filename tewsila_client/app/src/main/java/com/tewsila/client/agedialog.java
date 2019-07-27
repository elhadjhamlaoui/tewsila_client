package com.tewsila.client;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by neo on 25/01/2017.
 */
public class agedialog extends Fragment {
ageinterface agy;
    Button next;
    int d,m,y;
    String string="";
    private Uri filePath;
    private Uri imageUri;

    NumberPicker day,month,year;
    ImageView picture;
    private Bitmap bitmap;

    @Override
    public void onAttach(Activity activity) {
        agy = (ageinterface) activity;


        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.age, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        day= (NumberPicker) view.findViewById(R.id.numberPicker);
        month= (NumberPicker) view.findViewById(R.id.numberPicker2);
        year= (NumberPicker) view.findViewById(R.id.numberPicker3);
        next= (Button) view.findViewById(R.id.button20);
       // takepicture= (Button) view.findViewById(R.id.button15);

        picture= (ImageView) view.findViewById(R.id.imageView6);

        day.setMaxValue(31);
        day.setMinValue(1);
        day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                d=i1;

            }
        });
        month.setMaxValue(12);
        month.setMinValue(1);
        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                m=i1;
                if (i1==1)

             day.setMaxValue(31);
                else if(i1==2)  day.setMaxValue(28);

                else if(i1==3) day.setMaxValue(31);
                else if(i1==4) day.setMaxValue(30);

                else if(i1==5) day.setMaxValue(31);
                else if(i1==6) day.setMaxValue(30);
                else if(i1==7) day.setMaxValue(31);
                else if(i1==8) day.setMaxValue(31);
                else if(i1==9) day.setMaxValue(30);
                else if(i1==10) day.setMaxValue(31);
                else if(i1==11) day.setMaxValue(30);
                else if(i1==12) day.setMaxValue(31);



            }
        });
        year.setMaxValue(2017);
        year.setMinValue(1900);
        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                y=i1;

            }
        });
        if (getArguments()!=null){
            if (getArguments().getString("string")!=null) {
               // takepicture.setBackground(Drawable.createFromPath(getArguments().getString("string")));
                string=getArguments().getString("string");
            }
            d= getArguments().getInt("d");
            m=getArguments().getInt("m");
            y=getArguments().getInt("y");
            day.setValue(d);
            month.setValue(m);
            year.setValue(y);


        }
/*takepicture.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        chooser dia2 = new chooser();

        dia2.show(getActivity().getFragmentManager(), "dia2");



    }
});*/

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick one");
                ArrayList<String> displayValues=new ArrayList<>();
                    displayValues.add(getResources().getString(R.string.camera));
                displayValues.add(getResources().getString(R.string.galery));


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,displayValues);
                builder.setTitle("");
                builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photo));
                            imageUri = Uri.fromFile(photo);
                            startActivityForResult(intent, 0);
                            dialog.dismiss();


                        }else
                        {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                            dialog.dismiss();
                        }
                    }
                });


                builder.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agy.next(d+"/"+m+"/"+y,string,bitmap);

            }
        });
        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                picture.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
        }
        else if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri selectedImage = imageUri;
           getActivity(). getContentResolver().notifyChange(selectedImage, null);
            ContentResolver cr = getActivity().getContentResolver();
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(cr, selectedImage);

                picture.setImageBitmap(bitmap);

            } catch (Exception e) {


            }
        }
    }
}
