package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "php/Register.php";
    private Map<String, String> params;

    public RegisterRequest(int extra, String phone, String model, String regnumber, String kind, String name, String username, String age, String email, String mody, Response.Listener<String> listener, Response.ErrorListener errorListener,String url) {
        super(Method.POST, url+REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());
        params.put("name", name);
        params.put("age", age);
        params.put("username", username);
        params.put("kind",kind);
        params.put("mody",mody);
        params.put("regnumber",regnumber);
        params.put("email",email);
        params.put("model",model);
        params.put("phone",phone);
        params.put("extra", Integer.toString(extra));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}