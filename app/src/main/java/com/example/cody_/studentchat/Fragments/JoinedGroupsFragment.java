package com.example.cody_.studentchat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Adapters.StudyGroupListAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.UserRequests.GetJoinedGroups;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody_ on 11/9/2017.
 */

public class JoinedGroupsFragment extends Fragment {

    List<StudyGroup> studyGroupList;
    StudyGroupListAdapter studyAdapter;
    RecyclerView supportGroupRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.joined_groups_fragment, container, false);

        supportGroupRecycler = (RecyclerView)rootView.findViewById(R.id.studyGroupRecycler);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(rootView.getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        supportGroupRecycler.setLayoutManager(layoutmanager);

        supportGroupRecycler.setHasFixedSize(false);
        supportGroupRecycler.setItemAnimator(new DefaultItemAnimator());

        studyGroupList = new ArrayList<>();

        studyAdapter = new StudyGroupListAdapter(rootView.getContext(), R.layout.study_group_adapter, studyGroupList);
        supportGroupRecycler.setAdapter(studyAdapter);

        setStudyGroupList();

        return rootView;
    }

    public void setStudyGroupList(){
        try {
            Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success){
                            studyGroupList.clear();
                            String joinedGroups = jsonResponse.getString("joinedGroups");

                            studyGroupList.addAll(convertJsonToStudyGroupList(joinedGroups));

                            studyAdapter.notifyDataSetChanged();
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }
            };
            GetJoinedGroups getJoinedGroups = new GetJoinedGroups(ResponseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(getJoinedGroups);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private List<StudyGroup> convertJsonToStudyGroupList(String groupMemberJson){
        List<StudyGroup> groupList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(groupMemberJson);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String groupAdmin = jsonObject.getString("groupAdmin");
                String groupName = jsonObject.getString("groupName");
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                String subject = jsonObject.getString("subject");
                String startTime = jsonObject.getString("startTime");
                String startDate = jsonObject.getString("startDate");

                StudyGroup group = new StudyGroup(groupAdmin, groupName,
                        new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), subject, startDate, startTime);
                groupList.add(group);
            }

            return groupList;
        }catch(JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
