import { Application } from "@nativescript/core";
import { TNSPushNotification } from "./";

declare const org: any;

(() => {
	const registerLifecycleEvents = () => {
		org.nativescript.pushy.LifecycleCallbacks.registerCallbacks(Application.android.nativeApp);
	};

	// Hook on the application events
	if (Application.android.nativeApp) {
		registerLifecycleEvents();
	} else {
		Application.on(Application.launchEvent, registerLifecycleEvents);
	}
  Application.on(Application.exitEvent, () => {
    org.nativescript.pushy.LifecycleCallbacks.exitCallback()
  });
})();

export function getDevicePushToken(): Promise<string> {
  return new Promise<string>((resolve, reject) => {
    org.nativescript.pushy.PushyBridgePlugin.getToken(
      new org.nativescript.pushy.GetTokenListener({
        handler: (pushToken) => {
          resolve(pushToken);
        }
      }), getActivity()
    );
  });
}

export function setNotificationHandler(handler: (notification: TNSPushNotification) => void): void {
  org.nativescript.pushy.PushyBridgePlugin.setNotificationHandler(
    new org.nativescript.pushy.NotificationTapListener({
      handler: (notification) => {
        try {
          const parsedNotification = JSON.parse(notification);
          parsedNotification.data = JSON.parse(parsedNotification.data);
          parsedNotification.data = JSON.parse(parsedNotification.data.__json);
          if (typeof parsedNotification.data.data === 'string') {
            parsedNotification.data = {
              ...parsedNotification.data,
              ...JSON.parse(parsedNotification.data.data)
            }

            delete parsedNotification.data.data;
          }

          handler(parsedNotification);
        } catch (e) {
          console.log(e);
        }
      },
    }), getActivity());
}

function getActivity() {
  return Application.android.foregroundActivity || Application.android.startActivity;
}

export function showNotificationWhenAppInForeground(show: boolean): void {
  org.nativescript.pushy.PushyBridgePlugin.showNotificationWhenAppInForeground(new java.lang.Boolean(!!show));
}

export function unregister() {
  org.nativescript.pushy.PushyBridgePlugin.unregister(getActivity().getApplicationContext());
}

export function isRegistered() {
  org.nativescript.pushy.PushyBridgePlugin.isRegistered(getActivity().getApplicationContext());
}

export function isConnected() {
  org.nativescript.pushy.PushyBridgePlugin.isConnected();
}

export function toggleNotifications(enabled: boolean): void {
  org.nativescript.pushy.PushyBridgePlugin.toggleNotifications(new java.lang.Boolean(!!enabled), getActivity().getApplicationContext());
}

export function toggleFCM(enabled: boolean): void {
  org.nativescript.pushy.PushyBridgePlugin.toggleFCM(new java.lang.Boolean(!!enabled), getActivity().getApplicationContext());
}

// TODO: validate topics with regex [a-zA-Z0-9-_.]+
export function subscribe(topic: string | string[]): void {
  if (typeof topic === 'object') {
    const jStringArr = new java.lang.String(topic);
    org.nativescript.pushy.PushyBridgePlugin.subscribe(jStringArr, getActivity());
  } else {
    org.nativescript.pushy.PushyBridgePlugin.subscribe(topic, getActivity());
  }
}

export function unsubscribe(topic: string | string[]): void {
  if (typeof topic === 'object') {
    const jStringArr = new java.lang.String(topic);
    org.nativescript.pushy.PushyBridgePlugin.unsubscribe(jStringArr, getActivity());
  } else {
    org.nativescript.pushy.PushyBridgePlugin.unsubscribe(topic, getActivity());
  }
}
