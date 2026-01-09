package com.microservices.authservice.controller;

import com.microservices.authservice.model.DataResponse;
import com.microservices.authservice.model.LoginData;
import com.microservices.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> loginKeyCloak(@RequestBody LoginData userData) {
        return authService.login(userData);
    }

    @PostMapping(path = "/update-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> refreshTokenKeyCloak(@RequestHeader String refreshToken) {
        return authService.updateToken(refreshToken);
    }


}
