package com.example.cody_.studentchat.Services.StudyGroupRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Helpers.Globals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/26/2017.
 */

public class GetAdminGroups extends StringRequest {
    private static final String GET_GROUPS = "http://incho.xyz/GetAdminGroups.php";
    private Map<String, String> params;

    public GetAdminGroups(Response.Listener<String> listener){
        super(Method.POST, GET_GROUPS, listener, null);
        params = new HashMap<>();

        params.put("adminName", Globals.currentUserInfo.getUsername());
    }

    @Override
    public Map<String, String> getParams(){ return this.params;}
}
