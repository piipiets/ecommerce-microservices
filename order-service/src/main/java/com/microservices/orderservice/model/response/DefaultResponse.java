package com.microservices.orderservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultResponse {
    String result = "Success";
    String detail;
    Date date;
    int code = 200;

    public DefaultResponse(String detail, int code){
        this.detail = detail;
        this.code = code;
        this.date = new Date();
    }
}
