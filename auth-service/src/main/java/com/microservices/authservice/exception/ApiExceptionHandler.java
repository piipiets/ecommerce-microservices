package com.microservices.authservice.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.authservice.model.DefaultResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {
    private static final String ERROR = "Error";

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<DefaultResponse> generalException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        DefaultResponse response = new DefaultResponse(ERROR, status.getReasonPhrase(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<DefaultResponse> handleDataNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        DefaultResponse response = new DefaultResponse(ERROR, e.getMessage(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<DefaultResponse> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        DefaultResponse response = new DefaultResponse(ERROR, e.getMessage(), new Date(), status.value());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<DefaultResponse> handleHttpClientError(HttpClientErrorException e) {
        HttpStatus status = e.getStatusCode();
        String detail = extractErrorMessage(e);
        DefaultResponse response = new DefaultResponse(ERROR, detail, new Date(), status.value());
        return new ResponseEntity<>(response, status);
    }

    private String extractErrorMessage(HttpClientErrorException e) {
        try {
            String body = e.getResponseBodyAsString();
            System.out.println(body);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(body);
            if (node.has("error_description")) {
                return node.get("error_description").asText();
            }
            return body;
        } catch (Exception ex) {
            return e.getStatusText();
        }
    }

}