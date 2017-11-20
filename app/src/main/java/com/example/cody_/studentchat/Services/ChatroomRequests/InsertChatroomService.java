package com.example.cody_.studentchat.Services.ChatroomRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/13/2017.
 */

public class InsertChatroomService extends StringRequest {

    private static final String INSERT_CHATROOM = "http://incho.xyz/InsertChatroom.php";
    private Map<String, String> params;

    public InsertChatroomService(long boundChatId, String chatName, int personCount, Response.Listener<String> listener){
        super(Method.POST, INSERT_CHATROOM, listener, null);
        params = new HashMap<>();
        params.put("chatroomName", chatName);
        params.put("boundChatId", String.valueOf(boundChatId));
        params.put("personCount", String.valueOf(personCount));
    }

    @Override
    public Map<String, String> getParams(){return this.params;}
}
