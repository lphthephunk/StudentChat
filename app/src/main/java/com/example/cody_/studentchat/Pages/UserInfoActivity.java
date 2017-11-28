package com.example.cody_.studentchat.Pages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.R;
import com.google.android.gms.maps.SupportMapFragment;

public class UserInfoActivity extends Fragment {
    User user;
    TextView username;
    TextView name;
    TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_info, viewGroup, false);

        this.user = Globals.currentUserInfo;

        username = (TextView)view.findViewById(R.id.textViewUserD);
        username.setText(user.getUsername().toString());

        name = (TextView)view.findViewById(R.id.textViewNameD);
        name.setText(user.concatFirstAndLastName().toString());

        email = (TextView)view.findViewById(R.id.textViewEmailD);
        email.setText(user.getEmail());

        return view;
    }
}
