package com.example.cody_.studentchat.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.orm.SugarApp;
import com.orm.SugarRecord;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class StudyGroup extends SugarRecord {

    @Expose
    public String groupName;
    @Expose
    public String subject;
    @Expose
    public String startDate;
    @Expose
    public String startTime;
    @Expose
    public List<User> groupMembers;
    @Expose
    public String latitude;
    @Expose
    public String longitude;
    @Expose
    public String groupAdmin;

    public StudyGroup(){}

    public StudyGroup(String groupAdmin, String groupName, LatLng location, String subject, String startDate, String startTime){

        this.groupAdmin = groupAdmin;
        this.groupName = groupName;
        this.latitude = String.valueOf(location.latitude);
        this.longitude = String.valueOf(location.longitude);
        this.subject = subject;
        this.startDate = startDate;
        this.startTime = startTime;
        this.groupMembers = new ArrayList<>();
    }

    public void addGroupMember(User groupmember){this.groupMembers.add(groupmember);}

    public String getGroupName(){return this.groupName;}
}
