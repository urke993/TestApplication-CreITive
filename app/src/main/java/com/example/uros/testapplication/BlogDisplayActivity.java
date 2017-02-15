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
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Shows html content in WebView. It register NetworkStateReceiver
 * to deal with Network connection and sending request to server.
 */
public class BlogDisplayActivity extends AppCompatActivity {

    private String blogIdString;

    NetworkStateReceiver networkStateReceiver;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_display);
        setTitle("Blog Display");
        webView = (WebView) findViewById(R.id.webView);
        settingTheWebView();

        getPassedData();

        Session session = new Session(this);
        if (!session.loggedin()){
            finish();
        }
        if (!session.isOnline(BlogDisplayActivity.this)){
            session.showAlertDialog(BlogDisplayActivity.this, "No Internet Connection", "You are offline, please check your internet connection.");
        }


    }

    /**
     * Change default WebView settings.
     */
    private void settingTheWebView() {
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    /**
     * Getting the String data that BlogListActivity passed to this activity.
     */
    private void getPassedData() {
        Intent intent = getIntent();
        blogIdString = intent.getStringExtra("blogIdMessage");
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

    /**
     * If API response is OK, sets Html content in WebView.
     * If API response is not OK toasts message to user.
     */
    public void fillDisplay(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(o.getMessage());
                @SuppressWarnings("For some reason WebView crashes when toString() is not called after")
                String htmlContent = jsonObject.getString("content").toString();
                webView.loadData(changeHtmlContent(htmlContent), "text/html; charset=utf-8", "UTF-8");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Changing the Html content to adjust for WebView.
     * Searching the "src" in content and adding "width=100%" before it.
     * Returns changed Html content as String.
     */
    public String changeHtmlContent(String content){
        String stringToAdd = "width=\"100%\" ";

        // Create a StringBuilder to insert string in the middle of content.
        StringBuilder sb = new StringBuilder(content);

        int i = 0;
        int cont = 0;

        // Check for the "src" substring, if it exists, take the index where
        // it appears and insert the stringToAdd there, then increment a counter
        // because the string gets altered and you should sum the length of the inserted substring
        while(i != -1){
            i = content.indexOf("src", i + 1);
            if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
            ++cont;
        }
        return sb.toString();
    }

}
