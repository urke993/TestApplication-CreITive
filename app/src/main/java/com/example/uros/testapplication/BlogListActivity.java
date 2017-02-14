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


public class BlogListActivity extends AppCompatActivity {

    List<Blog> listOfBlogs;
    public Blog[] arrayOfBlogs;
    private NetworkStateReceiver networkStateReceiver;

    private Session session;
    private CustomListAdapter adapter;
    ListView blogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        setTitle("Blog List");
        listOfBlogs = new ArrayList<>();
        session = new Session(this);
        if (!session.loggedin()){
            finish();
        }
        if (!session.isOnline(BlogListActivity.this)){
            session.showAlertDialog(BlogListActivity.this,"No Internet Connection", "You are offline, please check your internet connection.");
        }


        blogList = (ListView) findViewById(R.id.listViewBlogs);

        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkStateReceiver, intentFilter);


        blogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Blog chosenBlog = arrayOfBlogs[+position];
                // 1. create an intent pass class name or intnet action name
                Intent intent = new Intent("android.intent.action.BLOGDISPLAYACTIVITY");
                // 2. put key/value data
                intent.putExtra("blogIdMessage", chosenBlog.getBlogId());
                // 5. start the activity
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }



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

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }



       }
    }
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
            View rowView =null;
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



        };


    }
}
