package org.nativescript.pushy;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import me.pushy.sdk.Pushy;

public class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Object> {
    Activity mActivity;

    public RegisterForPushNotificationsAsync(Activity activity) {
        this.mActivity = activity;
    }

    protected Object doInBackground(Void... params) {
        try {
            // Register the device for notifications
            String deviceToken = Pushy.register(this.mActivity.getApplicationContext());

            // Registration succeeded, log token to logcat
            Log.d("Pushy", "Pushy device token: " + deviceToken);

            // Registration success, Attach the token
            PushyBridgePlugin.token = deviceToken;

            if (PushyBridgePlugin.getTokenCallback != null) {
                PushyBridgePlugin.getTokenCallback.handler(deviceToken);
            }

            // Provide token to onPostExecute()
            return deviceToken;
        }
        catch (Exception exc) {
            // Registration failed, provide exception to onPostExecute()
            return exc;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof Exception) {
            // Registration failed
            Log.e("Pushy", ((Exception) result).getMessage());
        }
    }
}
