package com.microservices.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {
    private String orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String orderDate;
    private List<OrderItems> orderItems;
}
