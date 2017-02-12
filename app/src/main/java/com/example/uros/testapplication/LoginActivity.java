package com.example.uros.testapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText etEmail,etPassword;
    private Button btnLogin;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        session = new Session(this);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        if (session.loggedin()){
            session.setLoggedin(false,"Token doesn't exists");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (eMail.matches("") || password.matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter your email and password!", Toast.LENGTH_SHORT).show();
                }else {
                    if (isEmailValid(eMail)) {
                        if (isPasswordValid(password)) {
                            if(isOnline()){
                                String jsonString =  makeJsonString(eMail,password);
                                new LoginAsyncTask(LoginActivity.this,jsonString).execute();
                            }else{
                                showAlertDialog("No Internet Connection","You are offline, please check your internet connection.");
                            }
                        } else {
                            showAlertDialog("Invalid Password", "Password should be at least 6 characters long.");
                        }
                    } else {
                        showAlertDialog("Invalid Email","Email should be in valid format(example@example.com).");
                    }
                }
            }
        });
    }

    /**
     *This Method check if email is valid
     */
    public boolean isEmailValid(String eMailString){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(eMailString);
        return matcher.find();
    }
    public boolean isPasswordValid(String passString){
        if(passString.length()<6){
            return false;
        }else{
            return true;
        }
    }
    public String makeJsonString(String eMail, String pass){
        JSONObject item = new JSONObject();

        try {
            item.put("email", eMail);
            item.put("password",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = item.toString();
        return jsonString;
    }

    public void continueLogin(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObject = null;
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
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void showAlertDialog(String title,String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

}
