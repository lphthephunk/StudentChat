package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cody_.studentchat.Models.StudyGroup;

import java.util.List;

/**
 * Created by Cody_ on 11/27/2017.
 */

public class StudyGroupFragAdapter extends RecyclerView.Adapter<StudyGroupFragHolder> {

    private Context context;
    private int resource;
    private List<StudyGroup> groupList;

    public StudyGroupFragAdapter(Context context, int resource, List<StudyGroup> groupList){
        this.context = context;
        this.resource = resource;
        this.groupList = groupList;
    }

    @Override
    public StudyGroupFragHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.resource, parent, false);

        return new StudyGroupFragHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(StudyGroupFragHolder holder, int position) {
        StudyGroup studyGroup = this.groupList.get(position);
        holder.BindStudyGroup(studyGroup);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
