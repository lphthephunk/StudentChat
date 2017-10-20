package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cody_.studentchat.Models.StudyGroup;

import java.util.List;

/**
 * Created by Cody_ on 10/19/2017.
 */

public class StudyGroupListAdapter extends RecyclerView.Adapter<StudyGroupHolder> {

    private final List<StudyGroup> studyGroupList;
    private Context context;
    private int itemResource;

    public StudyGroupListAdapter(Context context, int resource, List<StudyGroup> studyGroups){

        // init the adapter
        this.context = context;
        this.itemResource = resource;
        this.studyGroupList = studyGroups;
    }

    @Override
    public StudyGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);

        return new StudyGroupHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(StudyGroupHolder holder, int position) {
        StudyGroup studyGroup = this.studyGroupList.get(position);
        holder.BindStudyGroup(studyGroup);
    }

    @Override
    public int getItemCount() {
        return this.studyGroupList.size();
    }
}
