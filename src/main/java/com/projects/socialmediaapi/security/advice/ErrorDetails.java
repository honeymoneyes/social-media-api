package com.projects.socialmediaapi.security.advice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetails {
    private int status;
    private String error;
    private String message;
    private String timestamp;
}
