package com.microservices.inventoryservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class InventoryResponseDto {
    private String skuCode;
    private int quantity;
    private BigDecimal price;
}
