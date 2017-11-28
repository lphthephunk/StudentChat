package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.R;

/**
 * Created by Cody_ on 11/27/2017.
 */

public class StudyGroupFragHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    StudyGroup studyGroup;
    TextView groupName;

    public StudyGroupFragHolder(Context context, View itemView){
        super(itemView);

        this.context = context;
       // this.messageEntryBar = (EditText)itemView.findViewById(R.id.messageContentEdit);
        groupName = (TextView)itemView.findViewById(R.id.StudyGroupNameTextView);


        itemView.setOnClickListener(this);
    }

    public void BindStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
        groupName.setText(studyGroup.getGroupName());
    }

    @Override
    public void onClick(View v){
        Globals.GroupToShare = this.studyGroup;
        v.setBackgroundColor(Color.BLUE);
    }
}
