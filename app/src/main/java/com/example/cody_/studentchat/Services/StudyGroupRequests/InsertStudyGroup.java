package com.example.cody_.studentchat.Services.StudyGroupRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cody_ on 11/20/2017.
 */

public class InsertStudyGroup extends StringRequest {

    private static final String INSERT_STUDYGROUP = "http://incho.xyz/InsertStudyGroup.php";
    private Map<String, String> params;

    public InsertStudyGroup(String groupName, String subject, String startDate, String startTime, String latitude, String longitude,
                            String groupAdmin, Response.Listener<String> listener){
        super(Method.POST, INSERT_STUDYGROUP, listener, null);

        params = new HashMap<>();
        params.put("groupName", groupName);
        params.put("subject", subject);
        params.put("startDate", startDate);
        params.put("startTime", startTime);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("groupAdmin", groupAdmin);
        params.put("groupMemberList", CreateGroupMemberList());
    }

    /*
    * Since the current user is the one creating the group, they will initially be the only group member*/
    private String CreateGroupMemberList(){
        List<User> singletonList = new ArrayList<>();
        singletonList.add(Globals.currentUserInfo);
        return new Gson().toJson(singletonList);
    }

    @Override
    public Map<String, String> getParams(){return this.params;}
}
