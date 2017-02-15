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

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Sending a GET request to server with login token in header.
 * Calling the fillListView method in activity and sending the response as parameter.
 */
public class BlogListAsyncTask extends AsyncTask {

    BlogListActivity blActivity;
    private String token;
    public BlogListAsyncTask(BlogListActivity blActivity,String token) {
        this.blActivity = blActivity;
        this.token = token;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        String url = " http://blogsdemo.creitiveapps.com:16427/blogs";
        return getAPIResponse(url,token);

    }
    @Override
    protected void onPostExecute(Object o) {

        blActivity.fillListView((HttpResponse) o);
    }

    /**
     *
     * @param urlString is address of API.
     * @param token sets in header of http request.
     * @return HttpResonse.
     */
    public static HttpResponse getAPIResponse(String urlString, String token){
        URL url;
        HttpURLConnection urlConnection;
        String result;
        HttpResponse response = new HttpResponse();
        response.setSucess(false);
        response.setMessage("Connection failed.");
        try {
            url= new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(1500);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("X-Authorize", token);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            switch (responseCode) {
                case 200:
                    BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bf.readLine()) != null) {
                        sb.append(line);

                    }
                    bf.close();
                    result = sb.toString();

                    response.setSucess(true);
                    response.setMessage(result);
                    break;
                case 406:
                    result = "The media type is unsupported.";
                    response.setMessage(result);
                    response.setSucess(false);
                    break;
                case 415:
                    result = "Content-Type is not application/json.";
                    response.setMessage(result);
                    response.setSucess(false);
                    break;
                case 401:
                    result = "Token is missing.";
                    response.setMessage(result);
                    response.setSucess(false);

                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
