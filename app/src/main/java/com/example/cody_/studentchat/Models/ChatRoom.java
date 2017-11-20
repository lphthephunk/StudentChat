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
    private String roomName;
    @Expose
    private int personCount;
    @Expose
    private Bitmap chatRoomImage;
    @Expose
    private long boundId;

    public ChatRoom(String roomName){
        this.roomName = roomName;
    }

    public ChatRoom(){}

    public long getBoundId(){
        this.boundId = this.getId();
        return this.boundId;
    }

    public String getRoomName(){
        return this.roomName;
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
