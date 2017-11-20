package com.example.cody_.studentchat.Services.ChatroomRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody_ on 11/14/2017.
 */

public class DeleteChatroomService extends StringRequest {

    private static final String DELETE_CHATROOM = "http://incho.xyz/DeleteChatroom.php";
    private Map<String, String> params;

    public DeleteChatroomService(long boundChatId, String chatName, Response.Listener<String> listener){
        super(Method.POST, DELETE_CHATROOM, listener, null);
        params = new HashMap<>();
        params.put("boundChatId", String.valueOf(boundChatId));
        params.put("chatroomName", chatName);
    }

    @Override
    public Map<String, String> getParams(){ return this.params;}
}
