package com.example.cody_.studentchat;

import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.widget.TextView;

public class ChatRoomActivity extends AppCompatActivity {

    String username;

    SharedPreferences sharedPreferences;

    ListView messageListView;
    Gson gson;
    ArrayList<String> chatMessageList;
    MessageListArrayAdapter messageAdapter;
    Pubnub pubnub;
    JSONObject messageObject;

    /*private ServiceConnection serviceConnection = new ServiceConnection() {
        @Overrid
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatroomServiceClient = ((PubNubService.LocalBinder)service).getServiceInstance();
            Log.d("Service Status: ", "Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Connection Status: ", "Disconnecting from service");
            chatroomServiceClient = null;
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //TODO: display a custom action bar with a centered title of chatroom
        setTitle(API_Keys.CHANNEL);
        //getApplicationContext().bindService(new Intent(getApplicationContext(), PubnubException.class), serviceConnection, Context.BIND_AUTO_CREATE);

        final EditText chatMessage = (EditText) findViewById(R.id.messageContentEdit);

        messageListView = (ListView)findViewById(R.id.MessageListView);
        chatMessageList = new ArrayList<>();
        messageAdapter = new MessageListArrayAdapter(getApplicationContext(), chatMessageList);

        messageListView.setAdapter(messageAdapter);

        gson = new Gson();

        pubnub = new Pubnub(API_Keys.PUBLISH_KEY, API_Keys.SUBSCRIBE_KEY);

        try{
            pubnub.subscribe(API_Keys.CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);

                    ChatMessage deserializedMessage = gson.fromJson(message.toString(), ChatMessage.class);

                    chatMessageList.add(deserializedMessage.toString());

                    //messageListView.scrollTo(0, chatMessageList.size() - 1);
                }
                @Override
                public void errorCallback(String channel, PubnubError error){
                    super.errorCallback(channel, error);
                }
                @Override
                public void connectCallback(String channel, Object message){
                    super.connectCallback(channel, message);
                    Log.d("Connect callback: ", message.toString());
                }
                @Override
                public void reconnectCallback(String channel, Object message){
                    super.reconnectCallback(channel, message);
                    Log.d("Reconnect callback: ", message.toString());
                }
                @Override
                public void disconnectCallback(String channel, Object message){
                    super.disconnectCallback(channel, message);
                    Log.d("Disconnect callback: ", message.toString());
                }
            });
        } catch(PubnubException ex){
            Log.d("Subscribe Error: ", ex.getMessage().toString());
        }

        chatMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // check if the send button has been pressed by the user
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    // TODO: send message to group
                    String message = chatMessage.getText().toString().trim();
                    if (message.length() != 0){
                        message = gson.toJson(new ChatMessage("Test User", message));
                        try{
                            messageObject = new JSONObject(message);
                        } catch(JSONException ex){
                            Log.d("Message Conversion: ", ex.getMessage().toString());
                        }
                        // clear out the message input
                        chatMessage.setText("");
                        // publish the message
                        try {
                            pubnub.publish(API_Keys.CHANNEL, messageObject, new Callback() {
                                @Override
                                public void successCallback(String channel, Object message) {
                                    super.successCallback(channel, message);
                                    Log.d("Posted Message: ", message.toString());
                                }
                            });
                        }catch(Exception ex){
                            Log.d("Publish Error: ", ex.getMessage().toString());
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Enter a Message", Toast.LENGTH_SHORT).show();
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
        pubnub.unsubscribe(API_Keys.CHANNEL);
    }
}
