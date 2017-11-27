package com.example.cody_.studentchat.Services.StudyGroupRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Services.ChatroomRequests.DeleteChatroomService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/20/2017.
 */

public class DeleteStudyGroup extends StringRequest {

    private static final String DELETE_STUDYGROUP = "http://incho.xyz/DeleteStudyGroup.php";
    private Map<String, String> params;

    public DeleteStudyGroup(StudyGroup group, Response.Listener<String> listener){
        super(Method.POST, DELETE_STUDYGROUP, listener, null);

        params = new HashMap<>();

        params.put("groupAdmin", group.getGroupAdmin());
        params.put("latitude", group.getLatitude());
        params.put("longitude", group.getLongitude());
        params.put("groupName", group.getGroupName());
    }

    @Override
    public Map<String, String> getParams(){return this.params;}
}
