package com.example.cody_.studentchat.Pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.MainDrawer;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.annotations.Expose;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class HomeScreen extends Fragment {

    View inflatedView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
            inflatedView = inflater.inflate(R.layout.activity_home_screen, viewGroup, false);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            SupportMapFragment fragment = new SupportMapFragment();
            ft.add(R.id.homeScreenFragment, fragment);
            ft.commit();

            initView();

            return inflatedView;
    }

    public void initView() {

        Button GoToChatRoomsBtn = (Button)inflatedView.findViewById(R.id.GoToChatRoomsBtn);
        Button mapBtn = (Button)inflatedView.findViewById(R.id.MapBtn);

        GoToChatRoomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatRooms.class));
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getActivity().getPackageManager();
                if (pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(getActivity(), MainDrawer.class);
                    i.putExtra("pageType", "Map");
                    startActivity(i);
                }
                else {
                    getLocationPermission();
                }
            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(getActivity(), MainDrawer.class));
                }
                break;
        }
    }
}

//add sharedpreference
//https://www.google.com/search?q=how+to+store+username+and+password+in+sharedpreference+in+android&oq=how+to+store+username+and+password+in+sharedpreference+in+android&gs_l=psy-ab.3..0i19k1.6830.16656.0.17166.72.54.1.0.0.0.453.4227.30j13j4-1.45.0....0...1.1.64.psy-ab..26.38.3665.0..0j0i30k1j0i30i19k1j0i10i30i19k1j33i21k1.51.GnvRwu5W-Do