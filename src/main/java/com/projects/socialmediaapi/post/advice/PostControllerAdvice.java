package com.projects.socialmediaapi.post.advice;

import com.projects.socialmediaapi.post.exceptions.*;
import com.projects.socialmediaapi.security.advice.ErrorDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.projects.socialmediaapi.security.constants.TokenConstants.DATE_TIME_FORMAT;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class PostControllerAdvice {

    @ExceptionHandler(UnauthorizedPostDeletedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorDetails handleUnauthorizedPostDeletedException(UnauthorizedPostDeletedException exception) {
        return ErrorDetails.builder()
                .status(FORBIDDEN.value())
                .error("POST_CANNOT_DELETE")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler(UnauthorizedPostUpdatedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorDetails handleUnauthorizedPostUpdatedException(UnauthorizedPostUpdatedException exception) {
        return ErrorDetails.builder()
                .status(FORBIDDEN.value())
                .error("POST_CANNOT_UPDATE")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handlePostNotFoundException(PostNotFoundException exception) {
        return ErrorDetails.builder()
                .status(NOT_FOUND.value())
                .error("POST_NOT_FOUND")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleImageNotFoundException(ImageNotFoundException exception) {
        return ErrorDetails.builder()
                .status(NOT_FOUND.value())
                .error("IMAGE_NOT_FOUND")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler(FilePathInvalidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleFilePathInvalidException(FilePathInvalidException exception) {
        return ErrorDetails.builder()
                .status(BAD_REQUEST.value())
                .error("FILE_PATH_INVALID")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }
}
