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

import static com.projects.socialmediaapi.security.constants.AuthConstants.ACCESS_DENIED;
import static com.projects.socialmediaapi.security.constants.TokenConstants.JWT_TOKEN_EXPIRED;
import static com.projects.socialmediaapi.security.constants.TokenConstants.JWT_TOKEN_NOT_FOUND;
import static com.projects.socialmediaapi.user.services.UserInteractionService.getErrorDetails;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        responseSetDetails(response);
        ErrorDetails errorDetails = getErrorDetails(
                UNAUTHORIZED,
                "UNAUTHORIZED",
                new Exception(ACCESS_DENIED));
        messageDetails(request, errorDetails);
        sendResponse(response, errorDetails);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void sendResponse(HttpServletResponse response, ErrorDetails errorDetails) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void responseSetDetails(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(SC_UNAUTHORIZED);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void messageDetails(HttpServletRequest request, ErrorDetails errorDetails) {
        if (request.getAttribute("incorrect") != null) {
            errorDetails.setMessage(singletonList(JWT_TOKEN_NOT_FOUND));
        }
        if (request.getAttribute("expired") != null) {
            errorDetails.setMessage(singletonList(JWT_TOKEN_EXPIRED));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
}
