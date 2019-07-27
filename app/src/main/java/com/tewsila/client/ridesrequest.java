package com.tewsila.client;

/**
 * Created by neo on 20/04/2017.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class ridesrequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "php/rides.php";
    private Map<String, String> params;
    String state;

    public ridesrequest(String phone,String kind,int number1, Response.Listener<String> listener,Response.ErrorListener errorListener,String url) {
        super(Method.POST, url+LOGIN_REQUEST_URL, listener, errorListener);
        if (number1==2)
            state="completed";
        else if (number1==3)
            state="canceled";

        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());


        params.put("phone", phone);
        params.put("kind", kind);
        params.put("state",state);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return params;
    }
}
