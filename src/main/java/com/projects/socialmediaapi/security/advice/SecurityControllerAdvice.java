package com.projects.socialmediaapi.security.advice;

import com.projects.socialmediaapi.security.exceptions.DuplicateLoginException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenExpirationException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenNotFoundException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.projects.socialmediaapi.user.services.impl.UserInteractionServiceImpl.getDateTimeFormatter;
import static com.projects.socialmediaapi.user.services.impl.UserInteractionServiceImpl.getErrorDetails;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class SecurityControllerAdvice {
    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleTokenRefreshException(RefreshTokenNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "REFRESH_TOKEN_NOT_EXIST", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(DuplicateLoginException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleDuplicateLoginException(DuplicateLoginException exception) {
        return getErrorDetails(CONFLICT, "DUPLICATE_LOGIN", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(RefreshTokenExpirationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorDetails handleRefreshTokenExpirationException(RefreshTokenExpirationException exception) {
        return getErrorDetails(UNAUTHORIZED, "UNAUTHORIZED", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ErrorDetails.builder()
                .status(BAD_REQUEST.value())
                .error("NOT_VALID")
                .timestamp(getDateTimeFormatter())
                .message(exception
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error-> error.getField() + ": " + error.getDefaultMessage())
                        .collect(toList()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        return getErrorDetails(BAD_REQUEST, "HEADER_IS_NOT_SET", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleBadCredentialsException(BadCredentialsException exception) {
        return getErrorDetails(BAD_REQUEST, "BAD_CREDENTIALS", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(DataAccessResourceFailureException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDetails handleBadCredentialsException(DataAccessResourceFailureException exception) {
        return getErrorDetails(INTERNAL_SERVER_ERROR, "DATABASE_ERROR", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
