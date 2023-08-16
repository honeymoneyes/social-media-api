package com.projects.socialmediaapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.socialmediaapi.security.advice.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.projects.socialmediaapi.security.constants.AuthConstants.ACCESS_DENIED;
import static com.projects.socialmediaapi.security.constants.TokenConstants.*;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized")
                .message(ACCESS_DENIED)
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
        if (request.getAttribute("incorrect") != null) {
            errorDetails.setMessage(JWT_TOKEN_NOT_FOUND);
        }
        if (request.getAttribute("expired") != null) {
            errorDetails.setMessage(JWT_TOKEN_EXPIRED);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
