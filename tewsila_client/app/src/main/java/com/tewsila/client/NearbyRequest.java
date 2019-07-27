package com.tewsila.client;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neo on 26/01/2017.
 */
public class NearbyRequest extends StringRequest {
    private Map<String, String> params;
    private static final String DURATION_REQUEST_URL = "php/Nearby.php";

    public NearbyRequest(double lat,double lng,Response.Listener<String> listener, Response.ErrorListener errorListener, String url) {
        super(Method.POST, url+DURATION_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("lat", Double.toString(lat));
        params.put("lng", Double.toString(lng));
        params.put("password", FirebaseAuth.getInstance().getUid());


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}