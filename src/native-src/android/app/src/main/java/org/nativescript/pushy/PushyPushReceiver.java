package org.nativescript.pushy;

import me.pushy.sdk.Pushy;
import android.content.Intent;
import android.content.Context;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PushyPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }

        String notificationTitle = "MyApp";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationTitle = intent.getStringExtra("title");
            notificationText = intent.getStringExtra("message");
        }

        final JSONObject notification = new JSONObject();
        final JSONObject notificationData = new JSONObject();
        try {
            notification.put("title", intent.getStringExtra("title"));
            notification.put("message", intent.getStringExtra("message"));
            notification.put("appLaunchedByNotification", extras.getString("push_tapped"));
            // notification.put("android", extras);

            Iterator<String> it = extras.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key != "from" && key != "collapse_key" && key != "push_tapped") {
                    notificationData.put(key, extras.get(key));
                }
            }

            notification.put("data", notificationData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!notification.isNull("title") || !notification.isNull("data")) {
            Store.save(context, notification);
            PushyBridgePlugin.processPendingNotifications(context);
        }

        if (!PushyBridgePlugin.isActive || PushyBridgePlugin.canShowNotificationWhenAppInForeground) {
            this.showNotification(context, intent.getExtras(), notification, notificationTitle, notificationText);
        }
    }

    private void showNotification(Context context, Bundle extras, JSONObject notification, String notificationTitle, String notificationText) {
        int notificationId = (int) Math.floor((Math.random() * 1000) + 1);

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
        launchIntent
                .setAction(Intent.ACTION_MAIN)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                .putExtras(extras)
                .putExtra("push_tapped", true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(0)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(PendingIntent.getActivity(context, notificationId, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Pushy.setNotificationChannel(builder, context);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(notificationId, builder.build());
    }

}
