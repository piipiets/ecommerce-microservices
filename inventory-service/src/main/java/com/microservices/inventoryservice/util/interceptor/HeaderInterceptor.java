package com.microservices.inventoryservice.util.interceptor;

import com.microservices.inventoryservice.util.DateHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HeaderInterceptor implements HandlerInterceptor {
    private final HeaderHolder headerHolder;

    private static final Logger log = LogManager.getLogger(HeaderInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        Map<String, String> tokenValue = JwtUtil.getValueFromToken(token, new String[]{"name", "given_name", "preferred_username", "role", "email"});
        String name = tokenValue.getOrDefault("name","");
        String email = tokenValue.getOrDefault("email","");
        String givenName = tokenValue.getOrDefault("given_name","");
        String remoteAddress = request.getRemoteAddr() == null ? "127.0.0.1": request.getRemoteAddr();
        String role = tokenValue.getOrDefault("role", "");
        String date = String.valueOf(new DateHelper().getCurrentDate());
        String endPointPath = request.getRequestURI() == null ? "" : request.getRequestURI();
        String param = request.getQueryString() == null ? "" : request.getRequestURI();

        headerHolder.setName(name);
        headerHolder.setEmail(email);
        headerHolder.setGivenName(givenName);
        headerHolder.setRemoteAddress(remoteAddress);
        headerHolder.setRoles(role.split(","));
        headerHolder.setPath(endPointPath);
        headerHolder.setDate(date);

        Package pkg = getClass().getPackage();
        String packageName = pkg.getName();
        log.info("App: {}, Date: {}, From: {}, nama: {}, Path: {}, Data: {}",
                packageName, date, remoteAddress, name, endPointPath, param);
        return true;
    }

    public HeaderInterceptor(HeaderHolder headerHolder) {
        this.headerHolder = headerHolder;
    }
}
