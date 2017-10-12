package com.example.cody_.studentchat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cody_.studentchat.ChatRoomActivity;
import com.example.cody_.studentchat.Keys.API_Keys;
import com.example.cody_.studentchat.R;

public class JonChatPopupDialogFragment extends DialogFragment {

    EditText RoomNameField;
    Button SubmitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_jon_chat_popup_dialog, container, false);

        RoomNameField = (EditText)rootView.findViewById(R.id.chatNameField);
        SubmitBtn = (Button)rootView.findViewById(R.id.submitBtn);

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String channelName = RoomNameField.getText().toString();
                if (channelName.length() > 0){
                    try{
                        API_Keys.CHANNEL = channelName;
                        startActivity(new Intent(getContext(), ChatRoomActivity.class));
                    }catch(Exception ex){
                        Log.d("Error joining: ", ex.getMessage());
                    }
                }
            }
        });

        return rootView;
    }
}
