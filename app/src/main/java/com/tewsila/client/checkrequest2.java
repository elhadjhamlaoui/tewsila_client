package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class checkrequest2 extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "php/checkemail.php";
    private Map<String, String> params;

    public checkrequest2(String phone,String email, Response.Listener<String> listener,String url) {
        super(Method.POST, url+REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("phone", phone);
        params.put("email", email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}