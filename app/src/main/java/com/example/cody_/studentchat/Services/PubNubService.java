package com.example.cody_.studentchat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cody_ on 10/3/2017.
 */

public class PubNubService extends Service {

    // put these in shared preferences
    private static final String subscribe_key="test"; // TODO: make key
    private static final String publish_key="test"; // TODO: make key

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
            pubnub = new Pubnub(publish_key, subscribe_key, true);

            pubnub.setHeartbeat(10000, heartbeatCallback); // every 10 minutes
            pubnub.setHeartbeatInterval(30);
            pubnub.setResumeOnReconnect(true);
            pubnub.setSubscribeTimeout(20000); // every twenty minutes
            // TODO: Set UUID for pubnub; grab the current users email address or unique identifier for their account
            initialized = true;
        }

        return pubnub;
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
