package com.example.uros.testapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

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

    BlogListActivity glavna;
    private ProgressDialog dialog;
    private String token;
    //za konstruktor alt+insert
    public BlogListAsyncTask(BlogListActivity glavna,String token) {
        this.glavna = glavna;
        this.token = token;
    }
    protected void onPreExecute() {


    }


    // ova metoda je nit koja radi
    @Override
    protected Object doInBackground(Object[] params) {







        String adresa = " http://blogsdemo.creitiveapps.com:16427/blogs";
        URL url;
        HttpURLConnection konekcija;
        try {
            url= new URL(adresa);
            konekcija = (HttpURLConnection) url.openConnection();
            konekcija.setConnectTimeout(10000);
            konekcija.setReadTimeout(1500);
            konekcija.setRequestProperty("Content-Type", "application/json");
            konekcija.setRequestProperty("Accept", "application/json");
            konekcija.setRequestProperty("X-Authorize", token);
            konekcija.setRequestMethod("GET");
            konekcija.connect();

            int responsecode = konekcija.getResponseCode();
            //citamo odgovor
            BufferedReader bf = new BufferedReader(new InputStreamReader(konekcija.getInputStream()));
            String rezultat = "";
            String odgovor = null;
            while ((odgovor = bf.readLine())!=null){
                rezultat+=odgovor;

            }
            bf.close();

            JSONArray jsonArray = new JSONArray(rezultat);
            HttpResponse res = new HttpResponse();
            res.setSucess(true);
            res.setMessage(rezultat);
            return res;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    //izlazni rezultat iz prethodne metode je ulaz u ovu metodu
    @Override
    protected void onPostExecute(Object o) {

        glavna.fillListView((HttpResponse) o);
    }
}
