package com.example.cody_.studentchat;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cody_.studentchat.Adapters.ChatMessageAdapter;
import com.example.cody_.studentchat.Adapters.CustomDivider;
import com.example.cody_.studentchat.Adapters.ShadowSpaceItemDecorator;
import com.example.cody_.studentchat.Adapters.VerticalSpacesChat;
import com.example.cody_.studentchat.Keys.API_Keys;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.RunnableFuture;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ChatRoomActivity extends AppCompatActivity {

    String username;
    SharedPreferences sharedPreferences;

    RecyclerView messageRecyclerView;
    Gson gson;
    ArrayList<ChatMessage> chatMessageList;
    ChatMessageAdapter messageAdapter;
    Pubnub pubnub;
    JSONObject messageObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setTitle(API_Keys.CHANNEL);

        final EditText messageEntryText = (EditText) findViewById(R.id.messageContentEdit);

        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        // set the manager for the recycler view
        messageRecyclerView.setLayoutManager(linearLayoutManager);

        // add space to the adapter
        messageRecyclerView.addItemDecoration(new VerticalSpacesChat(5));

        messageRecyclerView.setHasFixedSize(false);
        messageRecyclerView.setItemAnimator(new DefaultItemAnimator());

        chatMessageList = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(this, R.layout.message_recycler_adapter, chatMessageList);

        messageRecyclerView.setAdapter(messageAdapter);

        gson = new Gson();

        try{
            pubnub.subscribe(API_Keys.CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);

                    ChatMessage deserializedMessage = gson.fromJson(message.toString(), ChatMessage.class);

                    chatMessageList.add(deserializedMessage);

                    runOnUiThread(updateRecycler);

                    messageRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
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

        try{
            pubnub.history(API_Keys.CHANNEL, 100, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);

                    parseJson(message.toString());

                    runOnUiThread(updateRecycler);
                }
            });
        }catch(Exception ex){
            Log.d("History Error: ", ex.getMessage().toString());
        }

        messageEntryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // check if the send button has been pressed by the user
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    // TODO: send message to group
                    String message = messageEntryText.getText().toString().trim();
                    if (message.length() != 0){
                        message = gson.toJson(new ChatMessage("Test User", message));
                        try{
                            messageObject = new JSONObject(message);
                        }catch(Exception ex){
                            Log.d("Json conversion: ", ex.getMessage());
                        }
                        // clear the message input
                        messageEntryText.setText("");
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

    private Runnable updateRecycler = new Runnable() {
        @Override
        public void run() {
            messageAdapter.notifyDataSetChanged();
        }
    };

    private void parseJson(String jsonStr) {
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONArray innerJsonArray = jsonArray.getJSONArray(0);
            for(int i = 0; i < innerJsonArray.length(); i++) {
                JSONObject jsonObject = innerJsonArray.getJSONObject(i);
                String msg = jsonObject.getString("message");
                String user = jsonObject.getString("username");

                ChatMessage message = new ChatMessage(user, msg);
                chatMessageList.add(message);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        pubnub.unsubscribe(API_Keys.CHANNEL);
    }
}
