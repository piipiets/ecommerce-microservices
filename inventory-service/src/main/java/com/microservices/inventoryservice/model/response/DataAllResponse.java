package com.microservices.inventoryservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataAllResponse<T> {
    String result = "Success";
    String detail;
    Date date;
    int code = 200;
    List<T> data;
}
