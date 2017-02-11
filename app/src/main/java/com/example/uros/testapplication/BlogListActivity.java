package com.example.uros.testapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlogListActivity extends AppCompatActivity {

    String[] arrayOfBlogId;
    String[] arrayOfBlogTitle;
    String[] arrayOfBlogImageUrl;
    String[] arrayOfBlogDescription;
    List<String> listOfBlogId = new ArrayList<>();
    List<String> listOfBlogTitle = new ArrayList<>();
    List<String> listOfBlogImageUrl = new ArrayList<>();
    List<String> listOfBlogDescription = new ArrayList<>();

    List<Blog> listOfBlogs;
    public Blog[] arrayOfBlogs;

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
        blogList = (ListView) findViewById(R.id.listViewBlogs);
        new BlogListAsyncTask(this,session.getToken()).execute();

    }

    public void fillListView(HttpResponse o) {
        if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{
            JSONArray jsonBlogArray = null;

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

                    arrayOfBlogs = listOfBlogs.toArray(new Blog[listOfBlogs.size()]);

                    adapter = new CustomListAdapter(this, arrayOfBlogs);

                    blogList.setAdapter(adapter);


                }

            } catch (JSONException e) {
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
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.blog_list_cell, null, true);

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
                ImageLoader myImageLoader = new ImageLoader(BlogListActivity.this, blog, rowView);
                myImageLoader.execute();
            }
            return rowView;



        };
    }
}
