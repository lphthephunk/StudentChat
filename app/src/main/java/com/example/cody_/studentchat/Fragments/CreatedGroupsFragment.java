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

import com.example.cody_.studentchat.Adapters.StudyGroupListAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.R;

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
            List<StudyGroup> studyGroups = StudyGroup.findWithQuery(StudyGroup.class,
                    "Select * From STUDY_GROUP Where group_admin = ?", Globals.currentUserInfo.getUsername());

            createdStudyGroups.clear();
            createdStudyGroups.addAll(studyGroups);
            studyGroupListAdapter.notifyDataSetChanged();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
