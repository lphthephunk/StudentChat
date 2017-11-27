package com.example.cody_.studentchat.Services.StudyGroupRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/22/2017.
 */

public class GetSingleStudyGroupService extends StringRequest {

    private static final String GET_SINGLE_STUDYGROUP = "http://incho.xyz/GetSingleStudyGroup.php";
    private Map<String, String> params;

    public GetSingleStudyGroupService(LatLng position, Response.Listener<String> listener){
        super(Method.POST, GET_SINGLE_STUDYGROUP, listener, null);

        params = new HashMap<>();

        params.put("latitude", String.valueOf(position.latitude));
        params.put("longitude", String.valueOf(position.longitude));
    }

    @Override
    public Map<String, String> getParams(){return this.params;}
}
