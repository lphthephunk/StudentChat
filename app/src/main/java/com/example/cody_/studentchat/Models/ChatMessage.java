package com.example.cody_.studentchat.Models;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Cody_ on 10/3/2017.
 */

public class ChatMessage {

    // desearlized json will be bound to these two strings

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;
    @SerializedName("startTimeToken")
    String startTime;
    @SerializedName("endTimeToken")
    String endTime;


    public ChatMessage(String username, String message){
        this.username = username;
        this.message = message;
    }

    public String getUsername(){
        return username;
    }

    public String getMessage(){
        return message;
    }
}
