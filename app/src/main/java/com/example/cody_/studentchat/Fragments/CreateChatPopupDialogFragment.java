package com.example.cody_.studentchat.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Helpers.KeyCreator;
import com.example.cody_.studentchat.Models.ChatRoom;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Pages.ChatRoomActivity;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.ChatroomRequests.InsertChatroomService;
import com.pubnub.api.Pubnub;

import org.json.JSONException;
import org.json.JSONObject;

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

                final String channelName = channelNameBox.getText().toString().trim();
                if (channelName.length() != 0){
                    try {

                        final ChatRoom chatRoom = new ChatRoom(channelName);
                        //insert the chatRoom object into database
                        chatRoom.AddPerson();

                        chatRoom.save();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResposne = new JSONObject(response);
                                    boolean success = jsonResposne.getBoolean("success");
                                    if (success){
                                        // close popup
                                        dismiss();

                                        Intent i = new Intent(getContext(), ChatRoomActivity.class);
                                        i.putExtra("chatRoomName", chatRoom.getRoomName());
                                        startActivity(i);
                                    }else{
                                        // remove locally saved chatroom if there are errors saving to online database
                                        ChatRoom.delete(chatRoom);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Oops!")
                                                .setMessage("We were unable to create this ChatRoom." +
                                                        " Check your internet connection and try again.")
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
                                    // remove locally saved chatroom if there are errors saving to online database
                                    ChatRoom.delete(chatRoom);
                                    e.printStackTrace();
                                }
                            }
                        };
                        InsertChatroomService insertChatroomService = new InsertChatroomService(chatRoom.getBoundId(), chatRoom.getRoomName(), 1, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.add(insertChatroomService);
                    }catch (Exception ex){
                        Log.d("Error Creating Room: ", ex.getMessage().toString());
                    }
                }
            }
        });

        return rootView;
    }
}
