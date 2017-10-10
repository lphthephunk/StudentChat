package com.example.cody_.studentchat;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Cody_ on 10/3/2017.
 */

public class ChatMessage {

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;

    public ChatMessage(String username, String message){
        this.message = message;
        this.username = username;
    }

    public String toString(){
        return getUsername() + ": " + getMessage();
    }

    public String getUsername(){
        return username;
    }

    public String getMessage(){
        return message;
    }
}
