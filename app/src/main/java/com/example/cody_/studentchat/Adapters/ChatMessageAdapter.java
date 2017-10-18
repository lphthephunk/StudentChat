package com.example.cody_.studentchat.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cody_.studentchat.Models.ChatMessage;

import java.util.List;

/**
 * Created by Cody_ on 10/11/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageHolder> {

    private final List<ChatMessage> messages;
    private Context context;
    private int itemResource;

    public ChatMessageAdapter(Context context, int itemResource, List<ChatMessage> messages){

        // init the adapter
        this.messages = messages;
        this.itemResource = itemResource;
        this.context = context;
    }

    @Override
    public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType){

        // inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new ChatMessageHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(ChatMessageHolder holder, int position) {

        // use position to access a specific message
        ChatMessage message = this.messages.get(position);

        // bind the message object to the viewHolder
        holder.BindChatMessage(message);
    }

    @Override
    public int getItemCount(){
        return this.messages.size();
    }
}
