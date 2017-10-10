package com.example.cody_.studentchat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccount extends AppCompatActivity {

    EditText etusername;
    EditText etpassword;
    EditText etpasswordConfirm;
    EditText etemail;
    EditText etemailConfirm;
    Button usernameConfirm;
    Button save;

    boolean username_check = false;
    boolean password_check = false;
    boolean email_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etusername = (EditText)findViewById(R.id.editTextCreateUsername);
        etpassword = (EditText)findViewById(R.id.editTextCreatePassword);
        etpasswordConfirm = (EditText)findViewById(R.id.editTextConfirmPassword);
        etemail = (EditText)findViewById(R.id.editTextCreateEmail);
        etemailConfirm = (EditText)findViewById(R.id.editTextConfirmEmail);
        usernameConfirm = (Button)findViewById(R.id.buttonUsernameConfirm);
        save = (Button)findViewById(R.id.buttonCreateAccount);

        etpasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etpasswordConfirm.getText().toString().equals(etpassword.getText().toString()))
                {
                    etpasswordConfirm.setBackgroundResource(R.drawable.green_box);
                    password_check = true;
                }
                else
                {
                    etpasswordConfirm.setBackgroundResource(R.drawable.red_box);
                    password_check = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etemailConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etemailConfirm.getText().toString().equals(etemail.getText().toString()))
                {
                    etemailConfirm.setBackgroundResource(R.drawable.green_box);
                    email_check = true;
                }
                else
                {
                    etemailConfirm.setBackgroundResource(R.drawable.red_box);
                    email_check = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etusername.getText().toString();
                String password = etpassword.getText().toString();
                String email = etemail.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success)
                            {
                                startActivity(new Intent(CreateAccount.this, MainActivity.class));
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                                builder.setMessage("Register Failed").setNegativeButton("Retry", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, password, email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateAccount.this);
                queue.add(registerRequest);
            }
        });
    }
}