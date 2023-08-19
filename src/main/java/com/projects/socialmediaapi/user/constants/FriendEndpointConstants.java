package com.projects.socialmediaapi.user.constants;

public class FriendEndpointConstants {

    public static final String MAIN_FRIENDS = "/user";

    public static final String SHOW_FRIENDS = "/friends/{userId}";

    public static final String SHOW_SUBSCRIBERS = "/subscribers/{userId}";

    public static final String FOLLOW = "/follow/{userId}";

    public static final String UNFOLLOW = "/unfollow/{userId}";

    public static final String REMOVE_FRIEND = "/remove/{userId}";

    public static final String ACCEPT_REQUEST_FRIEND = "/accept/{userId}";

    public static final String REJECT_REQUEST_FRIEND = "/reject/{userId}";
}
