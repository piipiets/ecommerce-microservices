package com.microservices.orderservice.model.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DataAllResponse<T> {
    String result = "Success";
    String detail;
    Date date;
    int code = 200;
    List<T> data;
}