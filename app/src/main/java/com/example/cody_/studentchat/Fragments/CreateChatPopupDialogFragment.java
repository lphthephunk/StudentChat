package com.example.cody_.studentchat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cody_.studentchat.Models.ChatRoom;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.R;
import com.orm.SugarApp;
import com.pubnub.api.Pubnub;

/**
 * Created by Cody_ on 10/4/2017.
 */

public class CreateChatPopupDialogFragment extends DialogFragment {

    EditText channelNameBox;
    Button submitBtn;
    Pubnub pubnub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_create_chat_dialog, container, false);

        getDialog().setTitle("Enter ChatRoom Password");

        channelNameBox = (EditText)rootView.findViewById(R.id.ChannelNameEditBox);

        submitBtn = (Button)rootView.findViewById(R.id.SubmitButton);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                // TODO: enter the name of the channel that the user wishes to join
                String channelName = channelNameBox.getText().toString().trim();
                if (channelName.length() != 0){
                    try {
                        ChatRoom chatRoom = new ChatRoom(channelName);
                        //insert the chatRoom object into database
                        chatRoom.AddPerson();

                        try{
                            chatRoom.save();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        Intent i = new Intent(getContext(), ChatRoomActivity.class);
                        i.putExtra("chatRoomName", chatRoom.getRoomName());
                        startActivity(i);

                    }catch (Exception ex){
                        Log.d("Error Creating Room: ", ex.getMessage().toString());
                    }
                }
            }
        });

        return rootView;
    }
}
