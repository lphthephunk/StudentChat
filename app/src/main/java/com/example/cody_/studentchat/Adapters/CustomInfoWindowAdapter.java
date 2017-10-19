package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cody_.studentchat.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

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

        groupName.setText(marker.getPosition().toString());
        groupSubject.setText(marker.getSnippet().toString());

        return CustomView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return CustomView;
    }
}
