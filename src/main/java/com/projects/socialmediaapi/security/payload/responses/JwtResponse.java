package com.projects.socialmediaapi.security.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    @JsonProperty("access-token")
    private String token;
    private final String type = "Bearer ";
    @JsonProperty("refresh-token")
    private String refresh;
}
