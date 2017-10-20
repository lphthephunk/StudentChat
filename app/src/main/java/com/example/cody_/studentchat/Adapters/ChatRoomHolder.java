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
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;

/**
 * Created by Cody_ on 10/13/2017.
 */

public class ChatRoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private ChatRoom chatRoom;

    private ImageView chatRoomImage;
    private TextView chatRoomName;
    private Button deleteChatroomBtn;

    public ChatRoomHolder(Context context, View itemView){
        super(itemView);

        this.context = context;

        // bind UI objects (ie: possible image and chatroom name
        this.chatRoomImage = (ImageView)itemView.findViewById(R.id.chatRoomImage);
        this.chatRoomName = (TextView)itemView.findViewById(R.id.chatRoomName);
        this.deleteChatroomBtn = (Button)itemView.findViewById(R.id.DeleteChatBtn);

        deleteChatroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Delete hit: ", "");
            }
        });

        itemView.setOnClickListener(this);
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
