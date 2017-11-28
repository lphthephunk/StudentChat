package com.example.cody_.studentchat.Pages;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Adapters.GMailSender;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.UserRequests.RetrieveRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class RetrieveAccount extends AppCompatActivity {

    EditText email;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_account);

        email = (EditText)findViewById(R.id.editTextCheckEmail);
        send = (Button)findViewById(R.id.buttonRetrieve);

        int length = 8;
        final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ1234567890";
        final StringBuilder result = new StringBuilder();
        while(length > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String to = email.getText().toString();
                final String code = result.toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {

                                final String username = jsonResponse.getString("username");
                                Thread sender = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender("studyroomapplication@gmail.com", "inho123123");
                                            sender.sendMail("Retrieve Account", "Username = " + username + "\nNew Password = " + code, "incho0824@gmail.com", to);
                                        } catch (Exception e) {
                                            Log.e("mylog", "Error: " + e.getMessage());
                                        }
                                    }
                                });
                                sender.start();
                                Toast.makeText(getApplicationContext(),"Username and New-Password has been sent to:\n" + to, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RetrieveAccount.this, MainActivity.class);
                                RetrieveAccount.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RetrieveAccount.this);
                                builder.setMessage("E-mail Check Failed:\nUnknown E-mail Address")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RetrieveRequest retrieveRequest = new RetrieveRequest(to, code, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RetrieveAccount.this);
                queue.add(retrieveRequest);
            }
        });
    }
}
