package com.example.cody_.studentchat.Pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.StudyGroup;
import com.example.cody_.studentchat.Services.UserRequests.LoginRequest;
import com.example.cody_.studentchat.MainDrawer;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etusername;
    EditText etpassword;

    Button login;

    TextView newAccount;
    TextView reAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarDb db = new SugarDb(this);
        db.onCreate(db.getDB());

        etusername = (EditText)findViewById(R.id.editTextUsername);
        etpassword = (EditText)findViewById(R.id.editTextPassword);

        login = (Button)findViewById(R.id.buttonLogin);

        newAccount = (TextView)findViewById(R.id.textViewCreateAccount);
        reAccount = (TextView)findViewById(R.id.textViewRetrieveAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etusername.getText().toString();
                final String password = etpassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String username = jsonResponse.getString("username");
                                String firstname = jsonResponse.getString("firstname");
                                String lastname = jsonResponse.getString("lastname");
                                String email = jsonResponse.getString("email");
                                String uuid = jsonResponse.getString("uuid");
                                String joinedGroups = jsonResponse.getString("joinedGroups");

                                User user = new User(username, firstname, lastname, email, uuid);
                                user.setRawJsonGroupValue(joinedGroups);
                                Globals.currentUserInfo = user;



                                Intent i = new Intent(MainActivity.this, MainDrawer.class);
                                i.putExtra("pageType", "Homescreen");
                                i.putExtra("username", username);
                                i.putExtra("password", password);
                                i.putExtra("email", email);
                                MainActivity.this.startActivity(i);
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle(getString(R.string.login_fail));
                                alertDialog.setMessage(getString(R.string.login_fail_message));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });

        reAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RetrieveAccount.class));
            }
        });
    }
}
