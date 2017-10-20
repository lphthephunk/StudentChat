package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Created by Cody_ on 10/18/2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    TextView groupName;
    TextView groupSubject;
    TextView groupStartDate;
    TextView groupStartTime;
    ImageView groupImage;

    private Context context;
    private View CustomView;

    public CustomInfoWindowAdapter(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        CustomView = inflater.inflate(R.layout.custom_infowindow_adapter, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {

        groupName = (TextView)CustomView.findViewById(R.id.GroupNameTextView);
        groupSubject = (TextView)CustomView.findViewById(R.id.SubjectTextView);
        groupStartDate = (TextView)CustomView.findViewById(R.id.StartDateTextView);
        groupStartTime = (TextView)CustomView.findViewById(R.id.StartTimeTextView);
        groupImage = (ImageView)CustomView.findViewById(R.id.groupImage);
        try {
            // get the studygroup data from the db
            String lat = String.valueOf(marker.getPosition().latitude);
            String lng = String.valueOf(marker.getPosition().longitude);

            List<StudyGroup> groupList = StudyGroup.findWithQuery(StudyGroup.class,
                    "SELECT * FROM STUDY_GROUP WHERE latitude = ? AND longitude = ? LIMIT 1", lat, lng);

            if (groupList.size() == 0){
                groupName.setText(marker.getTitle());
                groupSubject.setText("Create and share this group!");
                groupStartDate.setText("");
                groupStartTime.setText("");
            }else {
                StudyGroup group = groupList.get(0);

                // have to do this for long-click flag in StudyFinderActivity
                marker.setTitle(group.groupName);

                groupName.setText(group.groupName);
                groupSubject.setText(group.subject);
                groupStartDate.setText(group.startDate);
                groupStartTime.setText(group.startTime);
                // TODO: SET GROUP IMAGE
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return CustomView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return CustomView;
    }
}
