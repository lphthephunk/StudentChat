package com.example.cody_.studentchat.Models;

import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

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

    public void addGroup(StudyGroup group){
        joinedGroups.add(group);
    }

    public List<StudyGroup> getAllJoinedGroups(){
        return joinedGroups;
    }

    public void removeUserFromGroup(StudyGroup group){
        for (Iterator<StudyGroup> iterator = joinedGroups.listIterator(); iterator.hasNext();){
            StudyGroup compareGroup = iterator.next();
            if (compareGroup.getLatitude().equals(group.getLatitude()) && compareGroup.getLongitude().equals(group.getLongitude())
                    && compareGroup.getGroupName().equals(group.getGroupName()) && compareGroup.getStartTime().equals(group.getStartTime())
                    && compareGroup.getStartDate().equals(group.getStartDate())){

                iterator.remove();
                return;
            }
        }
    }
}
