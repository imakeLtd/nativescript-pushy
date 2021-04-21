package org.nativescript.pushy;

/**
 * Defines methods for notificationHandler callbacks
 */
public interface NotificationTapListener {
    /**
     * Defines a handler callback method, which is used to pass notification handler
     * function reference from NativeScript to the Java Plugin
     * @param notificationString
     */
    void handler(String notificationString);
}
