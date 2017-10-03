package com.example.cody_.studentchat.Interfaces;

/**
 * Created by Cody_ on 10/3/2017.
 */

public interface IChatroomCalls {

    void onMessage(String message);

    boolean isMessageSent(); // return true if the mssage was sent successfully

    void onPrivateMessage(String message);
}
