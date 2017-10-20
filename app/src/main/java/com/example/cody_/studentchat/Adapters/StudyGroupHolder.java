package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.R;

/**
 * Created by Cody_ on 10/19/2017.
 */

public class StudyGroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Context context;
    StudyGroup studyGroup;

    TextView groupName;

    public StudyGroupHolder(Context context, View itemView){
        super(itemView);

        this.context = context;

        // TODO: bind UI objects
        groupName = (TextView)itemView.findViewById(R.id.StudyGroupNameTextView);

        itemView.setOnClickListener(this);
    }

    public void BindStudyGroup(StudyGroup studyGroup){

        this.studyGroup = studyGroup;

        // TODO: bind the data to the UI objects
        groupName.setText(studyGroup.getGroupName());
    }

    @Override
    public void onClick(View v) {

    }
}
