import { getDevicePushToken, setNotificationHandler, showNotificationWhenAppInForeground, subscribe, unregister, unsubscribe } from "nativescript-pushy";
import { Observable } from "@nativescript/core";

export class HelloWorldModel extends Observable {
  public message: string;

  constructor() {
    super();

    setNotificationHandler(notification => {
      console.dir(notification);

      setTimeout(() => {
        alert({
          title: notification.title,
          message: notification.message,
          okButtonText: "OK"
        });
      }, 500);
    });
  }

  public doGetDevicePushToken(): void {
    getDevicePushToken()
        .then(token => {
          console.log(`getDevicePushToken success, token: ${token}`);
          this.set("message", "token: " + token);
        })
        .catch(err => {
          this.set("message", err)

          console.log(err.stack)
        });

    showNotificationWhenAppInForeground(true);
  }

  public doUnregister(): void {
    this.set("message", "");
    unregister();
  }

  public subscribeToGeneralTopic() {
    subscribe("general");
  }

  public unsubscribeToGeneralTopic() {
    unsubscribe("general");
  }
}
