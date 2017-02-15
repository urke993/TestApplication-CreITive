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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;


/**
 * Downloading the image from the Internet in background.
 * Stores the image in blog object in array of blogs.
 * It is called from CustomListAdapter.
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

    /**
     *Resizing the image from the original size to smaller
     *aceptable for android devices.
     */
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
