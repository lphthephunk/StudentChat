package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.ChatMessage;
import com.example.cody_.studentchat.Models.ChatRoom;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;

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

    ChatRoomListAdapter adapter;

    List<ChatRoom> chatRoomList;

    public ChatRoomHolder(Context context, final View itemView, final ChatRoomListAdapter adapter, final List<ChatRoom> chatRoomList){
        super(itemView);

        this.context = context;
        this.adapter = adapter;
        this.chatRoomList = chatRoomList;

        // bind UI objects (ie: possible image and chatroom name
        this.chatRoomImage = (ImageView)itemView.findViewById(R.id.chatRoomImage);
        this.chatRoomName = (TextView)itemView.findViewById(R.id.chatRoomName);
        this.deleteChatroomBtn = (Button)itemView.findViewById(R.id.DeleteChatBtn);

        deleteChatroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoom.delete(chatRoom);
                UpdateChatList();
            }
        });

        itemView.setOnClickListener(this);
    }

    private void UpdateChatList(){
        try {
            List<ChatRoom> rooms = ChatRoom.listAll(ChatRoom.class);
            if (rooms != null && !rooms.isEmpty()){
                chatRoomList.clear();
                chatRoomList.addAll(rooms);
                adapter.notifyDataSetChanged();

            }
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
