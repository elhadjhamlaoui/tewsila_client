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
public class DurationRequest extends StringRequest {
    private Map<String, String> params;
    private static final String DURATION_REQUEST_URL = "php/Duration.php";

    public DurationRequest(double lat, double lng, double dlat, double dlng,Response.Listener<String> listener, Response.ErrorListener errorListener, String url) {
        super(Method.POST, url+DURATION_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("lat", Double.toString(lat));
        params.put("lng", Double.toString(lng));
        params.put("dlat",Double.toString(dlat));
        params.put("dlng",Double.toString(dlng));
        params.put("password", FirebaseAuth.getInstance().getUid());


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}