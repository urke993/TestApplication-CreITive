/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.uros.testapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shows the login page to user, where user can enter username and password
 * and login into system.
 */
public class LoginActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText etEmail,etPassword;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        session = new Session(this);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        //If user is logged in previously this activity is passed right away.
        if (session.loggedin() && !session.getToken().equals("")){
            Intent intent = new Intent("android.intent.action.BLOGLISTACTIVITY");
            startActivity(intent);
            finish();
        }
        //Starting login with email and password if they are in valid format. It calls async task.

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (eMail.matches("") || password.matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter your email and password!", Toast.LENGTH_SHORT).show();
                } else {
                    if (isEmailValid(eMail)) {
                        if (isPasswordValid(password)) {
                            if (session.isOnline(LoginActivity.this)) {
                                String jsonString = makeJsonString(eMail, password);
                                new LoginAsyncTask(LoginActivity.this, jsonString).execute();
                            } else {
                                session.showAlertDialog(LoginActivity.this,"No Internet Connection", "You are offline, please check your internet connection.");
                            }
                        } else {
                            session.showAlertDialog(LoginActivity.this,"Invalid Password", "Password should be at least 6 characters long.");
                        }
                    } else {
                        session.showAlertDialog(LoginActivity.this,"Invalid Email", "Email should be in valid format(example@example.com).");
                    }
                }
            }
        });
    }

    /**
     *Checks if email is entered in valid format.
     */
    public boolean isEmailValid(String eMailString){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(eMailString);
        return matcher.find();
    }

    /**
     *Checks if password is entered in valid format and returns true or false.
     */
    public boolean isPasswordValid(String passString){
        return passString.length() >= 6;
    }

    /**
     *Creating String which contains one JSON object with email and password in it.
     */
    public String makeJsonString(String eMail, String pass){
        JSONObject item = new JSONObject();
        try {
            item.put("email", eMail);
            item.put("password",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item.toString();
    }

    /**
     *Continue login after Login button pressed, and deals with server response.
     * It's called from async task.
     */
    public void continueLogin(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(o.getMessage());
                session.setLoggedin(true,jsonObject.getString("token"));
                Intent intent = new Intent("android.intent.action.BLOGLISTACTIVITY");
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
