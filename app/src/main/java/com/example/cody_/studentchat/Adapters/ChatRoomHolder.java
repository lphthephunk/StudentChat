package com.example.cody_.studentchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Models.ChatRoom;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.ChatroomRequests.DeleteChatroomService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Cody_ on 10/13/2017.
 */

public class ChatRoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private ChatRoom chatRoom;

    private ImageView chatRoomImage;
    private TextView chatRoomName;
    private Button deleteChatroomBtn;
    private Activity activity;

    ChatRoomListAdapter adapter;

    List<ChatRoom> chatRoomList;

    public ChatRoomHolder(final Context context, final Activity activity, final View itemView, final ChatRoomListAdapter adapter, final List<ChatRoom> chatRoomList){
        super(itemView);

        this.context = context;
        this.activity = activity;
        this.adapter = adapter;
        this.chatRoomList = chatRoomList;

        // bind UI objects (ie: possible image and chatroom name
        this.chatRoomImage = (ImageView)itemView.findViewById(R.id.chatRoomImage);
        this.chatRoomName = (TextView)itemView.findViewById(R.id.chatRoomName);
        this.deleteChatroomBtn = (Button)itemView.findViewById(R.id.DeleteChatBtn);

        deleteChatroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Leave Chat?")
                        .setMessage("Are you sure you want to remove yourself" +
                                "from the ChatRoom?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // remove from online db
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");

                                            if (success){
                                                // remove from local db
                                                ChatRoom.delete(chatRoom);
                                                UpdateChatList();
                                            }else{
                                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                                builder.setTitle("Oops!")
                                                        .setMessage("We were unable to process this request." +
                                                                " Ensure that you have a proper internet connection and try again.")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // do nothing
                                                            }
                                                        });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        }catch(JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                DeleteChatroomService deleteChatroomService = new DeleteChatroomService(chatRoom.getBoundId(),chatRoom.getRoomName(), responseListener);
                                RequestQueue queue = Volley.newRequestQueue(context);
                                queue.add(deleteChatroomService);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        itemView.setOnClickListener(this);
    }

    private void UpdateChatList(){
        try {
            List<ChatRoom> rooms = ChatRoom.listAll(ChatRoom.class);
            chatRoomList.clear();
            chatRoomList.addAll(rooms);
            adapter.notifyDataSetChanged();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void BindChatRoom(ChatRoom chatRoom){
        this.chatRoom = chatRoom;

        this.chatRoomName.setText(chatRoom.getRoomName());
        if (chatRoom.getChatRoomImage() != null) {
            this.chatRoomImage.setImageBitmap(chatRoom.getChatRoomImage());
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, ChatRoomActivity.class);
        i.putExtra("chatRoomName", chatRoom.getRoomName());
        context.startActivity(i);
    }
}
