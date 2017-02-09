package com.example.uros.testapplication;

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

public class LoginActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText etEmail,etPassword;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

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
                           String jsonString =  makeJsonString(eMail,password);
                            new LoginAsyncTask(LoginActivity.this,jsonString).execute();

                        } else {
                            Toast.makeText(getApplicationContext(), "Password should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email should be in valid format.", Toast.LENGTH_SHORT).show();
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

    public void continueLogin(String o) {

    }
}
