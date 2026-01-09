package com.microservices.inventoryservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    String result = "Error";
    String detail;
    String date;
    int code;
    List<String> errors;

    public ErrorResponse(String result, String detail, int code, List<String> errors){
        this.result = result;
        this.detail = detail;
        this.code = code;
        this.date = new Date()+"";
        this.errors = errors;
    }
}
