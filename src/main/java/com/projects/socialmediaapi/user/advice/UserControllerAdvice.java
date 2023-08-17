package com.projects.socialmediaapi.user.advice;

import com.projects.socialmediaapi.security.advice.ErrorDetails;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(value = UserNotFoundException.class)
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
}

