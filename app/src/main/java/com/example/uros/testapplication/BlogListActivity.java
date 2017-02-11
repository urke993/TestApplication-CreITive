package com.example.uros.testapplication;

import android.app.Activity;
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

    private Session session;
    private CustomListAdapter adapter;
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
        new BlogListAsyncTask(this,session.getToken()).execute();

    }

    public void fillListView(JSONArray o) {
       /* if (!o.isSucess()){
            Toast.makeText(getApplicationContext(), o.getMessage(), Toast.LENGTH_SHORT).show();
        }else{*/
            JSONArray jsonBlogArray = null;

            try {
                jsonBlogArray = o;
                for (int i = 0; i < jsonBlogArray.length(); i++) {
                    JSONObject jsonRowObject = jsonBlogArray.getJSONObject(i);
                    int id = jsonRowObject.getInt("id");
                    String idStr = Integer.toString(id);
                    listOfBlogId.add(idStr);
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
           // adapter = new CustomListAdapter(this, arrayOfBlogId,arrayOfBlogTitle,arrayOfBlogImageUrl,arrayOfBlogDescription);

           // blogList.setAdapter(adapter);


       // }
    }
    public class CustomListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] arrayOfBlogId;
        private final String[] arrayOfBlogTitle;
        private final String[] arrayOfBlogImageUrl;
        private final String[] arrayOfBlogDescription;

        public CustomListAdapter(Activity context, String[] arrayOfBlogId, String[] arrayOfBlogTitle,String[] arrayOfBlogImageUrl, String[] arrayOfBlogDescription) {
            super(context, R.layout.blog_list_cell, arrayOfBlogTitle);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.arrayOfBlogId=arrayOfBlogId;
            this.arrayOfBlogTitle=arrayOfBlogTitle;
            this.arrayOfBlogImageUrl=arrayOfBlogImageUrl;
            this.arrayOfBlogDescription=arrayOfBlogDescription;
        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.blog_list_cell, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.tvBlogTitle);
            TextView txtId = (TextView) findViewById(R.id.tvBlogId);
            TextView txtDescription = (TextView) findViewById(R.id.tvBlogDescription);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.ivBlogImage);
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
            try {
                image = Ion.with(BlogListActivity.this).load(arrayOfBlogImageUrl[position]).withBitmap().asBitmap().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            txtTitle.setText(arrayOfBlogTitle[position]);
            txtId.setText(arrayOfBlogId[position]);
            txtDescription.setText(Html.fromHtml(arrayOfBlogDescription[position]));
            imageView.setImageBitmap(image);

            return rowView;



        };
    }
}
