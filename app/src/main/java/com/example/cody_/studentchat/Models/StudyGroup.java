package com.example.cody_.studentchat.Models;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class StudyGroup extends SugarRecord {

    @Expose
    private String groupName;
    @Expose
    private String subject;
    @Expose
    private String startDate;
    @Expose
    private String startTime;
    @Expose
    private String latitude;
    @Expose
    private String longitude;
    @Expose
    private String groupAdmin;
    @Expose
    private String jsonGroupMemberList;

    @Ignore
    private ArrayList<User> groupMembers = new ArrayList<>();

    public StudyGroup(){}

    public StudyGroup(String groupAdmin, String groupName, LatLng location, String subject, String startDate, String startTime){

        this.groupAdmin = groupAdmin;
        this.groupName = groupName;
        this.latitude = String.valueOf(location.latitude);
        this.longitude = String.valueOf(location.longitude);
        this.subject = subject;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public void addGroupMember(User groupmember){
        if (this.groupMembers == null){
            getGroupMembers();
        }
        this.groupMembers.add(groupmember);
    }

    public void removeGroupMember(User groupMember){
        if (this.groupMembers == null){
            getGroupMembers();
        }
        for (Iterator<User> iter = groupMembers.listIterator(); iter.hasNext();){
            User member = iter.next();
            if (member.getUsername().equals(groupMember.getUsername())){
                iter.remove();
                return;
            }
        }
    }

    public void setLatitude(String lat){
        this.latitude = lat;
    }

    public void setLongitude(String lng){
        this.longitude = lng;
    }

    public String getGroupName(){return this.groupName;}

    public String getSubject(){return this.subject;}

    public String getStartDate(){return this.startDate;}

    public String getStartTime(){return this.startTime;}

    public String getLatitude(){return this.latitude;}

    public String getLongitude(){return this.longitude;}

    public List<User> getGroupMembers(){
        groupMembers = new Gson().fromJson(this.jsonGroupMemberList, new TypeToken<List<User>>(){}.getType());
        if (groupMembers == null){
            groupMembers = new ArrayList<>();
        }
        return groupMembers;
    }

    @Override
    public long save(){
        jsonGroupMemberList = new Gson().toJson(groupMembers);
        return super.save();
    }
}
