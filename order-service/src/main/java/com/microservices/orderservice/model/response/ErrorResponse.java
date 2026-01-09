package com.microservices.orderservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse<T> {
    String result = "Error";
    String detail;
    String date;
    int code;
    List<T> errors;

    public ErrorResponse(String result, String detail, int code, List<T> errors){
        this.result = result;
        this.detail = detail;
        this.code = code;
        this.date = new Date()+"";
        this.errors = errors;
    }
}
