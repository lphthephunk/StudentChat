package com.example.cody_.studentchat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cody_.studentchat.ChatMessage;
import com.example.cody_.studentchat.Keys.API_Keys;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cody_ on 10/3/2017.
 */

public class PubNubService extends Service {

    private final Callback heartbeatCallback = new Callback() {
        @Override
        public void successCallback(String channel, Object message) {
            Log.d("heartbeat callback", message.toString());
        }
        @Override
        public void errorCallback(String message, PubnubError error){
            // TODO: process the error
        }
    };

    private final Map<String, List<Callback>> listeners = new HashMap<>();

    private boolean initialized;

    public void SetInitialized(boolean initialized){this.initialized = initialized;}

    public boolean IsInitialized() {return initialized;}

    private Pubnub pubnub;

    public PubNubService(){}

    public Pubnub getPubnub(){
        if (null == pubnub){
            // initialize single instance of pubnub if none exist
            pubnub = new Pubnub(API_Keys.PUBLISH_KEY, API_Keys.SUBSCRIBE_KEY, true);

            pubnub.setHeartbeat(10000, heartbeatCallback); // every 10 minutes
            pubnub.setHeartbeatInterval(30);
            pubnub.setResumeOnReconnect(true);
            pubnub.setSubscribeTimeout(20000); // every twenty minutes
            // TODO: Set UUID for pubnub; grab the current users email address or unique identifier for their account
            initialized = true;
        }

        return pubnub;
    }

    public void unSubscribe(String channel){
        if (initialized){
            pubnub.unsubscribe(channel);
        }
    }

    public void createRoom(final String roomName){
        if (this.IsInitialized()) {
            boolean isFound = false;
            String[] currentChannels = this.getPubnub().getSubscribedChannelsArray();
            for (String channel : currentChannels) {
                if (channel.equals(roomName)) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound){
                try{
                    this.getPubnub().subscribe(roomName, new Callback() {
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
            messageJSON.put(ChatMessage.SENDERUUID, this.getPubnub().getUUID());
            messageJSON.put(ChatMessage.EMOTICON, "");
            messageJSON.put(ChatMessage.FROM, message.getMessageFrom());
            messageJSON.put(ChatMessage.SENTON, new Date());
            messageJSON.put(ChatMessage.TYPE, message.getType());
            messageJSON.put(ChatMessage.MESSAGECONTENT, message.getMessageContent());

            this.getPubnub().publish(channel, messageJSON, true, new Callback(){
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

    IBinder serviceBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public class LocalBinder extends Binder{
        public PubNubService getServiceInstance(){
            return PubNubService.this;
        }
    }
}
