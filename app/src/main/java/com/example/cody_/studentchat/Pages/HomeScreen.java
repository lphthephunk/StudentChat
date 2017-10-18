package com.example.cody_.studentchat.Pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cody_.studentchat.Drawer;
import com.example.cody_.studentchat.Fragments.JonChatPopupDialogFragment;
import com.example.cody_.studentchat.Pages.ChatRooms;
import com.example.cody_.studentchat.R;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        String password = intent.getStringExtra("password");
        String email = intent.getStringExtra("email");
        long mobile = intent.getLongExtra("mobile", -1);

//------------------------Testing-----------------------------
        /*TextView test = (TextView)findViewById(R.id.test);
        test.setText(
                "Username = " + username + "\n" +
                "Firstname = " + firstname + "\n" +
                "Lastname = " + lastname + "\n" +
                "Password = " + password + "\n" +
                "E-mail = " + email + "\n" +
                "Phone Number = " + mobile + "\n");*/
//------------------------Testing-----------------------------

        Button GoToChatRoomsBtn = (Button)findViewById(R.id.GoToChatRoomsBtn);
        Button mapBtn = (Button)findViewById(R.id.MapBtn);

        GoToChatRoomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatRooms.class));
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                if (pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getApplicationContext(), Drawer.class));
                }
                else {
                    getLocationPermission();
                }
            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(getApplicationContext(), Drawer.class));
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {

    }
}

//add sharedpreference
//https://www.google.com/search?q=how+to+store+username+and+password+in+sharedpreference+in+android&oq=how+to+store+username+and+password+in+sharedpreference+in+android&gs_l=psy-ab.3..0i19k1.6830.16656.0.17166.72.54.1.0.0.0.453.4227.30j13j4-1.45.0....0...1.1.64.psy-ab..26.38.3665.0..0j0i30k1j0i30i19k1j0i10i30i19k1j33i21k1.51.GnvRwu5W-Do