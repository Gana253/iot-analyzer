package com.java.relay42.constants;

/**
 * Generic constants file for the application
 */
public final class IotConstants {
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    private IotConstants() {
    }
}
