package com.microservices.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class InventoryRequest {
    @NotEmpty(message = "Sku code must be filled")
    private String skuCode;

    @NotEmpty(message = "Name must be filled")
    private String productName;

    @NotEmpty(message = "Description must be filled")
    private String description;

    @NotNull(message = "Price must be filled")
    private BigDecimal price;

    @NotNull(message = "Quantity must be filled")
    private int quantity;
}
