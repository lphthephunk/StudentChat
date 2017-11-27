package com.example.cody_.studentchat.Models;

import android.provider.BaseColumns;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class User extends SugarRecord {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String email;
    @Expose
    private String uuid;
    @Expose
    private String username;
    @Ignore
    private String jsonJoinedGroups;

    @Ignore
    private transient ArrayList<StudyGroup> joinedGroups = new ArrayList<>();

    public User(){}

    public User(String username, String firstName, String lastName, String email, String uuid){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uuid = uuid;
    }

    public String concatFirstAndLastName(){
        return this.firstName + " " + this.lastName;
    }

    public String getEmail(){
        return this.email;
    }

    public String getUUID(){
        return this.uuid;
    }

    public String getUsername(){return this.username;}

    public String getFirstName(){return this.firstName;}

    public String getLastName(){return this.lastName;}

    public void addGroup(StudyGroup group){
        joinedGroups.add(group);
    }

    public List<StudyGroup> getAllJoinedGroups() {
        joinedGroups.clear();
        try{
            JSONArray jArray = new JSONArray(jsonJoinedGroups);
            for (int i = 0; i<jArray.length(); i++){
                JSONObject jObject = jArray.getJSONObject(i);
                String GroupName = jObject.getString("groupName");
                String GroupAdmin = jObject.getString("groupAdmin");
                String startTime = jObject.getString("startTime");
                String startDate = jObject.getString("startDate");
                String latitude = jObject.getString("latitude");
                String longitude = jObject.getString("longitude");
                String subject = jObject.getString("subject");
                StudyGroup group = new StudyGroup(GroupAdmin, GroupName, new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), subject, startDate,
                        startTime);
                joinedGroups.add(group);
            }
            return joinedGroups;
        }catch(JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public String getJsonJoinedGroups(){return this.jsonJoinedGroups;}

    public void setJsonJoinedGroups(){
        jsonJoinedGroups = new Gson().toJson(joinedGroups, new TypeToken<List<StudyGroup>>(){}.getType());
    }

    public void setRawJsonGroupValue(String json){
        this.jsonJoinedGroups = json;
    }

    public void removeUserFromGroup(StudyGroup group){
        /*for (Iterator<StudyGroup> iterator = joinedGroups.listIterator(); iterator.hasNext();){
            StudyGroup compareGroup = iterator.next();
            if (compareGroup.getLatitude().equals(group.getLatitude()) && compareGroup.getLongitude().equals(group.getLongitude())
                    && compareGroup.getGroupName().equals(group.getGroupName()) && compareGroup.getStartTime().equals(group.getStartTime())
                    && compareGroup.getStartDate().equals(group.getStartDate())){

                iterator.remove();
                return;
            }
        }*/
        try {
            joinedGroups.remove(group.getGroupName());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
