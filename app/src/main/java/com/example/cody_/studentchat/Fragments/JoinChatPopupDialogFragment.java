package com.example.cody_.studentchat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cody_.studentchat.ChatRoomActivity;
import com.example.cody_.studentchat.R;

/**
 * Created by Cody_ on 10/4/2017.
 */

public class JoinChatPopupDialogFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_join_chat_dialog, container, false);
        getDialog().setTitle("Enter ChatRoom Password");

        Button submitBtn = (Button)rootView.findViewById(R.id.SubmitButton);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                // TODO: implement code to check for proper password, and navigate to page based on that password
                startActivity(new Intent(getContext(), ChatRoomActivity.class));
            }
        });

        return rootView;
    }
}
