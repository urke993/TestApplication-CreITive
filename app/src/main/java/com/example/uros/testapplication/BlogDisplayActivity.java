package com.example.uros.testapplication;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class BlogDisplayActivity extends AppCompatActivity {

    private Session session;
    private String blogIdString;

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_display);
        setTitle("Blog Display");

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        // 1. get passed intent
        Intent intent = getIntent();
        // 2. get message value from intent
        blogIdString = intent.getStringExtra("blogIdMessage");
        session = new Session(this);
        if (!session.loggedin()){
            finish();
        }

        new BlogDisplayAsyncTask(this,session.getToken(),blogIdString).execute();
    }

    public void fillDisplay(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(o.getMessage());
                String htmlContent = jsonObject.getString("content");
                webView.loadData(htmlContent, "text/html; charset=utf-8", "UTF-8");




            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

}
