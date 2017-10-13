package com.example.cody_.studentchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class CreateAccount extends AppCompatActivity {

    EditText etusername;
    EditText etfirstname;
    EditText etlastname;
    EditText etpassword;
    EditText etpasswordConfirm;
    EditText etemail;
    EditText etemailConfirm;
    EditText etmobile;
    Button save;

    boolean password_check = false;
    boolean email_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etusername = (EditText)findViewById(R.id.editTextCreateUsername);
        etfirstname = (EditText)findViewById(R.id.editTextFirstName);
        etlastname = (EditText)findViewById(R.id.editTextLastName);
        etpassword = (EditText)findViewById(R.id.editTextCreatePassword);
        etpasswordConfirm = (EditText)findViewById(R.id.editTextConfirmPassword);
        etemail = (EditText)findViewById(R.id.editTextCreateEmail);
        etemailConfirm = (EditText)findViewById(R.id.editTextConfirmEmail);
        etmobile = (EditText)findViewById(R.id.editTextPhoneNumber);
        save = (Button)findViewById(R.id.buttonCreateAccount);

        save.setEnabled(false);

        final Drawable originalDra = etusername.getBackground();

        etusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etusername.setBackgroundDrawable(originalDra);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etpasswordConfirm.getText().toString().equals(etpassword.getText().toString()))
                {
                    etpasswordConfirm.setBackgroundResource(R.drawable.green_box);
                    password_check = true;
                    if (email_check)
                    {
                        save.setEnabled(true);
                    }
                }
                else
                {
                    etpasswordConfirm.setBackgroundResource(R.drawable.red_box);
                    password_check = false;
                    save.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etpassword.getText().toString().matches("")) {
                    etpasswordConfirm.setBackgroundDrawable(originalDra);
                    password_check = false;
                    save.setEnabled(false);
                }
            }

        });

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
                    if (email_check)
                    {
                        save.setEnabled(true);
                    }
                }
                else
                {
                    etpasswordConfirm.setBackgroundResource(R.drawable.red_box);
                    password_check = false;
                    save.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        etemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etemailConfirm.getText().toString().equals(etemail.getText().toString()))
                {
                    etemailConfirm.setBackgroundResource(R.drawable.green_box);
                    email_check = true;
                    if (password_check)
                    {
                        save.setEnabled(true);
                    }
                }
                else
                {
                    etemailConfirm.setBackgroundResource(R.drawable.red_box);
                    email_check = false;
                    save.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etemail.getText().toString().matches("")) {
                    etemailConfirm.setBackgroundDrawable(originalDra);
                    email_check = false;
                    save.setEnabled(false);
                }
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
                    if (password_check)
                    {
                        save.setEnabled(true);
                    }
                }
                else
                {
                    etemailConfirm.setBackgroundResource(R.drawable.red_box);
                    email_check = false;
                    save.setEnabled(false);
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
                String firstname = etfirstname.getText().toString();
                String lastname = etlastname.getText().toString();
                String password = etpassword.getText().toString();
                String email = etemail.getText().toString();
                String uuid = randomUUID().toString();

                // create new user object
                final User user = new User(username, firstname, lastname, email, uuid);

                long mobile = Long.parseLong(etmobile.getText().toString());

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {

                                // store the user object in the sqlite database
                                user.save();

                                Globals.UUID = user.getUUID();
                                Globals.currentUserInfo = user;

                                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                                CreateAccount.this.startActivity(intent);
                            } else {
                                etusername.setBackgroundResource(R.drawable.red_box);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                                builder.setMessage("Register Failed:\nUsername Already Exists")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, firstname, lastname, password, email, uuid, mobile, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateAccount.this);
                queue.add(registerRequest);
            }
        });
    }
}