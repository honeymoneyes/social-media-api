package com.projects.socialmediaapi.user.constants;

public class EndpointConstants {

    public static final String MAIN = "/user";

    public static final String CREATE_POST = "/posts/create";

    public static final String SHOW_ALL_POSTS = "/{userId}/posts";

    public static final String SHOW_IMAGE_OF_POST = "/{postId}/image";

    public static final String UPDATE_POST = "/posts/{postId}/update";

    public static final String DELETE_POST = "/posts/{postId}/delete";
}
