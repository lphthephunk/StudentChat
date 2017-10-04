package com.example.cody_.studentchat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cody_.studentchat.Fragments.ChatMessageSendFragment;
import com.example.cody_.studentchat.Fragments.JoinChatPopupDialogFragment;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button goToChatButton = (Button)findViewById(R.id.ChatButton);

        goToChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                JoinChatPopupDialogFragment dialogFragment = new JoinChatPopupDialogFragment();
                dialogFragment.show(fm, "Sample Fragment");
            }
        });
    }
}
