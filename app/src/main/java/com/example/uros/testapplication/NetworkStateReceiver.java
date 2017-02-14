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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Uros on 2/13/2017.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private Session session;
    private String blogIdPassed;
    @Override
    public void onReceive(Context context, Intent intent) {
        session = new Session(context);

        if (session.isOnline(context)){
            if (context instanceof BlogDisplayActivity){
                new BlogDisplayAsyncTask((BlogDisplayActivity) context,session.getToken(),blogIdPassed).execute();
            }else if (context instanceof BlogListActivity) {
                new BlogListAsyncTask((BlogListActivity) context, session.getToken()).execute();
            }
        }

    }

    public void setBlogIdPassed(String blogIdPassed) {
        this.blogIdPassed = blogIdPassed;
    }
}
