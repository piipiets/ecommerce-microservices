package com.microservices.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InventoryUpdateRequest {
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
}
