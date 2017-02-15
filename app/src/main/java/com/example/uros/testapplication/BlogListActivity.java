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

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows the list of blogs in ListView. Adapter that is used for list is CustomListAdapter.
 */
public class BlogListActivity extends AppCompatActivity {

    private List<Blog> listOfBlogs;
    private Blog[] arrayOfBlogs;
    private NetworkStateReceiver networkStateReceiver;

    private Session session;
    private CustomListAdapter adapter;
    ListView blogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        setTitle("Blog List");
        blogList = (ListView) findViewById(R.id.listViewBlogs);
        listOfBlogs = new ArrayList<>();
        session = new Session(this);
        //Checking if the user is logged in
        if (!session.loggedin()){
            finish();
        }
        //Informing the user that there is no Internet connection
        if (!session.isOnline(BlogListActivity.this)){
            adapter = new CustomListAdapter(this,new Blog[]{});
            blogList.setAdapter(adapter);
            session.showAlertDialog(BlogListActivity.this,"No Internet Connection", "You are offline, please check your internet connection.");
        }

        getDataFromTheInternet();

        blogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Blog chosenBlog = arrayOfBlogs[+position];
                Intent intent = new Intent("android.intent.action.BLOGDISPLAYACTIVITY");
                intent.putExtra("blogIdMessage", chosenBlog.getBlogId());
                startActivity(intent);
            }
        });
    }

    /**
     * Registering the NetworkStateReceiver who gets the blog data from the Internet.
     */
    private void getDataFromTheInternet(){
        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkStateReceiver, intentFilter);
    }


    /**
     * This method processing API response.
     * If response is OK, JSON data is parsing and fill the ListView.
     * If response is not OK, the message toasts to screen.
     */
    public void fillListView(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONArray jsonBlogArray;
            listOfBlogs.clear();

            try {
                jsonBlogArray = new JSONArray(o.getMessage());
                for (int i = 0; i < jsonBlogArray.length(); i++) {
                    JSONObject jsonRowObject = jsonBlogArray.getJSONObject(i);
                    int id = jsonRowObject.getInt("id");
                    String idStr = Integer.toString(id);
                    String title = jsonRowObject.getString("title");
                    String imageUrl = jsonRowObject.getString("image_url");
                    String description = jsonRowObject.getString("description");

                    Blog newBlog =new Blog(idStr,title,imageUrl,description);
                    listOfBlogs.add(newBlog);


                    if ((jsonBlogArray.length()==(i+1))){
                        arrayOfBlogs = listOfBlogs.toArray(new Blog[listOfBlogs.size()]);
                        adapter = new CustomListAdapter(this,arrayOfBlogs);
                        blogList.setAdapter(adapter);
                    }


                }
                unregisterReceiver(networkStateReceiver);

            } catch (JSONException | IllegalArgumentException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * This is custom adapter for list view used in BlogListActivity.
     * It deals with blog objects.
     */
    public class CustomListAdapter extends ArrayAdapter<Blog> {

        private final Activity context;
        private final Blog[] arrayOfBlogs;


        public CustomListAdapter(Activity context, Blog[] arrayOfBlogs) {
            super(context, R.layout.blog_list_cell, arrayOfBlogs);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.arrayOfBlogs=arrayOfBlogs;
        }

        public View getView(int position,View view,ViewGroup parent) {
            View rowView;
            if (view == null) {
                LayoutInflater inflater=context.getLayoutInflater();
               rowView =inflater.inflate(R.layout.blog_list_cell, null, true);

            } else {
                rowView = view;
            }

            TextView txtTitle = (TextView) rowView.findViewById(R.id.tvBlogTitle);
            TextView txtDescription = (TextView) rowView.findViewById(R.id.tvBlogDescription);
            Blog blog = arrayOfBlogs[position];


            txtTitle.setText(blog.getTitle());
            txtDescription.setText(Html.fromHtml(blog.getDesription()));
            //Async task for downloading image
            if (blog.getImage()!=null){
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ivBlogImage);
                imageView.setImageBitmap(blog.getImage());
            }else {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ivBlogImage);
                imageView.setImageResource(android.R.color.transparent);
                ImageLoader myImageLoader = new ImageLoader(BlogListActivity.this, blog, rowView);
                myImageLoader.execute();
            }
            return rowView;



        }


    }
}
