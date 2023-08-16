package com.projects.socialmediaapi.auth.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    @JsonProperty("access-token")
    private String token;
    @JsonProperty("refresh-token")
    private String refresh;
}
