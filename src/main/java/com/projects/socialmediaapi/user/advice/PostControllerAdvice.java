package com.projects.socialmediaapi.user.advice;

import com.projects.socialmediaapi.security.advice.ErrorDetails;
import com.projects.socialmediaapi.user.exceptions.FilePathInvalidException;
import com.projects.socialmediaapi.user.exceptions.ImageNotFoundException;
import com.projects.socialmediaapi.user.exceptions.PostNotFoundException;
import com.projects.socialmediaapi.user.exceptions.UnauthorizedPostAction;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.projects.socialmediaapi.user.services.impl.UserInteractionServiceImpl.getErrorDetails;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class PostControllerAdvice {
    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(UnauthorizedPostAction.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorDetails handleUnauthorizedPostDeletedException(UnauthorizedPostAction exception) {
        return getErrorDetails(UNAUTHORIZED, "UNAUTHORIZED_POST_ACTION", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handlePostNotFoundException(PostNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "POST_NOT_FOUND", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleImageNotFoundException(ImageNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "IMAGE_NOT_FOUND", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(FilePathInvalidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleFilePathInvalidException(FilePathInvalidException exception) {
        return getErrorDetails(BAD_REQUEST, "FILE_PATH_INVALID", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
