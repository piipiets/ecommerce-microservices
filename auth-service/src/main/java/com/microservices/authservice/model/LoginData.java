package com.microservices.authservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginData {
    String username;
    String password;
}
