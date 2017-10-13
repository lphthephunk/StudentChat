package com.example.cody_.studentchat;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ino on 2017-10-10.
 */

public class RegisterRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://incho.xyz/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String firstname, String lastname, String password, String email, String uuid, long mobile, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("password", password);
        params.put("email", email);
        params.put("uuid", uuid);
        params.put("mobile", mobile + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}