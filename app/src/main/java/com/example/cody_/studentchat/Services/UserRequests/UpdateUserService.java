package com.example.cody_.studentchat.Services.UserRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/20/2017.
 */

public class UpdateUserService extends StringRequest {

    private static final String UPDATE_USER = "http://incho.xyz/UpdateUser.php";
    private Map<String, String> params;

    public UpdateUserService(User user, Response.Listener<String> listener){
        super(Method.POST, UPDATE_USER, listener, null);

        params = new HashMap<>();
        params.put("userName", user.getUsername());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("joinedGroups", user.getJsonJoinedGroups());
    }

    @Override
    public Map<String, String> getParams(){ return this.params;}
}
