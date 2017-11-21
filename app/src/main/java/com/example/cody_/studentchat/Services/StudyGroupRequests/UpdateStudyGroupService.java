package com.example.cody_.studentchat.Services.StudyGroupRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/20/2017.
 */

public class UpdateStudyGroupService extends StringRequest {

    private static final String UPDATE_STUDYGROUP = "http://incho.xyz/UpdateStudyGroup.php";
    private Map<String, String> params;

    public UpdateStudyGroupService(StudyGroup group, Response.Listener<String> listener){
        super(Method.POST, UPDATE_STUDYGROUP, listener, null);

        params = new HashMap<>();
        params.put("groupName", group.getGroupName());
        params.put("subject", group.getSubject());
        params.put("startDate", group.getStartDate());
        params.put("startTime", group.getStartTime());
        params.put("latitude", group.getLatitude());
        params.put("longitude", group.getLongitude());
        params.put("groupAdmin", group.getGroupAdmin());
        params.put("groupMembers", group.getJsonGroupMemberList());
    }

    @Override
    public Map<String, String> getParams(){ return this.params;}
}
