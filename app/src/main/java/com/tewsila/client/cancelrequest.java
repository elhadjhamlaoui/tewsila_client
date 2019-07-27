package com.tewsila.client;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class cancelrequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "php/cancel.php";
    private Map<String, String> params;

    public cancelrequest(String taxiid, String myid, String rideid, Response.Listener<String> listener, String url) {
        super(Method.POST,url+ LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());
        params.put("clientid",taxiid);
        params.put("myid",myid);
        params.put("rideid",rideid);
        params.put("kind","driver");



    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
