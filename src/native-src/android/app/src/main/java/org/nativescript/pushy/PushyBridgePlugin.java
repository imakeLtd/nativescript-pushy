package org.nativescript.pushy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class PushyBridgePlugin {
  static final String TAG = "PushyBridgePlugin";
  public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
  public static String token = null;
  public static Boolean isActive = false;
  public static Boolean canShowNotificationWhenAppInForeground = true;
  public static NotificationTapListener notificationHandlerCallback = null;
  public static GetTokenListener getTokenCallback = null;

  public static void getToken(GetTokenListener callbacks, Activity activity) {
    if (callbacks != null) {
      getTokenCallback = callbacks;
    }

    if (!Pushy.isRegistered(activity.getApplicationContext())) {
      new RegisterForPushNotificationsAsync(activity).execute();
    } else {
      PushyBridgePlugin.token = Pushy.getDeviceCredentials(activity.getApplicationContext()).token;

      if (PushyBridgePlugin.getTokenCallback != null) {
        getTokenCallback.handler(PushyBridgePlugin.token);
      }
    }
    Pushy.listen(activity);
  }

  public static boolean isRegistered(Context context) {
    return Pushy.isRegistered(context);
  }

  public static boolean isConnected() {
    return Pushy.isConnected();
  }

  public static void toggleNotifications(boolean enabled, Context context) {
    Pushy.toggleNotifications(enabled, context);
  }

  public static void toggleFCM(boolean enabled, Context context) {
    Pushy.toggleFCM(enabled, context);
  }

  public static void subscribe(String topicName, Activity activity) {
    new SubscribeToTopicAsync(topicName, activity).execute();
  }

  public static void subscribe(String[] topicName, Activity activity) {
    new SubscribeToTopicAsync(topicName, activity).execute();
  }

  public static void unsubscribe(String topicName, Activity activity) {
    new UnsubscribeFromTopicAsync(topicName, activity).execute();
  }

  public static void unsubscribe(String[] topicName, Activity activity) {
    new UnsubscribeFromTopicAsync(topicName, activity).execute();
  }

  public static void showNotificationWhenAppInForeground(Boolean show) {
    canShowNotificationWhenAppInForeground = show;
  }

  public static void setNotificationHandler(NotificationTapListener callbacks, Activity activity) {
    notificationHandlerCallback = callbacks;

    Bundle extras = activity.getIntent().getExtras();
    if (extras != null && extras.getBoolean("push_tapped")) {
      final JSONObject notification = new JSONObject();
      final JSONObject notificationData = new JSONObject();
      try {
        notification.put("title", extras.getString("title"));
        notification.put("message", extras.getString("message"));
        notification.put("appLaunchedByNotification", extras.getString("push_tapped"));
        notification.put("foreground", true);

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
        Store.save(activity.getApplicationContext(), notification);
        PushyBridgePlugin.processPendingNotifications(activity.getApplicationContext());
      }

      Intent intent = activity.getIntent();
      intent.replaceExtras(new android.os.Bundle());
      intent.setAction("");
      intent.setData(null);
      intent.setFlags(0);
    }

  }

  public static void processPendingNotifications(Context context) {
    if (notificationHandlerCallback != null) {
      for (Map.Entry<String, String> entry : Store.getAll(context).entrySet()) {
        final String notificationString = entry.getValue();

        try {
          JSONObject notification = new JSONObject(notificationString);

          Log.d(TAG, notificationString);

          notificationHandlerCallback.handler(notification.toString());

          final int notificationID = notification.optInt("id", 0);

          Store.remove(context, notificationID);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void unregister(Context context) {
    Pushy.unregister(context);
  }
}



