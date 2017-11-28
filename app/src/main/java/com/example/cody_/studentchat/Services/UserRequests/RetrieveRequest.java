package com.example.cody_.studentchat.Services.UserRequests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/27/2017.
 */

public class RetrieveRequest extends StringRequest {
    private static final String RETRIEVE_REQUEST_URL = "http://incho.xyz/EmailCheck.php";
    private Map<String, String> params;

    public RetrieveRequest(String email, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, RETRIEVE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}