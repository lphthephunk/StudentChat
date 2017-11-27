package com.example.cody_.studentchat.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Adapters.StudyGroupListAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.StudyGroupRequests.GetAdminGroups;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody_ on 11/9/2017.
 */

public class CreatedGroupsFragment extends Fragment {

    RecyclerView createdStudyGroupRecycler;
    StudyGroupListAdapter studyGroupListAdapter;
    List<StudyGroup> createdStudyGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.created_group_fragment, container, false);

        createdStudyGroupRecycler = (RecyclerView)rootView.findViewById(R.id.createdStudyGroupRecycler);
        createdStudyGroupRecycler.setHasFixedSize(false);
        createdStudyGroupRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        createdStudyGroups = new ArrayList<>();

        studyGroupListAdapter = new StudyGroupListAdapter(rootView.getContext(), R.layout.study_group_adapter, createdStudyGroups);

        createdStudyGroupRecycler.setAdapter(studyGroupListAdapter);
        createdStudyGroupRecycler.setLayoutManager(layoutManager);

        populateList();

        return rootView;
    }

    private void populateList(){
        try {
            Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray jsonResponse = new JSONArray(response);

                            int length = jsonResponse.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonResponse.getJSONObject(i);
                                String adminName = jsonObject.getString("groupAdmin");
                                String groupName = jsonObject.getString("groupName");
                                String subject = jsonObject.getString("subject");
                                String latitude = jsonObject.getString("latitude");
                                String longitude = jsonObject.getString("longitude");
                                String startTime = jsonObject.getString("startTime");
                                String startDate = jsonObject.getString("startDate");
                                StudyGroup group = new StudyGroup(adminName, groupName, new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)),subject, startDate, startTime);
                                createdStudyGroups.add(group);
                            }
                            studyGroupListAdapter.notifyDataSetChanged();
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }
            };
            GetAdminGroups getAdminGroups = new GetAdminGroups(ResponseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(getAdminGroups);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
