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

import com.example.cody_.studentchat.Adapters.StudyGroupListAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;

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
            studyGroupList.clear();
            studyGroupList.addAll(Globals.currentUserInfo.getAllJoinedGroups());
            studyAdapter.notifyDataSetChanged();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
