package org.nativescript.pushy;

import android.app.Activity;
import android.os.AsyncTask;
import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class SubscribeToTopicAsync extends AsyncTask<Void, Void, Object> {
    Activity mActivity;
    String topic = null;
    String[] topics = null;

    public SubscribeToTopicAsync(String topic, Activity activity) {
        this.topic = topic;
        this.mActivity = activity;
    }
    public SubscribeToTopicAsync(String[] topics, Activity activity) {
        this.topics = topics;
        this.mActivity = activity;
    }

    protected Object doInBackground(Void... params) {
        try {
            if (this.topic != null) {
                Pushy.subscribe(this.topic, this.mActivity.getApplicationContext());
            } else {
                Pushy.subscribe(this.topics, this.mActivity.getApplicationContext());
            }
        } catch (PushyException e) {
            e.printStackTrace();
        }

        return new Object();
    }

    @Override
    protected void onPostExecute(Object result) {
    }
}
