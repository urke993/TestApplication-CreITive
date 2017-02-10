package com.example.uros.testapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlogListActivity extends AppCompatActivity {

    private Session session;
    ListView blogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        setTitle("Blog List");
        session = new Session(this);
        if (!session.loggedin()){
            finish();
        }
        blogList = (ListView) findViewById(R.id.listViewBlogs);
    }

    public void fillListView(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONArray jsonBlogArray = null;
            String[] arrayOfBlogId;
            String[] arrayOfBlogTitle;
            String[] arrayOfBlogImageUrl;
            String[] arrayOfBlogDescription;
            List<String> listOfBlogId = new ArrayList<>();
            List<String> listOfBlogTitle = new ArrayList<>();
            List<String> listOfBlogImageUrl = new ArrayList<>();
            List<String> listOfBlogDescription = new ArrayList<>();
            try {
                jsonBlogArray = new JSONArray(o.getMessage());
                for (int i = 0; i < jsonBlogArray.length(); i++) {
                    JSONObject jsonRowObject = jsonBlogArray.getJSONObject(i);
                    listOfBlogId.add(jsonRowObject.getString("id"));
                    listOfBlogTitle.add(jsonRowObject.getString("title"));
                    listOfBlogImageUrl.add(jsonRowObject.getString("image_url"));
                    listOfBlogDescription.add(jsonRowObject.getString("description"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrayOfBlogId = listOfBlogId.toArray(new String[listOfBlogId.size()]);
            arrayOfBlogTitle = listOfBlogTitle.toArray(new String[listOfBlogTitle.size()]);
            arrayOfBlogImageUrl = listOfBlogImageUrl.toArray(new String[listOfBlogImageUrl.size()]);
            arrayOfBlogDescription = listOfBlogDescription.toArray(new String[listOfBlogDescription.size()]);


        }
    }
}
