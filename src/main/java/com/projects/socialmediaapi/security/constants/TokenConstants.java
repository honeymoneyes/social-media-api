package com.projects.socialmediaapi.security.constants;

public class TokenConstants {
    public static final String JWT_TOKEN_EXPIRED = "JWT token was expired. Please make a refresh request";

    public static final String JWT_TOKEN_NOT_FOUND = "JWT token doesn't exist";

    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token was expired. Please make a new sign-in request";

    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token is not in database!";

    public static final String DATE_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss";
}
