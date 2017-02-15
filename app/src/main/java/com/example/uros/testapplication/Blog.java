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

import android.graphics.Bitmap;

/**
 * Used like domain object for downloaded blogs for blog list.
 */
public class Blog {
    private String blogId;
    private String title;
    private String imageUrl;
    private String desription;
    private Bitmap image;

    public Blog(String blogId, String title, String imageUrl, String desription) {
        this.blogId = blogId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.desription = desription;
    }

    public String getBlogId() {
        return blogId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDesription() {
        return desription;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
