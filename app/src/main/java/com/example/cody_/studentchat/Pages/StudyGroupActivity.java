package com.example.cody_.studentchat.Pages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.example.cody_.studentchat.Adapters.ChatRoomListAdapter;
import com.example.cody_.studentchat.Adapters.StudyGroupListAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class StudyGroupActivity extends Fragment {

    View view;
    RecyclerView supportGroupRecycler;
    Context context;
    List<StudyGroup> studyGroupList;
    StudyGroupListAdapter studyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_study_group, viewGroup, false);

        initView();

        return view;
    }

    public void initView(){

        context = view.getContext();

        supportGroupRecycler = (RecyclerView)view.findViewById(R.id.studyGroupRecycler);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(context);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        supportGroupRecycler.setLayoutManager(layoutmanager);

        supportGroupRecycler.setHasFixedSize(false);
        supportGroupRecycler.setItemAnimator(new DefaultItemAnimator());

        studyGroupList = new ArrayList<>();

        studyAdapter = new StudyGroupListAdapter(context, R.layout.study_group_adapter, studyGroupList);
        supportGroupRecycler.setAdapter(studyAdapter);

        setStudyGroupList();
    }

    public void setStudyGroupList(){
        // TODO: get all joined groups from current user
        try {
            studyGroupList = Globals.currentUserInfo.getAllJoinedGroups();
            studyAdapter.notifyDataSetChanged();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
