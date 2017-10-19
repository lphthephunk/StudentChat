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
    public Date startDate;
    @Expose
    public Long startTime;
    @Expose
    public List<User> groupMembers;
    @Expose
    public LatLng location;

    public StudyGroup(){}

    public StudyGroup(String groupName, LatLng location, String subject, Date startDate, Long startTime){

        this.groupName = groupName;
        this.location = location;
        this.subject = subject;
        this.startDate = startDate;
        this.startTime = startTime;
    }
}
