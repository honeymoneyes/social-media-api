package com.projects.socialmediaapi.user.advice;

import com.projects.socialmediaapi.security.advice.ErrorDetails;
import com.projects.socialmediaapi.user.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.projects.socialmediaapi.user.services.UserInteractionService.getErrorDetails;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class UserControllerAdvice {

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleTokenRefreshException(UserNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "USER_NOT_EXIST", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleUserAlreadyExistException(UserAlreadyExistException exception) {
        return getErrorDetails(CONFLICT, "USER_ALREADY_EXIST", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(SubscriberNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleSubscriberNotFoundException(SubscriberNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "SUBSCRIBER_NOT_FOUND", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(SubscriberAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleSubscriberAlreadyExistException(SubscriberAlreadyExistException exception) {
        return getErrorDetails(CONFLICT, "SUBSCRIBER_ALREADY_EXIST", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(SelfActionException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleSelfSubscriptionException(SelfActionException exception) {
        return getErrorDetails(CONFLICT, "SELF_SUBSCRIPTION_ERROR", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(UsersAlreadyFriendsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleUsersAlreadyFriendsException(UsersAlreadyFriendsException exception) {
        return getErrorDetails(CONFLICT, "USERS_ALREADY_FRIENDS", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(FriendshipRequestNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleFriendshipRequestNotFoundException(FriendshipRequestNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "FRIENDSHIP_REQUEST_NOT_FOUND", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MessageSendingNotAllowedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorDetails handleMessageSendingNotAllowedException(MessageSendingNotAllowedException exception) {
        return getErrorDetails(METHOD_NOT_ALLOWED, "MESSAGE_NOT_ALLOWED_ERROR", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(ChatNotAllowedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorDetails handleChatNotAllowedException(ChatNotAllowedException exception) {
        return getErrorDetails(METHOD_NOT_ALLOWED, "CHAT_NOT_ALLOWED_ERROR", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleIllegalArgumentException(IllegalArgumentException exception) {
        return getErrorDetails(BAD_REQUEST, "ILLEGAL_ARGUMENT", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(FriendNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleFriendNotFoundException(FriendNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "FRIEND_NOT_FOUND", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(FriendAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleFriendAlreadyExistException(FriendAlreadyExistException exception) {
        return getErrorDetails(CONFLICT, "YOU_ARE_FRIENDS", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------
}

