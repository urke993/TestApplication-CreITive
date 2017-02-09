package com.example.uros.testapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BlogListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        setTitle("Blog List");
    }
}
