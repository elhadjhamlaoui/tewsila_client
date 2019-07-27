package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class editRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL ="php/edit.php";
    private Map<String, String> params;

    public editRequest(String phone,String name,String email,String age,String gender, Response.Listener<String> listener, Response.ErrorListener errorListener,String url) {
        super(Method.POST, url+LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());
        params.put("phone", phone);
        params.put("name", name);
        params.put("email", email);
        params.put("age", age);
        params.put("gender", gender);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
