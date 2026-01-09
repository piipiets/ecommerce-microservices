package com.microservices.orderservice.exception;

import com.microservices.orderservice.model.dto.OutOfStockItemsDto;

import java.util.List;

public class OutOfStockException extends RuntimeException {

    private final List<OutOfStockItemsDto> items;

    public OutOfStockException(List<OutOfStockItemsDto> items) {
        super("Some products are out of stock");
        this.items = items;
    }

    public List<OutOfStockItemsDto> getItems() {
        return items;
    }
}
