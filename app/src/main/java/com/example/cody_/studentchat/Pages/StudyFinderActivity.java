package com.example.cody_.studentchat.Pages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cody_.studentchat.Adapters.CustomInfoWindowAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.pubnub.api.models.consumer.history.PNHistoryResult;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static android.content.Context.LOCATION_SERVICE;

public class StudyFinderActivity extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleMap mMap;
    LocationManager locationManager;
    Location location;

    LatLng previousLocation;

    List<Marker> markerList = new ArrayList<>();

    GridLayout GroupEntryGrid;
    Button GroupSubmitBtn;
    Button GroupCancelBtn;

    EditText groupNameEdit;
    EditText subjectEdit;
    EditText groupDateEdit;
    EditText groupTimeEdit;

    // location of current pin binding a group to
    LatLng currentlySelectedLocation;
    Marker currentlySelectedMarker;

    boolean IsInfoWindowShownOverride = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_study_finder, viewGroup, false);

        GroupEntryGrid = (GridLayout)view.findViewById(R.id.GroupEntryGrid);
        GroupSubmitBtn = (Button)view.findViewById(R.id.GroupSubmitBtn);
        GroupCancelBtn = (Button)view.findViewById(R.id.GroupCancelBtn);

        // entry points for group info
        groupNameEdit = (EditText)view.findViewById(R.id.GroupNameEntry);
        subjectEdit = (EditText)view.findViewById(R.id.SubjectEntry);
        groupDateEdit = (EditText)view.findViewById(R.id.StartDateEntry);
        groupTimeEdit = (EditText)view.findViewById(R.id.StartTimeEntry);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        ft.add(R.id.map, fragment);
        ft.commit();

        fragment.getMapAsync(this);

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        CustomInfoWindowAdapter customAdapter = new CustomInfoWindowAdapter(getContext());
        mMap.setInfoWindowAdapter(customAdapter);

        getAllStudyGroupMarkers();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // TODO: show popup allowing user to enter data about study group

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // close most recent marker popup if not already closed
                if (currentlySelectedMarker != null){
                    currentlySelectedMarker.hideInfoWindow();
                }

                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("New Study Group")
                        .snippet(""));
                markerList.add(marker);
                marker.setDraggable(true);
            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                previousLocation = marker.getPosition();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                try {
                    // TODO: beef up this code in the future to handle multiple study groups at the same location
                    List<StudyGroup> groupList = StudyGroup.findWithQuery(StudyGroup.class, "Select * from STUDY_GROUP Where latitude = ? AND longitude = ? LIMIT 1",
                            String.valueOf(previousLocation.latitude), String.valueOf(previousLocation.longitude));

                    if (groupList.size() > 0) {
                        StudyGroup myGroup = groupList.get(0);

                        myGroup.latitude = String.valueOf(marker.getPosition().latitude);
                        myGroup.longitude = String.valueOf(marker.getPosition().longitude);

                        myGroup.save();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    if (!IsInfoWindowShownOverride) {
                        marker.showInfoWindow();
                        IsInfoWindowShownOverride = true;
                    }else{
                        marker.hideInfoWindow();
                        IsInfoWindowShownOverride = false;
                        return true;
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                return false;
            }
        });

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                currentlySelectedLocation = marker.getPosition();
                currentlySelectedMarker = marker;
                String title = marker.getTitle().toString();
                // TODO: if current user is admin of group, allow them to edit or create
                if (title.equals("New Study Group")) {
                    GroupEntryGrid.setVisibility(View.VISIBLE);
                }else{
                    // TODO: else, give the user the option to join the room
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Join Group")
                            .setMessage("Are you sure you want to join this group?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        List<StudyGroup> groupList = StudyGroup.findWithQuery(StudyGroup.class, "Select * From STUDY_GROUP Where latitude = ? AND longitude = ? LIMIT 1",
                                                String.valueOf(currentlySelectedLocation.latitude), String.valueOf(currentlySelectedLocation.longitude));
                                        StudyGroup group = groupList.get(0);

                                        if (group.groupMembers == null){
                                            group.groupMembers = new ArrayList<User>();
                                        }

                                        group.addGroupMember(Globals.currentUserInfo);
                                        group.save();

                                        Globals.currentUserInfo.addGroup(group);
                                        // TODO: update the phpmyadmin db
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                try {
                    location = null;
                    location = getLocation();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                return false;
            }
        });

        GroupSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // check if fields have all been filled
                    if (groupNameEdit.getText().toString().isEmpty()) {
                        groupNameEdit.setHint("*Group Name Required");
                        groupNameEdit.setHintTextColor(Color.RED);
                    }
                    if (subjectEdit.getText().toString().isEmpty()) {
                        subjectEdit.setHint("*Subject Required");
                        subjectEdit.setHintTextColor(Color.RED);
                    }
                    if (groupDateEdit.getText().toString().isEmpty()) {
                        groupDateEdit.setHint("*Date Required");
                        groupDateEdit.setHintTextColor(Color.RED);
                    }
                    if (groupTimeEdit.getText().toString().isEmpty()) {
                        groupTimeEdit.setHint("*Time Required");
                        groupTimeEdit.setHintTextColor(Color.RED);
                    } else if (!groupNameEdit.getText().toString().isEmpty()
                            && !subjectEdit.getText().toString().isEmpty()
                            && !groupDateEdit.getText().toString().isEmpty()
                            && !groupTimeEdit.getText().toString().isEmpty()) {

                        // save the group data in the sqlite db
                        StudyGroup newGroup = new StudyGroup(Globals.currentUserInfo.getUsername(), groupNameEdit.getText().toString(), currentlySelectedLocation, subjectEdit.getText().toString(),
                                groupDateEdit.getText().toString(), groupTimeEdit.getText().toString());

                        newGroup.save();

                        GroupEntryGrid.setVisibility(View.GONE);
                        currentlySelectedMarker.hideInfoWindow();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        GroupCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupEntryGrid.setVisibility(View.GONE);
            }
        });

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public Location getLocation(){
        int MIN_TIME_BTWN_UPDATES = 5000;
        int MIN_DISTANCE_CHANGE_FOR_UPDATES = 10000;

        try{
            locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);

            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean isPassiveEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGpsEnabled || isPassiveEnabled || isNetworkEnabled){

                if (isGpsEnabled && location == null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTWN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null){
                        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
                else if (isPassiveEnabled && location == null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTWN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null){
                        return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                }
                else if (isNetworkEnabled && location == null){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BTWN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null){
                        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }else{
                return null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    private void getAllStudyGroupMarkers(){
        final List<StudyGroup> groupList = StudyGroup.listAll(StudyGroup.class);
        for (StudyGroup group : groupList) {
            double lat = Double.valueOf(group.latitude);
            double lng = Double.valueOf(group.longitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
            markerList.add(marker);
        }
    }

    @Override
    public boolean onMyLocationButtonClick(){
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        /*mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        location = getLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
