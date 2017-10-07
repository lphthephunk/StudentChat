package com.example.cody_.studentchat.Fragments;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cody_.studentchat.ChatRoomActivity;
import com.example.cody_.studentchat.Keys.API_Keys;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.PubNubService;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;

/**
 * Created by Cody_ on 10/4/2017.
 */

public class JoinChatPopupDialogFragment extends DialogFragment {

    private PubNubService chatroomServiceClient;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            chatroomServiceClient = ((PubNubService.LocalBinder)service).getServiceInstance();
            Log.d("Service Status: ", "Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Connection Status: ", "Disconnecting from service");
            chatroomServiceClient = null;
        }
    };

    EditText channelNameBox;
    Button submitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_join_chat_dialog, container, false);

        Thread serviceThread = new Thread(){
            @Override
            public void run(){
                getActivity().startService(new Intent(getActivity(), PubNubService.class));
                super.run();
            }
        };

        serviceThread.start();

        getDialog().setTitle("Enter ChatRoom Password");

        // bind the service for use of creating a new room TODO: later this must be changed to joining a room
        getActivity().bindService(new Intent(getActivity(), PubnubException.class), serviceConnection, Context.BIND_AUTO_CREATE);

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
                        chatroomServiceClient.createRoom(channelName);
                        API_Keys.CHANNEL = channelName;
                        startActivity(new Intent(getContext(), ChatRoomActivity.class));
                    }catch (Exception ex){
                        Log.d("Error Creating Room: ", ex.getMessage().toString());
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        getActivity().bindService(new Intent(getActivity(), PubNubService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().startService(new Intent(getActivity(), PubNubService.class));
        getActivity().bindService(new Intent(getActivity(), PubNubService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unbindService(serviceConnection);
        getActivity().stopService(new Intent(getActivity(), PubNubService.class));
    }
}
