package com.example.cody_.studentchat;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cody_.studentchat.Fragments.JoinChatPopupDialogFragment;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button joinRoomBtn = (Button)findViewById(R.id.JoinRoomBtn);
        Button createRoomBtn = (Button)findViewById(R.id.CreateRoomBtn);
        Button mapBtn = (Button)findViewById(R.id.MapBtn);

        joinRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                JoinChatPopupDialogFragment dialogFragment = new JoinChatPopupDialogFragment();
                dialogFragment.show(fm, "Join a ChatRoom");
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
