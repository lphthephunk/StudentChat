package com.example.cody_.studentchat.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Cody_ on 10/12/2017.
 */

public class StudentChatService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StudentChatService(String name) {
        super(name);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Service Status: ", "Started");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }
}
