package com.microservices.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockDto {
    private String skuCode;
    private Integer qty;
}
