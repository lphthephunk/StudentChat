package com.example.cody_.studentchat.Pages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Adapters.CustomInfoWindowAdapter;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.StudyGroupRequests.DeleteStudyGroup;
import com.example.cody_.studentchat.Services.StudyGroupRequests.GetSingleStudyGroupService;
import com.example.cody_.studentchat.Services.StudyGroupRequests.GetStudyGroupsService;
import com.example.cody_.studentchat.Services.StudyGroupRequests.InsertStudyGroup;
import com.example.cody_.studentchat.Services.StudyGroupRequests.UpdateStudyGroupService;
import com.example.cody_.studentchat.Services.UserRequests.UpdateUserService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    Marker quickTapCurrentMarker;

    GridLayout iconOptions;
    Button deleteGroupBtn;

    StudyGroup currentlySelectedGroup;

    boolean IsInfoWindowShownOverride = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_study_finder, viewGroup, false);

        GroupEntryGrid = (GridLayout)view.findViewById(R.id.GroupEntryGrid);
        GroupSubmitBtn = (Button)view.findViewById(R.id.GroupSubmitBtn);
        GroupCancelBtn = (Button)view.findViewById(R.id.GroupCancelBtn);

        iconOptions = (GridLayout)view.findViewById(R.id.iconOptions);
        deleteGroupBtn = (Button)view.findViewById(R.id.DeleteGroupButton);

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
                // close the icon options pane when the user clicks any blank map area
                iconOptions.setVisibility(View.GONE);
                if (quickTapCurrentMarker != null){
                    IsInfoWindowShownOverride = false;
                    quickTapCurrentMarker.hideInfoWindow();
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // close most recent marker popup if not already closed
                if (currentlySelectedMarker != null) {
                    currentlySelectedMarker.hideInfoWindow();
                }

                quickTapCurrentMarker = CreateMarker(latLng.latitude, latLng.longitude);

                currentlySelectedLocation = quickTapCurrentMarker.getPosition();
                currentlySelectedMarker = quickTapCurrentMarker;
                String title = quickTapCurrentMarker.getTitle().toString();

                if (title.equals("New Study Group")) {
                    OpenEntryGrid();
                }
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

                        myGroup.setLatitude(String.valueOf(marker.getPosition().latitude));
                        myGroup.setLongitude(String.valueOf(marker.getPosition().longitude));

                        myGroup.save();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                try {
                    currentlySelectedLocation = marker.getPosition();
                    if (iconOptions.getVisibility() == View.VISIBLE){
                        iconOptions.setVisibility(View.GONE);
                    }
                    quickTapCurrentMarker = marker;

                    ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    // if there is a network connection, query the server for the study group info
                    if (networkInfo != null && networkInfo.isConnected() && !IsInfoWindowShownOverride) {
                        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");


                                    if (success){
                                        String groupAdmin = jsonResponse.getString("groupAdmin");
                                        String latitude = jsonResponse.getString("latitude");
                                        String longitude = jsonResponse.getString("longitude");
                                        String subject = jsonResponse.getString("subject");
                                        String groupName = jsonResponse.getString("groupName");
                                        String startDate = jsonResponse.getString("startDate");
                                        String startTime = jsonResponse.getString("startTime");
                                        //String groupMembersJson = new Gson().toJson(jsonResponse.getString("groupMembers"));
                                        String groupMembers = jsonResponse.getString("groupMembers");

                                        if (latitude.equals("null") || longitude.equals("null")){
                                            OpenEntryGrid();
                                            return;
                                        }
                                        StudyGroup group = new StudyGroup(groupAdmin, groupName,
                                                new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), subject, startDate, startTime);

                                        group.setJsonGroupMemberList(groupMembers);

                                        currentlySelectedGroup = group;

                                        if (!IsInfoWindowShownOverride) {
                                            if ((group != null && group.getGroupAdmin().equals(Globals.currentUserInfo.getUsername()))
                                                    || marker.getTitle().equals("New Study Group")) {
                                                iconOptions.setVisibility(View.VISIBLE);
                                            }
                                            marker.showInfoWindow();
                                            IsInfoWindowShownOverride = true;
                                        } else {
                                            iconOptions.setVisibility(View.GONE);
                                            marker.hideInfoWindow();
                                            IsInfoWindowShownOverride = false;
                                        }
                                    }else{
                                        Toast.makeText(getContext(), "Unable to access this Study Group at the moment", Toast.LENGTH_SHORT).show();
                                    }
                                }catch(JSONException ex){
                                    ex.printStackTrace();
                                    Toast.makeText(getContext(), "Unable to access this Study Group at the moment", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        GetSingleStudyGroupService getSingleStudyGroupService = new GetSingleStudyGroupService(marker.getPosition(), ResponseListener);
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(getSingleStudyGroupService);
                    }else if (networkInfo != null && !networkInfo.isConnected()){

                        List<StudyGroup> groupList = StudyGroup.findWithQuery(StudyGroup.class,
                                "Select * From STUDY_GROUP Where latitude = ? AND longitude = ? LIMIT 1",
                                String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude));
                        StudyGroup group;
                        if (groupList.size() == 0) {
                            group = null;
                            currentlySelectedGroup = null;
                        } else {
                            group = groupList.get(0);
                            currentlySelectedGroup = group;
                        }

                        if (!IsInfoWindowShownOverride) {
                            if ((group != null && group.getGroupAdmin().equals(Globals.currentUserInfo.getUsername()))
                                    || marker.getTitle().equals("New Study Group")) {
                                iconOptions.setVisibility(View.VISIBLE);
                            }
                            marker.showInfoWindow();
                            IsInfoWindowShownOverride = true;
                        } else {
                            iconOptions.setVisibility(View.GONE);
                            marker.hideInfoWindow();
                            IsInfoWindowShownOverride = false;
                            return true;
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                return true;
            }
        });

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                try {
                    final StudyGroup group = currentlySelectedGroup;

                    if (!isInThisGroup(group)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Join Group")
                                .setMessage("Are you sure you want to join this group?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            group.addGroupMember(Globals.currentUserInfo);
                                            group.save();

                                            Globals.currentUserInfo.addGroup(group);

                                            Globals.currentUserInfo.setJsonJoinedGroups();

                                            // update the user in php my admin
                                            Response.Listener<String> UserResponseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonResponse = new JSONObject(response);
                                                        boolean success = jsonResponse.getBoolean("success");

                                                        if (success) {
                                                            // update the studygroup table to reflect the newly joined user
                                                            Response.Listener<String> GroupResponseListener = new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try {
                                                                        JSONObject jsonResponse = new JSONObject(response);
                                                                        boolean success = jsonResponse.getBoolean("success");

                                                                        if (success) {
                                                                            Toast.makeText(getContext(), "Successfully joined the group", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                                            builder.setTitle("Oops!")
                                                                                    .setMessage("We had a problem adding you to " + group.getGroupName() + " Please check your internet connection " +
                                                                                            "and try again.")
                                                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            // remove the user from the sqlite saved group
                                                                                            group.removeGroupMember(Globals.currentUserInfo);
                                                                                            Globals.currentUserInfo.removeUserFromGroup(group);
                                                                                        }
                                                                                    });
                                                                            AlertDialog dialog = builder.create();
                                                                            dialog.show();
                                                                        }
                                                                    } catch (JSONException ex) {
                                                                        ex.printStackTrace();
                                                                    }
                                                                }
                                                            };
                                                            UpdateStudyGroupService updateStudyGroupService = new UpdateStudyGroupService(group, GroupResponseListener);
                                                            RequestQueue queue = Volley.newRequestQueue(getContext());
                                                            queue.add(updateStudyGroupService);
                                                        } else {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            builder.setTitle("Oops!")
                                                                    .setMessage("We had a problem trying to update your joined study groups on the server. Please check your " +
                                                                            "internet connection and try again.")
                                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            // do nothing
                                                                        }
                                                                    });
                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                        }
                                                    } catch (JSONException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            };
                                            UpdateUserService updateUserService = new UpdateUserService(Globals.currentUserInfo, UserResponseListener);
                                            RequestQueue queue = Volley.newRequestQueue(getContext());
                                            queue.add(updateUserService);
                                        } catch (Exception ex) {
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
                    } else {
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Already Joined")
                                    .setMessage("You have already joined this study group. Would you like to leave it?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // remove user from this study group
                                            group.removeGroupMember(Globals.currentUserInfo);
                                            Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonResponse = new JSONObject(response);
                                                        boolean success = jsonResponse.getBoolean("success");
                                                        if (success){
                                                            Globals.currentUserInfo.removeUserFromGroup(group);
                                                            Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try{
                                                                        JSONObject jsonResponse = new JSONObject(response);
                                                                        boolean success = jsonResponse.getBoolean("success");

                                                                        if (success){
                                                                            Globals.currentUserInfo.setJsonJoinedGroups();
                                                                            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            // do nothing
                                                                        }
                                                                    }catch(JSONException ex){
                                                                        ex.printStackTrace();
                                                                    }
                                                                }
                                                            };
                                                            UpdateUserService updateUserService = new UpdateUserService(Globals.currentUserInfo, ResponseListener);
                                                            RequestQueue queue = Volley.newRequestQueue(getContext());
                                                            queue.add(updateUserService);
                                                        }else{
                                                            // do nothing
                                                        }
                                                    }catch(JSONException ex){
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            };
                                            UpdateStudyGroupService updateStudyGroupService = new UpdateStudyGroupService(group, ResponseListener);
                                            RequestQueue queue = Volley.newRequestQueue(getContext());
                                            queue.add(updateStudyGroupService);

                                            Globals.currentUserInfo.removeUserFromGroup(group);

                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
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
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success){
                                        // save the group data in the sqlite db
                                        StudyGroup newGroup = new StudyGroup(Globals.currentUserInfo.getUsername(), groupNameEdit.getText().toString(), currentlySelectedLocation, subjectEdit.getText().toString(),
                                                groupDateEdit.getText().toString(), groupTimeEdit.getText().toString());

                                        newGroup.save();
                                        Globals.currentUserInfo.addGroup(newGroup);

                                        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");

                                                    if (success){
                                                        Toast.makeText(getContext(), "Info updated", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle("Oops!")
                                                                .setMessage("There was a problem updating your info to join this group. Please check your network " +
                                                                        "connection and try again")
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // do nothing
                                                                    }
                                                                });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                }catch(JSONException ex){
                                                    ex.printStackTrace();
                                                }
                                            }
                                        };
                                        UpdateUserService updateUserService = new UpdateUserService(Globals.currentUserInfo, ResponseListener);
                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                        queue.add(updateUserService);

                                        GroupEntryGrid.setVisibility(View.GONE);
                                        currentlySelectedMarker.hideInfoWindow();
                                        IsInfoWindowShownOverride = false;
                                        currentlySelectedMarker.setTitle(newGroup.getGroupName());
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Oops!")
                                                .setMessage("There was an issue connecting to the server. We were unable to " +
                                                        "create a new Study Group. Check your internet connection and try again.")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // do nothing
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                        currentlySelectedMarker.hideInfoWindow();
                                        IsInfoWindowShownOverride = false;
                                        iconOptions.setVisibility(View.GONE);
                                    }
                                }catch(JSONException ex){
                                    ex.printStackTrace();
                                }
                            }
                        };
                        InsertStudyGroup insertStudyGroup = new InsertStudyGroup(groupNameEdit.getText().toString(), subjectEdit.getText().toString(),
                                groupDateEdit.getText().toString(), groupTimeEdit.getText().toString(), String.valueOf(currentlySelectedLocation.latitude), String.valueOf(currentlySelectedLocation.longitude),
                                Globals.currentUserInfo.getUsername(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(insertStudyGroup);
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

        deleteGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Verify")
                        .setMessage("Are you sure you want to delete this Study Group?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (currentlySelectedGroup == null) {
                                        // remove the pin from the map
                                        markerList.remove(quickTapCurrentMarker);
                                        quickTapCurrentMarker.remove();
                                        iconOptions.setVisibility(View.GONE);
                                    } else {
                                        final Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    final JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");

                                                    if (success) {
                                                        List<StudyGroup> groupList = StudyGroup.findWithQuery(StudyGroup.class,
                                                                "SELECT * FROM STUDY_GROUP WHERE latitude = ? AND longitude = ? LIMIT 1",
                                                                String.valueOf(quickTapCurrentMarker.getPosition().latitude), String.valueOf(quickTapCurrentMarker.getPosition().longitude));

                                                        StudyGroup group = groupList.get(0);
                                                        group.delete();
                                                        markerList.remove(quickTapCurrentMarker);
                                                        iconOptions.setVisibility(View.GONE);
                                                        quickTapCurrentMarker.remove();
                                                        IsInfoWindowShownOverride = false;

                                                        Globals.currentUserInfo.removeUserFromGroup(group);

                                                        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try{
                                                                    JSONObject jsonResponse = new JSONObject(response);
                                                                    boolean success = jsonResponse.getBoolean("success");

                                                                    if (success){
                                                                        Toast.makeText(getContext(), "Deleted " + quickTapCurrentMarker.getTitle(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }catch(JSONException ex){
                                                                    ex.printStackTrace();;
                                                                }
                                                            }
                                                        };
                                                        UpdateUserService updateUserService = new UpdateUserService(Globals.currentUserInfo, ResponseListener);
                                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                                        queue.add(updateUserService);
                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle("Oops!")
                                                                .setMessage("There was a problem deleting this Study Group. Check your internet connection" +
                                                                        " and try again.")
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // do nothing
                                                                    }
                                                                });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                } catch (JSONException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        };
                                        DeleteStudyGroup deleteStudyGroup = new DeleteStudyGroup(currentlySelectedGroup, ResponseListener);
                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                        queue.add(deleteStudyGroup);
                                    }
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

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
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            // get study group markers from the server
            Response.Listener<String> ResponseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray jsonArray = new JSONArray(response);

                        boolean success = jsonArray.getJSONObject(0).getBoolean("success");

                        if (success){
                            int length = jsonArray.length();
                            for (int i = 0; i < length; i++){
                                double lat = Double.valueOf(jsonArray.getJSONObject(i).getString("latitude"));
                                double lng = Double.valueOf(jsonArray.getJSONObject(i).getString("longitude"));
                                CreateMarker(lat, lng);
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Oops!")
                                    .setMessage("We were unable to populate the map with markers. There must be an issue on our end. Please try again shortly.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }
            };
            GetStudyGroupsService getStudyGroupsService = new GetStudyGroupsService(ResponseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(getStudyGroupsService);
        }else {
            // get the markers locally from what is saved on the phone
            final List<StudyGroup> groupList = StudyGroup.listAll(StudyGroup.class);
            for (StudyGroup group : groupList) {
                double lat = Double.valueOf(group.getLatitude());
                double lng = Double.valueOf(group.getLongitude());
                CreateMarker(lat, lng);
            }
        }
    }

    /* Checks if the user is already in the study group that has been selected
    * */
    private boolean isInThisGroup(StudyGroup group){
        try {
            List<String> userList = group.getGroupMembers();
            for (Iterator<String> iter = userList.listIterator(); iter.hasNext(); ) {
                String user = iter.next();
                if (user.equals(Globals.currentUserInfo.getUsername())) {
                    return true;
                }
            }
            return false; // default return value if not in the group
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private Marker CreateMarker(double lat, double lng){
        markerList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                .title("New Study Group")
                .snippet("")
                .draggable(true)));

        return markerList.get(markerList.size() - 1);
    }

    private void OpenEntryGrid(){
        GroupEntryGrid.setVisibility(View.VISIBLE);
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
