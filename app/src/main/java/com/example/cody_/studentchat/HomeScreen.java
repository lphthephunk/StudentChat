package com.example.cody_.studentchat;

import android.content.Intent;
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

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String email = intent.getStringExtra("email");

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

    @Override
    public void onBackPressed() {

    }
}

//add sharedpreference
//https://www.google.com/search?q=how+to+store+username+and+password+in+sharedpreference+in+android&oq=how+to+store+username+and+password+in+sharedpreference+in+android&gs_l=psy-ab.3..0i19k1.6830.16656.0.17166.72.54.1.0.0.0.453.4227.30j13j4-1.45.0....0...1.1.64.psy-ab..26.38.3665.0..0j0i30k1j0i30i19k1j0i10i30i19k1j33i21k1.51.GnvRwu5W-Do