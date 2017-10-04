package com.example.cody_.studentchat;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by Cody_ on 10/3/2017.
 */

public class ChatMessage {
    public static final String DEVICETAG = "deviceTag"; // what kind of device sent the message
    public static final String TYPE = "type"; // what type of message was this (ie: chat, image, hyperlink, etc.)
    public static final String FROM = "from"; // who is the message from
    public static final String SENTON = "sentOn"; // when was the message sent;
    public static final String EMOTICON = "emoticon"; // was there an emoticon in the message
    public static final String MESSAGECONTENT = "messageContent"; // the actual content of the sent message
    public static final String MESSAGEID = "messageId"; // the unique identifier of the message
    public static final String SENDERUUID = "uuid"; // uuid of the sender

    // message types
    public static final String PRIVATE_CHAT_REQUEST = "privateChatRequest";
    private String type;
    private String messageContnet;
    private String from;
    private Date sentOn;
    private String deviceTag;
    private String emoticon;
    private Drawable avatarImage;
    private String senderUUID;

    private ChatMessage(){

    }

    public String getDeviceTag(){
        return deviceTag;
    }

    public String getType(){
        return type;
    }

    public String getMessageFrom(){
        return from;
    }

    public String getMessageContnet(){
        return messageContnet;
    }
}
