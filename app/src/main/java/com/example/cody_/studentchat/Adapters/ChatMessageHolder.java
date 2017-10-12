package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cody_.studentchat.ChatMessage;
import com.example.cody_.studentchat.R;

/**
 * Created by Cody_ on 10/11/2017.
 */

public class ChatMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private ChatMessage chatMessage;

    private TextView username;
    private TextView messageBlock;

    public ChatMessageHolder(Context context, View itemView){

        super(itemView);

        // set the context
        this.context = context;

        // bind the UI widgets
        this.username = (TextView)itemView.findViewById(R.id.usernameBlock);
        this.messageBlock = (TextView)itemView.findViewById(R.id.messageBlock);

        // set click event listener
        itemView.setOnClickListener(this);
    }

    public void BindChatMessage(ChatMessage message){
        this.chatMessage = message;

        this.username.setText(message.getUsername());
        this.messageBlock.setText(message.getMessage());
    }

    @Override
    public void onClick(View v){
        if (chatMessage != null){

            Toast.makeText(this.context, "Message Tapped", Toast.LENGTH_SHORT).show();
        }
    }
}
