package com.microservices.inventoryservice.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        errorAttributes.put("result", "Error");
        errorAttributes.put("detail", errorAttributes.get("error"));
        errorAttributes.put("path", errorAttributes.get("path"));
        errorAttributes.put("date", new Date());
        errorAttributes.put("code", errorAttributes.get("status"));
        errorAttributes.remove("status");
        errorAttributes.remove("message");
        errorAttributes.remove("locale");
        errorAttributes.remove("error");
        errorAttributes.remove("cause");
        errorAttributes.remove("trace");
        errorAttributes.remove("timestamp");

        return errorAttributes;
    }
}
