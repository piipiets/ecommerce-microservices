package com.microservices.inventoryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.inventoryservice.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        int status = HttpServletResponse.SC_UNAUTHORIZED;

        String message = resolveMessage(authException);

        ErrorResponse body = new ErrorResponse(
                "Unauthorized",
                message,
                status,
                null
        );

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        OBJECT_MAPPER.writeValue(response.getWriter(), body);
    }

    private String resolveMessage(AuthenticationException ex) {

        if (ex instanceof InsufficientAuthenticationException) {
            return "Missing or invalid Authorization header";
        }

        if (ex instanceof BadCredentialsException) {
            return "Invalid authentication credentials";
        }

        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("expired")) {
            return "Authentication token has expired";
        }

        return "Unauthorized";
    }
}
