package com.microservices.notificationservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItems {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    private String productName;
}
