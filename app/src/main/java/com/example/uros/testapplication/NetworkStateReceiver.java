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
