package com.example.cody_.studentchat.Models;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

/**
 * Created by Cody_ on 10/13/2017.
 */

public class ChatRoom extends SugarRecord {
    @Expose
    private String RoomName;
    @Expose
    private int personCount;
    @Expose
    private Bitmap chatRoomImage;

    public ChatRoom(String roomName){
        this.RoomName = roomName;
    }

    public ChatRoom(){}

    public String getRoomName(){
        return this.RoomName;
    }

    public int GetPersonCount(){
        return this.personCount;
    }

    public Bitmap getChatRoomImage(){return this.chatRoomImage;}

    public void AddPerson(){
        this.personCount++;
    }

    public void setChatRoomImage(Bitmap chatImage){
        this.chatRoomImage = chatImage;
    }
}
