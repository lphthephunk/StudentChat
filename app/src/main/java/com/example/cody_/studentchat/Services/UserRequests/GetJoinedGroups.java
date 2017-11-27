package com.example.cody_.studentchat.Services.UserRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Helpers.Globals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/27/2017.
 */

public class GetJoinedGroups extends StringRequest {

    static String GET_JOINED_GROUPS = "http://incho.xyz/GetJoinedGroups.php";
    private Map<String, String> params;

    public GetJoinedGroups(Response.Listener<String> listener){
        super(Method.POST, GET_JOINED_GROUPS, listener, null);
        params = new HashMap<>();

        params.put("username", Globals.currentUserInfo.getUsername());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
