package com.tewsila.client;

/**
 * Created by neo on 13/05/2017.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class pricerequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL ="php/price.php";
    private Map<String, String> params;


    public pricerequest(String phone,long distance,String mode,long time,double lat,double lng,double dis_price,double time_price,double plat,double plng,double dlat,double dlng, Response.Listener<String> listener,Response.ErrorListener errorListener,String url) {
        super(Method.POST, url+LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());
        MCrypt mcrypt = new MCrypt();

        //Log.wtf("togi",encryption.encrypt(Long.toString(distance)));
        params.put("phone", phone);
        try {
            params.put("dd",MCrypt.bytesToHex( mcrypt.encrypt(Long.toString(distance))));
            params.put("tt",MCrypt.bytesToHex( mcrypt.encrypt(Long.toString(time))));
           // Log.wtf("togi","encr : "+MCrypt.bytesToHex( mcrypt.encrypt(Long.toString(distance))));


        } catch (Exception e) {
            e.printStackTrace();
           // Log.wtf("togi",e.getMessage());
        }
        params.put("mode", mode);
        params.put("lat", Double.toString(lat));
        params.put("lng", Double.toString(lng));
        params.put("dis_price", Double.toString(dis_price));
        params.put("time_price", Double.toString(time_price));

        params.put("plat", Double.toString(plat));
        params.put("plng", Double.toString(plng));
        params.put("dlat", Double.toString(dlat));
        params.put("dlng", Double.toString(dlng));

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {




        return params;
    }


    private String MyHashHmac(String secret, String data){
        String returnString = "";
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(),"HmacSHA1");
            Mac mac =  Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] dataByteArray = mac.doFinal(data.getBytes());
            BigInteger hash = new BigInteger(1,dataByteArray);
            returnString = hash.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnString;
    }
}