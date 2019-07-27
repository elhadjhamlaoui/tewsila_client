package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neo on 26/01/2017.
 */
public class verify_request extends StringRequest {
    private static final String REGISTER_REQUEST_URL ="sms/verify.php";
    private Map<String, String> params;

    public verify_request(String phone, String code, Response.Listener<String> listener, Response.ErrorListener errorListener,String url) {
        super(Method.POST, url+REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("code2", code);

        params.put("phone2","+213"+ phone);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}