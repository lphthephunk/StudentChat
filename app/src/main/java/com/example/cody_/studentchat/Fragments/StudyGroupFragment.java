package com.example.cody_.studentchat.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cody_.studentchat.Adapters.ChatRoomListAdapter;
import com.example.cody_.studentchat.Adapters.StudyGroupFragAdapter;
import com.example.cody_.studentchat.Adapters.StudyGroupListAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody_ on 11/27/2017.
 */

public class StudyGroupFragment extends DialogFragment {

    private List<StudyGroup> groupList;
    private StudyGroupFragAdapter groupListAdapter;
    private RecyclerView groupRecycler;
    private Button submitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.studygroup_fragment,null);

        getDialog().setTitle("Select StudyGroup");

        submitBtn = (Button)rootView.findViewById(R.id.shareGroupBtn);

        groupRecycler = (RecyclerView)rootView.findViewById(R.id.studyGroupFragRecycler);
        groupRecycler.setHasFixedSize(false);
        groupRecycler.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        groupList = new ArrayList<>();

        groupListAdapter = new StudyGroupFragAdapter(rootView.getContext(), R.layout.study_group_adapter, groupList);

        groupRecycler.setLayoutManager(layoutManager);
        groupRecycler.setAdapter(groupListAdapter);

        setList();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoomActivity parent = (ChatRoomActivity)getActivity();
                EditText messageBar = (EditText)parent.findViewById(R.id.messageContentEdit);
                StringBuilder builder = new StringBuilder();
                builder.append("Group Name: " + Globals.GroupToShare.getGroupName());
                builder.append("\n");
                builder.append("Subject: " + Globals.GroupToShare.getSubject());
                builder.append("\n");
                builder.append("Start Date: " + Globals.GroupToShare.getStartDate());
                builder.append("\n");
                builder.append("Start Time: " + Globals.GroupToShare.getStartTime());
                builder.append("\n");
                String query = URLEncoder.encode(String.valueOf(Globals.GroupToShare.getLatitude()) + ", " + String.valueOf(Globals.GroupToShare.getLongitude()));
                builder.append("Click to navigate: " + "https://www.google.com/maps/search/?api=1&query=" + query);

                messageBar.setText(builder.toString());
                dismiss();
            }
        });

        return rootView;
    }

    private void setList(){
        try {
            JSONArray jsonArray = new JSONArray(Globals.currentUserInfo.getJsonJoinedGroups());
            int length = jsonArray.length();

            groupList.clear();

            for (int i = 0; i < length; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String groupAdmin = jsonObject.getString("groupAdmin");
                String groupName = jsonObject.getString("groupName");
                String subject = jsonObject.getString("subject");
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                String startTime = jsonObject.getString("startTime");
                String startDate = jsonObject.getString("startDate");

                StudyGroup group = new StudyGroup(groupAdmin, groupName, new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)),
                        subject, startDate, startTime);

                groupList.add(group);
            }

            groupListAdapter.notifyDataSetChanged();
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }
}
