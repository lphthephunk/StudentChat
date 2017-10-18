package com.example.cody_.studentchat.Models;

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
    public String subject;
    @Expose
    public Date startDate;
    @Expose
    public Time startTime;
    @Expose
    public List<User> groupMembers;

    public StudyGroup(){}

    public StudyGroup(String subject, Date startDate, Time startTime, ArrayList<User> groupMembers){

        this.subject = subject;
        this.startDate = startDate;
        this.startTime = startTime;
        this.groupMembers = groupMembers;
    }
}
