package com.example.cody_.studentchat.Pages;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.cody_.studentchat.Adapters.ChatRoomListAdapter;
import com.example.cody_.studentchat.Adapters.CustomDivider;
import com.example.cody_.studentchat.Fragments.CreateChatPopupDialogFragment;
import com.example.cody_.studentchat.Fragments.JonChatPopupDialogFragment;
import com.example.cody_.studentchat.Models.ChatRoom;
import com.example.cody_.studentchat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRooms extends AppCompatActivity {

    RecyclerView chatroomRecycler;
    List<ChatRoom> chatRoomList;
    ChatRoomListAdapter chatRoomListAdapter;
    Button addNewChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chatroomRecycler = (RecyclerView)findViewById(R.id.chatRoomsRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayoutManager.setStackFromEnd(true);

        chatroomRecycler.setLayoutManager(linearLayoutManager);

        //chatroomRecycler.addItemDecoration(new CustomDivider(this));

        chatroomRecycler.setHasFixedSize(false);
        chatroomRecycler.setItemAnimator(new DefaultItemAnimator());

        chatRoomList = new ArrayList<>();
        chatRoomListAdapter = new ChatRoomListAdapter(this, R.layout.chatroom_recycler_adapter, chatRoomList);

        chatroomRecycler.setAdapter(chatRoomListAdapter);

        addNewChatButton = (Button)findViewById(R.id.addNewChatBtn);

        addNewChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateChatPopupDialogFragment dialogFragment = new CreateChatPopupDialogFragment();
                dialogFragment.show(fm, "Create a ChatRoom");
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            List<ChatRoom> rooms = ChatRoom.listAll(ChatRoom.class);
            if (rooms != null && !rooms.isEmpty()){
                chatRoomList.clear();
                chatRoomList.addAll(rooms);
                chatRoomListAdapter.notifyDataSetChanged();

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
