package com.microservices.authservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessTokenResponse {
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private int refresh_expires_in;
    private String token_type;
    private String error;
    private String error_description;
}
