package com.example.cody_.studentchat;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cody_.studentchat.Adapters.MessageListArrayAdapter;
import com.example.cody_.studentchat.Keys.API_Keys;
import com.example.cody_.studentchat.Services.PubNubService;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    String username;
    SharedPreferences sharedPreferences;

    PubNubService chatroomServiceClient;

    ListView messageListView;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatroomServiceClient = (PubNubService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Connection Status: ", "Disconnecting from service");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        final EditText chatMessage = (EditText) findViewById(R.id.messageContentEdit);

        messageListView = (ListView)findViewById(R.id.MessageListView);

        // set default values in the ListView for tesing purposes
        String[] testVals = new String[] {"Message1", "Message2", "Message3", "Message4", "Message5", "Message6", "Message7", "Message8"};
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < testVals.length; i++){
            list.add(testVals[i]);
        }

        final MessageListArrayAdapter adapter = new MessageListArrayAdapter(getApplicationContext(), testVals);
        messageListView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("details", Context.MODE_PRIVATE);
        //username = sharedPreferences.getString("username", null);

        chatMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // check if the send button has been pressed by the user
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    // TODO: send message to group

                    // check for a non-empty message
                    if (chatMessage.getText().toString().trim().length() != 0){
                    // create the message object
                        ChatMessage messageToSend = new ChatMessage();
                        messageToSend.setFrom("Test User"); // TODO: change this to username from sharedPreferences
                        messageToSend.setDeviceTag("android");
                        messageToSend.setSenderUUID("1"); // TODO: later change this to the id of the current user in sharedPreferences...Determine if necessary
                        messageToSend.setType("chatMessage");
                        messageToSend.setMessage(chatMessage.getText().toString());

                        if (chatroomServiceClient.IsInitialized()){
                            chatroomServiceClient.publish(API_Keys.CHANNEL, messageToSend);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No content of message", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        chatroomServiceClient.unSubscribe(API_Keys.CHANNEL);
    }
}
