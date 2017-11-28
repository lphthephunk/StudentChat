package com.example.cody_.studentchat.Pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cody_.studentchat.Adapters.GMailSender;
import com.example.cody_.studentchat.Helpers.Globals;
import com.example.cody_.studentchat.Models.User;
import com.example.cody_.studentchat.R;
import com.example.cody_.studentchat.Services.UserRequests.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

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
    Button verify;

    boolean password_check = false;
    boolean email_check = false;
    boolean verification = false;

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
        verify = (Button)findViewById(R.id.buttonVerify);

        save.setEnabled(false);
        verify.setEnabled(false);

        final Drawable originalDra = etusername.getBackground();
        Random r = new Random( System.currentTimeMillis() );
        int x = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
        final String code = Integer.toString(x);

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
                    if (verification && email_check)
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
                    if (verification && email_check)
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
                    verify.setEnabled(true);
                    if (verification && password_check)
                    {
                        save.setEnabled(true);
                    }
                }
                else
                {
                    verify.setEnabled(false);
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
                    verify.setEnabled(true);
                    if (verification && password_check)
                    {
                        save.setEnabled(true);
                    }
                }
                else
                {
                    verify.setEnabled(false);
                    etemailConfirm.setBackgroundResource(R.drawable.red_box);
                    email_check = false;
                    save.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread sender = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GMailSender sender = new GMailSender("studyroomapplication@gmail.com", "inho123123");
                            sender.sendMail("Verification Code", code, "incho0824@gmail.com", etemail.getText().toString());
                        } catch (Exception e) {
                            Log.e("mylog", "Error: " + e.getMessage());
                        }
                    }
                });
                sender.start();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateAccount.this);
                alertDialog.setTitle("E-Mail Verification Sent to " + etemail.getText().toString());
                alertDialog.setMessage("If verification code is not received: go back and re-verify your email");

                final EditText input = new EditText(CreateAccount.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Re-verify",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setNegativeButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String num = input.getText().toString();
                                if (num.equals(code)) {
                                    Toast.makeText(getApplicationContext(),"Verification Succeeded", Toast.LENGTH_LONG).show();
                                    verification = true;
                                    if (password_check && email_check) {
                                        save.setEnabled(true);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),"Wrong Verification Code!", Toast.LENGTH_LONG).show();
                                    verification = false;
                                }
                            }
                        });

                alertDialog.show();
            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etusername.getText().toString();
                final String firstname = etfirstname.getText().toString();
                final String lastname = etlastname.getText().toString();
                final String password = etpassword.getText().toString();
                final String email = etemail.getText().toString();
                final String uuid = randomUUID().toString();
                long mobile = 0000000000;
                if (!etmobile.getText().toString().matches("")) {
                    mobile = Long.parseLong(etmobile.getText().toString());
                }
                // create new user object
                final User user = new User(username, firstname, lastname, email, uuid);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {

                                // store the user object in the sqlite database REMOVE
                                // user.save();

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