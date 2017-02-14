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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Sending a POST request to server with username and password.
 * Calling the continueLogin method in activity and sending the response as parameter.
 */
public class LoginAsyncTask extends AsyncTask{
    private LoginActivity loginActivity;
    private String jsonString;

    public LoginAsyncTask(LoginActivity glavna,String jsonSting) {
        this.loginActivity = glavna;
        this.jsonString = jsonSting;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String urlString = "http://blogsdemo.creitiveapps.com:16427/login";
        return makeRequest(urlString,jsonString);
    }

    @Override
    protected void onPostExecute(Object o) {
        loginActivity.continueLogin((HttpResponse) o);
    }

    /**
     *
     * @param uri is used to open connection. That is address of API.
     * @param json is passed as data i POST method
     * @return HttpResonse
     */
    public static HttpResponse makeRequest(String uri, String json) {
        HttpURLConnection urlConnection;
        String result;
        HttpResponse response = new HttpResponse();
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(json);
            writer.close();
            outputStream.close();
            int responseCode = urlConnection.getResponseCode();
            switch (responseCode){
                case 200:
                    //Read
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    bufferedReader.close();
                    result = sb.toString();
                    response.setMessage(result);
                    response.setSucess(true);
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
                case 400:
                    result = "Forgot email or password.";
                    response.setMessage(result);
                    response.setSucess(false);
                    break;
                case 401:
                    result = "Credentials are wrong.";
                    response.setMessage(result);
                    response.setSucess(false);
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
