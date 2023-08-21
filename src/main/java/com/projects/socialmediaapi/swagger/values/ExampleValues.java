package com.projects.socialmediaapi.swagger.values;

public class ExampleValues {

    public static final String JWT_TOKEN_EXPIRED = """
                {
                    "status": 401,
                    "error": "UNAUTHORIZED",
                    "message": [
                        "JWT token was expired. Please make a refresh request"
                    ],
                    "timestamp": "08-21-2023 08:25:27"
                }
            """;
    public static final String REFRESH_TOKEN_NOT_EXIST = """
            {
                "status": 404,
                "error": "REFRESH_TOKEN_NOT_EXIST",
                "message": [
                    "Failed for [9b78adc9-3706-4a04-818c-029a913b7090]: Refresh token is not in database!"
                ],
                "timestamp": "08-21-2023 08:17:44"
            }""";

    public static final String REFRESH_TOKEN_EXPIRED = """
            {
                "status": 401,
                "error": "UNAUTHORIZED",
                "message": [
                    "Refresh token was expired. Please make a new sign-in request"
                ],
                "timestamp": "08-21-2023 08:23:41"
            }""";

    public static final String REFRESH_TOKEN_NOT_VALID = """
            {
                "status": 400,
                "error": "NOT_VALID",
                "message": [
                    "refreshToken: Refresh token shouldn't be empty!"
                ],
                "timestamp": "08-21-2023 08:14:46"
            }
            """;

    public static final String REFRESH_TOKEN_UPDATED = """
            {
                "access-token": "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IiQyYSQxMCRGWjJFR0htREHucHFZWDVFSVJHN0p1SFEvMW9jS2xLc1oyMnJhRzB1dkswcENkM2oyZVRsbyIsImlkIjoxLCJlbWFpbCI6InN0cmluZ0BnbWFpbC5jb20iLCJ",
                               
                "refresh-token": "d1f71dad-30be-4bb3-b73a-9226a79b7838",
                "tokenType": "Bearer"
            }""";

    public static final String DUPLICATE_LOGIN = """
            {
                "status": 409,
                "error": "DUPLICATE_LOGIN",
                "message": [
                    "Login already done."
                ],
                "timestamp": "08-21-2023 06:16:06"
            }
            """;

    public static final String USER_NOT_FOUND = """
            {
                "status": 404,
                "error": "USER_NOT_EXIST",
                "message": [
                    "User is not found!"
                ],
                "timestamp": "08-21-2023 06:16:06"
            }
            """;

    public static final String LOGIN_NOT_VALID = """
            {
                "status": 400,
                "error": "NOT_VALID",
                "message": [
                    "email: Email address has invalid format",
                    "password: Password shouldn't be empty",
                    "email: Email shouldn't be empty"
                ],
                "timestamp": "08-21-2023 06:16:06"
            }
            """;

    public static final String LOGIN_OK = """
            {
              "type": "Bearer",
              "access-token": "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IiQyYSQxMCRGWjJFR0htREHucHFZWDVFSVJHN0p1SFEvMW9jS2xLc1oyMnJhRzB1dkswcENkM2oyZVRsbyIsImlkIjoxLCJlbWFpbCI6InN0cmluZ0BnbWFpbC5jb20iLCJ",
                               
              "refresh-token": "d1f71dad-30be-4bb3-b73a-9226a79b7838"
            }""";

    public static final String REGISTER_NOT_VALID = """
            {
                "status": 400,
                "error": "NOT_VALID",
                "message": [
                    "username: Username shouldn't be empty",
                    "password: Password shouldn't be empty",
                    "email: Email address has invalid format",
                    "email: Email shouldn't be empty"
                ],
                "timestamp": "08-21-2023 06:16:06"
            }
            """;

    public static final String REGISTER_OK = """
            {
                "message": "User successfully registered!"
            }
            """;

    public static final String BAD_CREDENTIALS = """
            {
                "status": 400,
                "error": "BAD_CREDENTIALS",
                "message": [
                    "Bad credentials"
                ],
                "timestamp": "08-21-2023 08:52:07"
            }""";

    public static final String USER_ALREADY_EXIST = """
            {
                "status": 409,
                "error": "USER_ALREADY_EXIST",
                "message": [
                    "User is already exist!"
                ],
                "timestamp": "08-21-2023 08:57:12"
            }""";

    public static final String FOLLOW_SUCCESS = """
            {
                "message": "You've signed up for user2"
            }""";

    public static final String SELF_FOLLOW_UNFOLLOW_ERROR = """
            {
                "status": 409,
                "error": "SELF_SUBSCRIPTION_ERROR",
                "message": [
                    "You can't action to yourself"
                ],
                "timestamp": "08-21-2023 09:13:17"
            }""";

    public static final String UNAUTHORIZED_ACCESS = """
            {
                "status": 401,
                "error": "UNAUTHORIZED",
                "message": [
                    "User doesn't have access"
                ],
                "timestamp": "08-21-2023 09:14:46"
            }""";

    public static final String SUBSCRIBER_ALREADY_EXIST = """
            {
                "status": 409,
                "error": "SUBSCRIBER_ALREADY_EXIST",
                "message": [
                    "You're already subscribed to this person!"
                ],
                "timestamp": "08-21-2023 09:19:25"
            }""";

    public static final String SUBSCRIBER_NOT_FOUND = """
            {
                "status": 404,
                "error": "SUBSCRIBER_NOT_FOUND",
                "message": [
                    "Subscriber is not found!"
                ],
                "timestamp": "08-21-2023 09:26:51"
            }""";

    public static final String UNFOLLOW_SUCCESS = """
            {
                "message": "You unsubscribed from user2"
            }""";

    public static final String REQUEST_FRIEND_SUCCESS = """
            {
                "message": "You unsubscribed from user2"
            }""";

    public static final String JWT_TOKEN_NOT_EXIST = """
            {
                "status": 401,
                "error": "UNAUTHORIZED",
                "message": [
                    "JWT token doesn't exist"
                ],
                "timestamp": "08-21-2023 09:40:35"
            }""";

    public static final String FRIENDSHIP_REQUEST_NOT_FOUND = """
            {
                "status": 404,
                "error": "FRIENDSHIP_REQUEST_NOT_FOUND",
                "message": [
                    "Friendship is not found"
                ],
                "timestamp": "08-21-2023 09:57:51"
            }""";

    public static final String FRIENDSHIP_REQUEST_REJECTED = """
            {
                "message": "Friendship request for user user1 rejected"
            }""";

    public static final String FRIEND_REMOVE_SUCCESS = """
            {
                "message": "You deleted a friend named user1"
            }""";

    public static final String FRIEND_IS_NOT_FOUND = """
            {
                "status": 404,
                "error": "FRIEND_NOT_FOUND",
                "message": [
                    "Friend is not found!"
                ],
                "timestamp": "08-21-2023 10:12:53"
            }""";

    public static final String POST_REQUEST_NOT_VALID = """
            {
                "status": 400,
                "error": "NOT_VALID",
                "message": [
                    "title: Title shouldn't be empty",
                    "body: Body shouldn't be empty"
                ],
                "timestamp": "08-21-2023 12:56:31"
            }""";

    public static final String UNAUTHORIZED_POST_ACTION = """
            {
                "status": 401,
                "error": "UNAUTHORIZED_POST_ACTION",
                "message": [
                    "You can't do this action with that post"
                ],
                "timestamp": "08-21-2023 13:27:27"
            }""";

    public static final String POST_DELETED = """
            {
                "id": 1,
                "message": "Post with id 1 deleted!"
            }""";

    public static final String POST_UPDATED = """
            {
                "id": 3,
                "message": "Post with id 3 updated!"
            }""";

    public static final String POST_NOT_FOUND = """
            {
                "status": 404,
                "error": "POST_NOT_FOUND",
                "message": [
                    "Post is not found!"
                ],
                "timestamp": "08-21-2023 13:38:24"
            }""";

    public static final String POST_CREATED = """
            {
                "title": "Пост User1",
                "body": "О посте User1 - пост третий",
                "imageResponse": {
                    "fileName": "d14f66ddf892b7406a2113e022698a6e.jpg",
                    "downloadUri": "http://localhost:8080/downloadFile/2",
                    "contentType": "image/jpeg",
                    "size": 45575
                },
                "message": "Post is successfully created!"
            }""";

    public static final String POSTS_RETURN_SUCCESS = """
            {
                "posts": [
                    {
                        "id": 1,
                        "title": "Пост User1",
                        "body": "О посте User1 - пост третий",
                        "timestamp": "2023-08-21T14:42:51",
                        "image": {
                            "id": 1,
                            "fileName": "d14f66ddf892b7406a2113e022698a6e.jpg",
                            "fileType": "image/jpeg"
                        }
                    }
                ]
            }""";

    public static final String IMAGE_RETURN_SUCCESS = "{\n\"file\": \"downloadFile\"\n}";

    public static final String SEND_MESSAGE_SUCCESS = """
            {
                "senderUsername": "user1",
                "receiverUsername": "user2",
                "text": "Всем привет. Это тест отправки сообщений.",
                "timestamp": "2023-08-21T14:54:31"
            }""";

    public static final String SEND_MESSAGE_DENIED = """
            {
                "status": 405,
                "error": "MESSAGE_NOT_ALLOWED_ERROR",
                "message": [
                    "You cannot send a message to a user that is not yours friend"
                ],
                "timestamp": "08-21-2023 14:53:37"
            }""";

    public static final String SHOW_CHAT_SUCCESS = """
            [
                {
                    "senderUsername": "user1",
                    "receiverUsername": "user2",
                    "text": "Чем занимаешься?",
                    "timestamp": "2023-08-21T04:22:29"
                },
                {
                    "senderUsername": "user1",
                    "receiverUsername": "user2",
                    "text": "Как день прошел?",
                    "timestamp": "2023-08-21T04:22:37"
                },
                {
                    "senderUsername": "user2",
                    "receiverUsername": "user1",
                    "text": "Код пишу",
                    "timestamp": "2023-08-21T04:24:10"
                },
                {
                    "senderUsername": "user2",
                    "receiverUsername": "user1",
                    "text": "Дома был.",
                    "timestamp": "2023-08-21T04:24:17"
                }
            ]""";

    public static final String SHOW_CHAT_DENIED = """
            {
                "status": 405,
                "error": "CHAT_NOT_ALLOWED_ERROR",
                "message": [
                    "You must be friends!"
                ],
                "timestamp": "08-21-2023 15:00:09"
            }""";

    public static final String FEED_RECEIVED_SUCCESS = """
            {
              "totalItems": 0,
              "feed": [
                {
                  "username": "user4",
                  "lastPost": {
                    "title": "Пост User4",
                    "body": "О посте User4 - пост второй",
                    "timestamp": "2023-08-21T15:39:04",
                    "imageInfo": {
                      "filename": "d14f66ddf892b7406a2113e022698a6e.jpg"
                    }
                  }
                },
                {
                  "username": "user3",
                  "lastPost": {
                    "title": "Пост User3",
                    "body": "О посте User5 - пост второй",
                    "timestamp": "2023-08-21T15:38:45",
                    "imageInfo": {
                      "filename": "d14f66ddf892b7406a2113e022698a6e.jpg"
                    }
                  }
                },
                {
                  "username": "user2",
                  "lastPost": {
                    "title": "Пост User5",
                    "body": "О посте User5 - пост второй",
                    "timestamp": "2023-08-21T15:38:24",
                    "imageInfo": {
                      "filename": "d14f66ddf892b7406a2113e022698a6e.jpg"
                    }
                  }
                }
              ],
              "totalPages": 0,
              "currentPage": 0
            }
            """;
}
