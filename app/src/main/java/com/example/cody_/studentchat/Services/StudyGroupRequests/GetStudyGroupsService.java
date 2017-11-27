package com.example.cody_.studentchat.Services.StudyGroupRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/20/2017.
 */

public class GetStudyGroupsService extends StringRequest {

    private static final String GET_STUDYGROUPS = "http://incho.xyz/GetStudyGroups.php";
    private Map<String, String> params;

    public GetStudyGroupsService(Response.Listener<String> listener) {
        super(Method.POST, GET_STUDYGROUPS, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
