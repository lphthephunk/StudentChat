package com.example.cody_.studentchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.Pages.HomeScreen;
import com.example.cody_.studentchat.Pages.MainActivity;
import com.example.cody_.studentchat.Pages.StudyFinderActivity;
import com.example.cody_.studentchat.Pages.StudyGroupActivity;
import com.example.cody_.studentchat.Pages.UserInfoActivity;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView UserNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_with_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Study Room");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                 this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        try {

            switch(getIntent().getStringExtra("pageType")) {
                case "Homescreen":
                    Fragment homeFrag = new HomeScreen();
                    FragmentTransaction homeFt = getSupportFragmentManager().beginTransaction();
                    homeFt.replace(R.id.frame_content, homeFrag);
                    homeFt.commit();
                    break;
                case "Map":
                    Fragment mapFrag = new StudyFinderActivity();
                    FragmentTransaction mapFt = getSupportFragmentManager().beginTransaction();
                    mapFt.replace(R.id.frame_content, mapFrag);
                    mapFt.commit();
                    break;
                case "UserInfo":
                    Fragment userFrag = new UserInfoActivity();
                    FragmentTransaction userFt = getSupportFragmentManager().beginTransaction();
                    userFt.replace(R.id.frame_content, userFrag);
                    userFt.commit();
                    break;
                case "StudyGroup":
                    Fragment studyFrag = new StudyGroupActivity();
                    FragmentTransaction studyFt = getSupportFragmentManager().beginTransaction();
                    studyFt.replace(R.id.frame_content, studyFrag);
                    studyFt.commit();
                    break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        UserNameTextView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.DrawerUserNameTextView);
        UserNameTextView.setText(Globals.currentUserInfo.getUsername());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.map_with_drawer, menu);

        // return false because we do not want an action bar for now
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent i = new Intent(MainDrawer.this, MainDrawer.class);

        switch(id){
            case R.id.HomePageBtn:
                i.putExtra("pageType", "Homescreen");
                MainDrawer.this.startActivity(i);
                break;
            case R.id.UserInfoPageBtn:
                i.putExtra("pageType", "UserInfo");
                MainDrawer.this.startActivity(i);
                break;
            case R.id.StudyGroupsPageBtn:
                i.putExtra("pageType", "StudyGroup");
                MainDrawer.this.startActivity(i);
                break;
            case R.id.LogoutBtn:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
