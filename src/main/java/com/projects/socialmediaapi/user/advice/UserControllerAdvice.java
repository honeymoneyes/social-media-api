package com.projects.socialmediaapi.user.advice;

import com.projects.socialmediaapi.security.advice.ErrorDetails;
import com.projects.socialmediaapi.user.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class UserControllerAdvice {

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleTokenRefreshException(UserNotFoundException exception) {
        return ErrorDetails.builder()
                .status(NOT_FOUND.value())
                .error("USER_NOT_EXIST")
                .timestamp(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();

    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleUserAlreadyExistException(UserAlreadyExistException exception) {
        return ErrorDetails.builder()
                .status(CONFLICT.value())
                .error("USER_ALREADY_EXIST")
                .timestamp(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();

    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(SubscriberNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleSubscriberNotFoundException(SubscriberNotFoundException exception) {
        return ErrorDetails.builder()
                .status(NOT_FOUND.value())
                .error("SUBSCRIBER_NOT_FOUND")
                .timestamp(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(SubscriberAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleSubscriberAlreadyExistException(SubscriberAlreadyExistException exception) {
        return ErrorDetails.builder()
                .status(CONFLICT.value())
                .error("SUBSCRIBER_ALREADY_EXIST")
                .timestamp(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();

    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(SelfActionException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleSelfSubscriptionException(SelfActionException exception) {
        return ErrorDetails.builder()
                .status(CONFLICT.value())
                .error("SELF_SUBSCRIPTION_ERROR")
                .timestamp(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss", Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();

    }

    // -----------------------------------------------------------------------------------------------------------------
}

