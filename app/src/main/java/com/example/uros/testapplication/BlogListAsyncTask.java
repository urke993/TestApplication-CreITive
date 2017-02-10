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
 * Created by Uros on 2/10/2017.
 */
public class BlogListAsyncTask extends AsyncTask {

    BlogListActivity blogListActivity;
    private String token;

    public BlogListAsyncTask(BlogListActivity blogListActivity,String token) {
        this.blogListActivity = blogListActivity;
        this.token = token;

    }



    // ova metoda je nit koja radi
    @Override
    protected Object doInBackground(Object[] params) {

        String urlString = "http://blogsdemo.creitiveapps.com:16427/login";//ovo je kada se vuce iz lokala(localhhost) preko wamp servera
        HttpResponse response = makeRequest(urlString,token);
        return response;
    }

    //izlazni rezultat iz prethodne metode je ulaz u ovu metodu
    @Override
    protected void onPostExecute(Object o) {

        blogListActivity.fillListView((HttpResponse) o);
    }
    public static HttpResponse makeRequest(String uri,String token) {
        HttpURLConnection urlConnection;
        String url;
        String result = null;
        HttpResponse response = new HttpResponse();
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("X-Authorize",token);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();



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
