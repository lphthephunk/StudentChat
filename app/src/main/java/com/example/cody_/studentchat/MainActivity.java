package com.example.cody_.studentchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
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
import com.example.cody_.studentchat.Models.User;
import com.pubnub.api.Pubnub;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText etusername;
    EditText etpassword;

    Button login;

    TextView newAccount;
    TextView reAccount;

    Pubnub pubnub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                /*Response.Listener<String> responseListener = new Response.Listener<String>() {
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

                                User user = new User(username, firstname, lastname, email, uuid);
                                Globals.currentUserInfo = user;

                                Intent i = new Intent(MainActivity.this, HomeScreen.class);
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
                queue.add(loginRequest);*/

                User user = new User("PodyCaul", "Cody", "Paul", "pcody8900@gmail.com", ";lkjas;ldkjfa;skljdf;aklsjdfa");

                Globals.currentUserInfo = user;

                startActivity(new Intent(getApplicationContext(), HomeScreen.class));

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

    public void InitPubnub(){


    }
}
