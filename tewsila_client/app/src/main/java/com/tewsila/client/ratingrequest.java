package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class ratingrequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "php/rating.php";
    private Map<String, String> params;

    public ratingrequest(String comment, int rating,String taxiid,String phone,String rideid, Response.Listener<String> listener,String url) {
        super(Method.POST, url+LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());

        params.put("rating", Integer.toString(rating) );
        params.put("taxiid", taxiid );
        params.put("phone", phone );
        params.put("rideid", rideid );

        params.put("comment", comment );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}