package com.example.uros.testapplication;

import android.app.ProgressDialog;
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
    private ProgressDialog dialog;
    String jsonString;

    public LoginAsyncTask(LoginActivity glavna,String jsonSting) {
        this.loginActivity = glavna;
        this.jsonString = jsonSting;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(loginActivity);
        dialog.setCancelable(true);
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String urlString = "http://blogsdemo.creitiveapps.com:16427/login";
        HttpResponse response = makeRequest(urlString,jsonString);
        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        Thread stoperica = new Thread(){
            public void  run(){
                try{
                    sleep(700);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    dialog.cancel();
                }
            }
        };stoperica.start();
        loginActivity.continueLogin((HttpResponse) o);
    }

    public static HttpResponse makeRequest(String uri, String json) {
        HttpURLConnection urlConnection;
        String data = json;
        String result = null;
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
            writer.write(data);
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
