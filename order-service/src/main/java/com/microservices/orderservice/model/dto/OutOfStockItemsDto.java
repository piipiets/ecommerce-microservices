package com.microservices.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutOfStockItemsDto {
    private String skuCode;
    private Integer requested;
    private Integer available;
}
