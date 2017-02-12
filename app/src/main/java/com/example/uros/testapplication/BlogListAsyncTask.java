package com.example.uros.testapplication;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Uros on 2/11/2017.
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
    public static HttpResponse getAPIResponse(String urlString, String token){
        URL url;
        HttpURLConnection urlConnection;
        String result;
        HttpResponse response = new HttpResponse();
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
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bf.readLine()) != null) {
                        sb.append(line);

                    }
                    bf.close();
                    result = sb.toString();

                    response.setSucess(true);
                    response.setMessage(result);
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
