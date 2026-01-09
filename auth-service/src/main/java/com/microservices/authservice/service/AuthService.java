package com.microservices.authservice.service;

import com.microservices.authservice.model.AccessTokenResponse;
import com.microservices.authservice.model.DataResponse;
import com.microservices.authservice.model.LoginData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;


import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    @Value("${keycloak-server-url}")
    private String serverUrl;
    @Value("${realm}")
    private String realm;
    @Value("${clientId}")
    private String clientId;

    public ResponseEntity<DataResponse> login(LoginData loginData){
        try{
            String uri = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            String requestBody = "grant_type=" + "password" +
                    "&client_id=" + clientId +
                    "&username=" + loginData.getUsername() +
                    "&password=" + loginData.getPassword();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<AccessTokenResponse> accessTokenResponse = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    request,
                    AccessTokenResponse.class);

            if (accessTokenResponse.getStatusCode().is2xxSuccessful()){
                return ResponseEntity.ok()
                        .body(new DataResponse("Success", "Login Success", new Date(), 200, accessTokenResponse.getBody()));
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new DataResponse("False", "login failed", new Date(), 500, null));
            }
        } catch (Exception e){
            log.error("Error login", e);
            throw e;
        }
    }

    public ResponseEntity<DataResponse> updateToken(String refreshToken) {
        try{
            String uri = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            String requestBody = "grant_type=" + "refresh_token" +
                    "&client_id=" + clientId +
                    "&refresh_token=" + refreshToken;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<AccessTokenResponse> accessTokenResponse = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    request,
                    AccessTokenResponse.class);

            if (accessTokenResponse.getStatusCode().is2xxSuccessful()){
                return ResponseEntity.ok()
                        .body(new DataResponse("Success", "Success Update Token", new Date(), 200, accessTokenResponse.getBody()));
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new DataResponse("False", "Failed Update Token", new Date(), 500, null));
            }
        } catch (Exception e){
            log.error("Error update token", e);
            throw e;
        }
    }
}
