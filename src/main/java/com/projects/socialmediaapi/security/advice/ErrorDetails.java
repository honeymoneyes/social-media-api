package com.projects.socialmediaapi.security.advice;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorDetails {
    private int status;
    private String error;
    private List<String> message;
    private String timestamp;
}
