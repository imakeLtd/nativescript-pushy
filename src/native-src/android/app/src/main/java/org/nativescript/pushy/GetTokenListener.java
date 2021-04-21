package org.nativescript.pushy;

public interface GetTokenListener {
    /**
     * Defines a handler callback method, which is used to pass get token handler
     * function reference from NativeScript to the Java Plugin
     * @param pushToken
     */
    void handler(String pushToken);
}
