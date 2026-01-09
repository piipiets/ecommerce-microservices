package com.microservices.inventoryservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class DataResponse<T> {
    String result;
    String detail;
    Date date;
    int code;
    T data;
}