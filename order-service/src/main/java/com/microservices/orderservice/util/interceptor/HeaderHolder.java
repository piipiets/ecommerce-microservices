package com.microservices.orderservice.util.interceptor;

import lombok.Data;

import java.util.Arrays;

@Data
public class HeaderHolder {
    private String name;
    private String givenName;
    private String email;
    private String remoteAddress;
    private String date;
    private String path;
    private String version;
    private String exp;
    private String[] roles;

    public boolean havingOneOfRoles(String[] neededRoles) {
        for (String role: neededRoles) {
            if ( Arrays.asList(this.roles).contains(role) ) {
                return true;
            }
        }
        return false;
    }
}
