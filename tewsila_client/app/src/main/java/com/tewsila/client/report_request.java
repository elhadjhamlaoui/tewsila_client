package com.tewsila.client;

/**
 * Created by neo on 02/12/2016.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class report_request extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "php/report.php";
    private Map<String, String> params;

    public report_request(String report,int id,String kind, Response.Listener<String> listener,String url) {
        super(Method.POST, url+LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>(); params.put("password", FirebaseAuth.getInstance().getUid());

        params.put("id", Integer.toString(id));
        params.put("report", report);
        params.put("kind", kind);



    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
