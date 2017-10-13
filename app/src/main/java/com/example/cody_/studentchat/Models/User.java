package com.example.cody_.studentchat.Models;

import android.provider.BaseColumns;

import com.orm.SugarRecord;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class User extends SugarRecord<User> {

    String firstName;
    String lastName;
    String email;
    String uuid;

    public User(){}

    public User(String firstName, String lastName, String email, String uuid){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uuid = uuid;
    }
}
