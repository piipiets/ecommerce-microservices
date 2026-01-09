package com.microservices.orderservice.controller;

import com.microservices.orderservice.exception.UnauthorizedException;
import com.microservices.orderservice.model.entity.Order;
import com.microservices.orderservice.model.response.DefaultResponse;
import com.microservices.orderservice.model.response.ResponseMessage;
import com.microservices.orderservice.service.OrderService;
import com.microservices.orderservice.util.interceptor.HeaderHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final HeaderHolder headerHolder;

    @PostMapping("/place-order")
    public ResponseEntity<DefaultResponse> placeOrder(
            @RequestHeader(value = "Authorization", defaultValue = "") String authorizationHeader,
            @RequestBody Order order) {
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return orderService.placeOrder(order, authorizationHeader);
    }
}
