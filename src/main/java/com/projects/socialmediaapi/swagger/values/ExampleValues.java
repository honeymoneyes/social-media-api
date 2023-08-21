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
                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IiQyYSQxMC" +
                               "RhUEtJOExSWVBjZWlQV1VBMGNXRTF1OFV0cmM2MFJyeW9PSDNaSnNBL05EcVhiQ3Z" +
                               "3ZWlKbSIsImlkIjoxLCJlbWFpbCI6InVzZXIxQGdtYWlsLmNvbSIsInVzZXJuYW1l" +
                               "IjoidXNlcjEiLCJqdGkiOiI4NDY4MWU2OC04ZjdmLTQ5MzgtOGE0NC1lYWYwNjkzY" +
                               "jkxYTAiLCJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpc3MiOiJob25leW1vbmV5Ii" +
                               "wiaWF0IjoxNjkyNTk4MDQ1LCJleHAiOjE2OTI1OTkyNDV9.turwAboN2lby0EDhFz" +
                               "lIiCp48shSusRMN9MjpDaJ9Ok",
                "refreshToken": "d7242539-3b41-4880-bd5e-ce8f478eacc0",
                "tokenType": "Bearer"
            }
            """;

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
                "type": "Bearer ",
                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IiQyYSQxMCRTU0x1QlhGRkpna1MwaHdsMTBScHQuWi9qNk" +
                               "dMSFpHNm5SVzJKekRoTFlldWFKQUp5aHpKcSIsImlkIjoxLCJlbWFpbCI6InVz" +
                               "ZXIyQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoidXNlcjIiLCJqdGkiOiIzOWE3NT" +
                               "AyMy1mZDZhLTQ4MWEtODA5OC04MDMxNTkyNWNhNjAiLCJzdWIiOiJ1c2VyMkBn" +
                               "bWFpbC5jb20iLCJpc3MiOiJob25leW1vbmV5IiwiaWF0IjoxNjkyNTk3Nzg3LCJ" +
                               "leHAiOjE2OTI1OTg5ODd9.nA0AmMf3bIiQLa1PhrmYbIKmlcnA36HW63B_DjDCt" +
                               "bQ",
                "refreshToken": "2b83d6c0-5f4a-4435-88b8-9a0665cf7b5a"
            }
            """;

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

    public static final String SELF_SUBSCRIPTION_ERROR = """
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

    public static final String UNFOLLOW_SUCCES = """
            {
                "message": "You unsubscribed from user2"
            }""";
}
