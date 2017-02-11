package com.example.uros.testapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;


/**
 * Created by Uros on 2/11/2017.
 */
public class ImageLoader extends AsyncTask {
    Context context;
    private Blog blog;
    private View view;

    public ImageLoader(Context context,Blog blog, View view){
        this.context = context;
        this.blog = blog;
        this.view = view;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        Bitmap image = null;
        try {
            image = Ion.with(context).load(blog.getImageUrl()).withBitmap().asBitmap().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Object o) {
        Bitmap image = (Bitmap) o;

        ImageView imageView = (ImageView) view.findViewById(R.id.ivBlogImage);
        imageView.setImageBitmap(image);
        blog.setImage(image);

    }
}
