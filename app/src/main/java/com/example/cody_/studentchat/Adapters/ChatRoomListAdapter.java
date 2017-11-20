package com.example.cody_.studentchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cody_.studentchat.Models.ChatRoom;

import java.util.List;

/**
 * Created by Cody_ on 10/13/2017.
 */

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomHolder> {

    private final List<ChatRoom> chatRooms;
    private Context context;
    private int itemResource;
    private Activity activity;

    public ChatRoomListAdapter(Context context, Activity activity, int itemResource, List<ChatRoom> chatRooms){

        // init the adapter
        this.chatRooms = chatRooms;
        this.context = context;
        this.activity = activity;
        this.itemResource = itemResource;
    }

    @Override
    public ChatRoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);

        return new ChatRoomHolder(this.context, this.activity, view, this, chatRooms);
    }

    @Override
    public void onBindViewHolder(ChatRoomHolder holder, int position) {
        ChatRoom chatRoom = this.chatRooms.get(position);
        holder.BindChatRoom(chatRoom);
    }

    @Override
    public int getItemCount() {
        return this.chatRooms.size();
    }
}
