package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class checkrequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "php/checkuser.php";
    private Map<String, String> params;

    public checkrequest(String username, String email, Response.Listener<String> listener, Response.ErrorListener errorListener,String url) {
        super(Method.POST, url+REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();

        params.put("username", username);
        params.put("email", email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}