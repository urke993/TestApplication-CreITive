package com.example.uros.testapplication;

import android.graphics.Bitmap;

/**
 * Created by Uros on 2/11/2017.
 */
public class Blog {
    private String blogId;
    private String title;
    private String imageUrl;
    private String desription;
    private Bitmap image;

    public Blog() {
    }

    public Blog(String blogId, String title, String imageUrl, String desription) {
        this.blogId = blogId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.desription = desription;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
