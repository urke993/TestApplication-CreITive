package com.example.uros.testapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Uros on 2/9/2017.
 */
public class Session {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin,String token){
        editor.putBoolean("loggedInmode",logggedin);
        editor.putString("loginToken", token);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);

    }
    public String getToken(){
        return prefs.getString("loginToken","Token doesn't exists");
    }
}
