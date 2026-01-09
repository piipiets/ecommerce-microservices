package com.microservices.inventoryservice.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Inventory {
    private String skuCode;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
}
