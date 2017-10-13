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
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Keys.API_Keys;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.pubnub.api.Callback;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.RunnableFuture;

import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ChatRoomActivity extends AppCompatActivity {

    String username;
    SharedPreferences sharedPreferences;

    RecyclerView messageRecyclerView;
    Gson gson;
    ArrayList<ChatMessage> chatMessageList;
    ChatMessageAdapter messageAdapter;
    PubNub pubnub;
    JSONObject messageObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setTitle(Globals.CHANNEL);

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

        // make new pubnub instance
        getPubnub();

        try{
            pubnub.subscribe()
            .channels(Arrays.asList(Globals.CHANNEL))
            .execute();

            pubnub.addListener(new SubscribeCallback() {
                @Override
                public void status(PubNub pubnub, PNStatus status) {

                }

                @Override
                public void message(PubNub pubnub, final PNMessageResult message) {
                    Runnable action = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ChatMessage deserializedMessage = gson.fromJson(message.getMessage().getAsString(), ChatMessage.class);

                                chatMessageList.add(deserializedMessage);

                                messageRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    };
                    runOnUiThread(action);
                    runOnUiThread(updateRecycler);
                }

                @Override
                public void presence(PubNub pubnub, PNPresenceEventResult presence) {

                }
            });
        } catch(Exception ex){
            ex.printStackTrace();
        }

        try{
            pubnub.history()
                    .channel(Globals.CHANNEL)
                    .count(100)
                    .includeTimetoken(false)
                    .async(new PNCallback<PNHistoryResult>() {
                        @Override
                        public void onResponse(final PNHistoryResult result, PNStatus status) {
                            Runnable action = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (result != null) {
                                            List<PNHistoryItemResult> messages = result.getMessages();
                                            for (PNHistoryItemResult item:messages){
                                                try{
                                                    parseJson(item.getEntry().getAsString());
                                                }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            };
                            runOnUiThread(action);
                            runOnUiThread(updateRecycler);
                        }
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }

        messageEntryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // check if the send button has been pressed by the user
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    // TODO: send message to group
                    String message = messageEntryText.getText().toString().trim();
                    if (message.length() != 0){
                        message = gson.toJson(new ChatMessage(Globals.currentUserInfo.concatFirstAndLastName(), message));
                        try{
                            messageObject = new JSONObject(message);
                        }catch(Exception ex){
                            Log.d("Json conversion: ", ex.getMessage());
                        }
                        // clear the message input
                        messageEntryText.setText("");
                        // publish the message
                        try{
                            pubnub.publish()
                                    .channel(Globals.CHANNEL)
                                    .message(message)
                                    .async(new PNCallback<PNPublishResult>(){
                                        @Override
                                        public void onResponse(PNPublishResult result, PNStatus status) {
                                            //Log.d("Message Response: ", result.toString());
                                        }
                                    });
                        }catch(Exception ex){
                            ex.printStackTrace();
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

    protected void getPubnub(){
        if (pubnub == null){
            pubnub = new PubNub(setupPubnubConfig());
        }
    }

    public PNConfiguration setupPubnubConfig(){
        try{
            PNConfiguration configuration = new PNConfiguration();
            Properties prop = new Properties();

            configuration.setPublishKey(API_Keys.PUBLISH_KEY);
            configuration.setSubscribeKey(API_Keys.SUBSCRIBE_KEY);
            configuration.setUuid(Globals.currentUserInfo.getUUID());
            //configuration.setSecure(true);
            configuration.setLogVerbosity(PNLogVerbosity.BODY);

            return configuration;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private Runnable updateRecycler = new Runnable() {
        @Override
        public void run() {
            messageAdapter.notifyDataSetChanged();
        }
    };

    private void parseJson(String jsonStr) {
        try{
            JsonParser jsonParser = new JsonParser();
            JsonObject obj = jsonParser.parse(jsonStr).getAsJsonObject();
            String message = obj.get("message").getAsString();
            String username = obj.get("username").getAsString();

            ChatMessage chatMessage = new ChatMessage(username, message);
            chatMessageList.add(chatMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        pubnub.destroy();
        onBackPressed();
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        pubnub.destroy();
    }
}
