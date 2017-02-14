package com.example.uros.testapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

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

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Object o) {
        Bitmap image = getResizedBitmap((Bitmap) o,300,300);
        ImageView imageView = (ImageView) view.findViewById(R.id.ivBlogImage);
        imageView.setImageBitmap(image);
        blog.setImage(image);

    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
