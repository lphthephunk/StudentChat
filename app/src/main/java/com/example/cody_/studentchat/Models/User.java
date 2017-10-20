package com.example.cody_.studentchat.Models;

import android.provider.BaseColumns;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class User extends SugarRecord {

    String firstName;
    String lastName;
    String email;
    String uuid;
    String username;
    List<StudyGroup> joinedGroups;

    public User(){}

    public User(String username, String firstName, String lastName, String email, String uuid){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uuid = uuid;
        this.joinedGroups = new ArrayList<>();
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

    public List<StudyGroup> getAllJoinedGroups() {return this.joinedGroups;}
}
