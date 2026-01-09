package com.microservices.inventoryservice.service;

import com.microservices.inventoryservice.exception.NotFoundException;
import com.microservices.inventoryservice.mapper.TdInventoryMapper;
import com.microservices.inventoryservice.model.dto.InventoryResponseDto;
import com.microservices.inventoryservice.model.dto.InventoryRequest;
import com.microservices.inventoryservice.model.dto.InventoryUpdateRequest;
import com.microservices.inventoryservice.model.dto.UpdateStockDto;
import com.microservices.inventoryservice.model.entity.Inventory;
import com.microservices.inventoryservice.model.response.DataAllResponse;
import com.microservices.inventoryservice.model.response.DataResponse;
import com.microservices.inventoryservice.model.response.DefaultResponse;
import com.microservices.inventoryservice.model.response.ResponseMessage;
import com.microservices.inventoryservice.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final TdInventoryMapper tdInventoryMapper;
    private final PlatformTransactionManager transactionManager;

    public ResponseEntity<DataAllResponse<InventoryResponseDto>> isInStock(List<String> skuCodeList) {
        try {
            List<InventoryResponseDto> res = tdInventoryMapper.getListProductBySku(skuCodeList).stream().map(
                    inventory ->
                            InventoryResponseDto.builder()
                                    .skuCode(inventory.getSkuCode())
                                    .quantity(inventory.getQuantity())
                                    .price(inventory.getPrice())
                                    .build()
            ).collect(Collectors.toList());

            return ResponseEntity.ok().body(new DataAllResponse<>(
                    "Success",
                    ResponseMessage.DATA_FETCHED,
                    new Date(),
                    200,
                    res));

        } catch (Exception e) {
            log.error("Error when get product stock", e);
            throw e;
        }
    }

    public DefaultResponse createProduct(InventoryRequest inventoryRequest) {
        try {
            Inventory product = tdInventoryMapper.getProductBySku(inventoryRequest.getSkuCode());
            if (product != null) {
                throw new DuplicateKeyException("Product with SKU " + inventoryRequest.getSkuCode() + " already exist");
            }
            tdInventoryMapper.saveProduct(inventoryRequest);

            return new DefaultResponse(ResponseMessage.DATA_CREATED, 200);
        } catch (Exception e) {
            log.error("Error when creating product", e);
            throw e;
        }
    }

    public DefaultResponse deleteProduct(String skuCode) {
        try {
            Inventory product = tdInventoryMapper.getProductBySku(skuCode);
            if (product == null) {
                throw new NotFoundException(ResponseMessage.DATA_NOT_FOUND);
            }
            tdInventoryMapper.deleteProduct(skuCode);

            return new DefaultResponse(ResponseMessage.DATA_DELETED, 200);
        } catch (Exception e) {
            log.error("Error when delete product", e);
            throw e;
        }
    }

    public DefaultResponse updateProduct(String skuCode, InventoryUpdateRequest request) {
        try {
            Inventory product = tdInventoryMapper.getProductBySku(skuCode);
            if (product == null) {
                throw new NotFoundException(ResponseMessage.DATA_NOT_FOUND);
            }

            tdInventoryMapper.updateProduct(skuCode, request);
            return new DefaultResponse(ResponseMessage.DATA_UPDATED, 200);
        } catch (Exception e) {
            log.error("Error when updating product", e);
            throw e;
        }
    }

    public DataResponse<Inventory> getProductBySKu(String skuCode) {
        try {
            Inventory product = tdInventoryMapper.getProductBySku(skuCode);
            return new DataResponse<>("Success", ResponseMessage.DATA_FETCHED, new Date(), 200, product);
        } catch (Exception e) {
            log.error("Error when get product's data", e);
            throw e;
        }
    }

    public DefaultResponse updateStock(List<UpdateStockDto> request) {
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            AppUtil.splitList(request, 10)
                    .forEach(tdInventoryMapper::updateProductStock);

            transactionManager.commit(txStatus);
            return new DefaultResponse(ResponseMessage.DATA_UPDATED, 200);
        } catch (Exception e) {
            log.error("Error when update stock", e);
            transactionManager.rollback(txStatus);
            throw e;
        }
    }
}
