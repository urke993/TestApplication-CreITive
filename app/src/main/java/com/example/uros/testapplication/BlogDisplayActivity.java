package com.example.uros.testapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;



public class BlogDisplayActivity extends AppCompatActivity {

    private Session session;
    private String blogIdString;

    NetworkStateReceiver networkStateReceiver;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_display);
        setTitle("Blog Display");
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);



        // 1. get passed intent
        Intent intent = getIntent();
        // 2. get message value from intent
        blogIdString = intent.getStringExtra("blogIdMessage");
        session = new Session(this);
        if (!session.loggedin()){
            finish();
        }
        if (!session.isOnline(BlogDisplayActivity.this)){
            session.showAlertDialog(BlogDisplayActivity.this,"No Internet Connection", "You are offline, please check your internet connection.");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.setBlogIdPassed(blogIdString);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkStateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(networkStateReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    public void fillDisplay(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(o.getMessage());
                String htmlContent = jsonObject.getString("content").toString();



                String stringToAdd = "width=\"100%\" ";

                // Create a StringBuilder to insert string in the middle of content.
                StringBuilder sb = new StringBuilder(htmlContent);

                int i = 0;
                int cont = 0;

                // Check for the "src" substring, if it exists, take the index where
                // it appears and insert the stringToAdd there, then increment a counter
                // because the string gets altered and you should sum the length of the inserted substring
                while(i != -1){
                    i = htmlContent.indexOf("src", i + 1);
                    if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                    ++cont;
                }

                webView.loadData(sb.toString(), "text/html; charset=utf-8", "UTF-8");



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
