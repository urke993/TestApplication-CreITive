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
 * Created by Uros on 2/9/2017.
 */
public class LoginAsyncTask extends AsyncTask{

    LoginActivity loginActivity;
    String jsonString;

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
                case 401:
                    result = "Email or password incorect.";
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
