package com.example.cody_.studentchat.Services.binder;

import android.util.Log;

import com.example.cody_.studentchat.ChatMessage;
import com.example.cody_.studentchat.Services.PubNubService;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Cody_ on 10/3/2017.
 */

public class ChatroomClient {

    PubNubService pubnubService;

    public void createRoom(final String roomName){
        if (pubnubService.IsInitialized()) {
            boolean isFound = false;
            String[] currentChannels = pubnubService.getPubnub().getSubscribedChannelsArray();
            for (String channel : currentChannels) {
                if (channel.equals(roomName)) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound){
                try{
                    pubnubService.getPubnub().subscribe(roomName, new Callback() {
                        @Override
                        public void successCallback(String channel, Object message) {
                            try{
                                Log.d("Message Received: ", "Received on channel " + channel);

                                if (message instanceof JSONObject){
                                    JSONObject jsonResponse = (JSONObject) message;
                                }
                            }
                            catch(Exception ex){
                                Log.d("Room Creation: ", ex.getMessage().toString());
                            }
                        }
                        @Override
                        public void errorCallback(String channel, PubnubError error){
                            Log.d("Error Subscribing: ", error.toString());
                        }
                    });
                }
                catch(Exception ex){
                    Log.d("Room Creation: ", ex.getMessage().toString());
                }
            }
        }
    }

    public void joinRoom(final String roomName, final double studentPassword){

    }

    public void publish(String channel, ChatMessage message){
        try{
            JSONObject messageJSON = new JSONObject();
            messageJSON.put(ChatMessage.DEVICETAG, message.getDeviceTag());
            messageJSON.put(ChatMessage.SENDERUUID, pubnubService.getPubnub().getUUID());
            messageJSON.put(ChatMessage.EMOTICON, "");
            messageJSON.put(ChatMessage.FROM, message.getMessageFrom());
            messageJSON.put(ChatMessage.SENTON, new Date());
            messageJSON.put(ChatMessage.TYPE, message.getType());
            messageJSON.put(ChatMessage.MESSAGECONTENT, message.getMessageContnet());

            pubnubService.getPubnub().publish(channel, messageJSON, true, new Callback(){
                @Override
                public void successCallback(String channel, Object message){
                    Log.d("Publish Status: ", "Success");
                }
                @Override
                public void errorCallback(String channel, PubnubError error){
                    Log.d("Publish Error", error.toString());
                }
            });
        } catch(Exception ex){
            Log.d("Publish Error: ", ex.getMessage().toString());
        }
    }
}
