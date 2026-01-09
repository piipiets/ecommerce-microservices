package com.microservices.inventoryservice.controller;

import com.microservices.inventoryservice.exception.UnauthorizedException;
import com.microservices.inventoryservice.model.dto.InventoryResponseDto;
import com.microservices.inventoryservice.model.dto.InventoryUpdateRequest;
import com.microservices.inventoryservice.model.dto.InventoryRequest;
import com.microservices.inventoryservice.model.dto.UpdateStockDto;
import com.microservices.inventoryservice.model.entity.Inventory;
import com.microservices.inventoryservice.model.response.DataAllResponse;
import com.microservices.inventoryservice.model.response.DataResponse;
import com.microservices.inventoryservice.model.response.DefaultResponse;
import com.microservices.inventoryservice.model.response.ResponseMessage;
import com.microservices.inventoryservice.service.InventoryService;
import com.microservices.inventoryservice.util.interceptor.HeaderHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final HeaderHolder headerHolder;

    @GetMapping(
            value = "/check-stock",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DataAllResponse<InventoryResponseDto>> isInStock(
            @RequestParam List<String> skuCodeList
    ){
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return inventoryService.isInStock(skuCodeList);
    }

    @PutMapping(
            value = "/update-stock",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DefaultResponse> updateStock(
            @RequestBody List<UpdateStockDto> request
    ){
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(inventoryService.updateStock(request));
    }

    @PostMapping(path = "/add")
    public ResponseEntity<DefaultResponse> createProduct(
            @Valid @RequestBody InventoryRequest inventoryRequest){
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(inventoryService.createProduct(inventoryRequest));
    }

    @GetMapping(path = "/get")
    public ResponseEntity<DataResponse<Inventory>> getProductBySku(
            @RequestParam String skuCode) {
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(inventoryService.getProductBySKu(skuCode));
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<DefaultResponse> updateProduct(
            @RequestParam String skuCode,
            @RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(inventoryService.updateProduct(skuCode, inventoryUpdateRequest));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<DefaultResponse> deleteProduct(
            @RequestParam String skuCode) {
        if (headerHolder.getEmail() == null || headerHolder.getEmail().isEmpty()) {
            throw new UnauthorizedException(ResponseMessage.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(inventoryService.deleteProduct(skuCode));
    }
}
