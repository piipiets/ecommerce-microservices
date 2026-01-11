package com.microservices.orderservice.service;

import com.microservices.orderservice.exception.OutOfStockException;
import com.microservices.orderservice.mapper.TdOrderMapper;
import com.microservices.orderservice.mapper.TdOrderItemsMapper;
import com.microservices.orderservice.model.dto.*;
import com.microservices.orderservice.model.entity.Order;
import com.microservices.orderservice.model.entity.OrderItems;
import com.microservices.orderservice.model.response.DefaultResponse;
import com.microservices.orderservice.model.response.ResponseMessage;
import com.microservices.orderservice.util.AppUtil;
import com.microservices.orderservice.util.interceptor.HeaderHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final TdOrderMapper tdOrderMapper;
    private final TdOrderItemsMapper tdOrderItemsMapper;
    private final PlatformTransactionManager transactionManager;
    private final GlobalService globalService;
    private final KafkaService kafkaService;
    private final HeaderHolder headerHolder;

    public ResponseEntity<DefaultResponse> placeOrder(Order order, String auth) {
        TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        List<UpdateStockDto> oldStockList = new ArrayList<>();
        try {
            String orderNum = tdOrderMapper.getOrderByOrderNumber(order.getOrderNumber());
            if (orderNum != null) {
                throw new DuplicateKeyException("Order number " + order.getOrderNumber() + " already exist");
            }

            List<String> skuCodeList = order.getOrderItems().stream().map(OrderItems::getSkuCode).collect(Collectors.toList());
            List<InventoryResponse> inventoryResponses = globalService.getProductsQuantity(skuCodeList, auth);

            Map<String, InventoryInfoDto> inventoryMap = inventoryResponses.stream()
                    .collect(Collectors.toMap(
                            InventoryResponse::getSkuCode,
                            res -> new InventoryInfoDto(res.getQuantity(), res.getPrice())
                    ));

            validateStock(order, inventoryMap);
            tdOrderMapper.insertOrder(order);

            List<UpdateStockDto> newStockList = new ArrayList<>();
            order.getOrderItems().forEach(item -> {
                InventoryInfoDto inventory = inventoryMap.get(item.getSkuCode());

                item.setOrderNumber(order.getOrderNumber());
                item.setPrice(inventory.getPrice());

                UpdateStockDto newStock = new UpdateStockDto();
                newStock.setSkuCode(item.getSkuCode());
                newStock.setQty(inventory.getQuantity() - item.getQuantity());
                newStockList.add(newStock);

                //old stock to rollback if failed
                UpdateStockDto oldStock = new UpdateStockDto();
                oldStock.setSkuCode(item.getSkuCode());
                oldStock.setQty(inventory.getQuantity());
                oldStockList.add(oldStock);
            });

            AppUtil.splitList(order.getOrderItems(), 10)
                    .forEach(tdOrderItemsMapper::insertOrderLineItems);

            //update stock
            DefaultResponse res = globalService.updateProductStock(newStockList, auth);
            if (res == null) {
                throw new IllegalArgumentException("Failed update product stock");
            }

            transactionManager.commit(txStatus);
            kafkaService.sendKafka("order-placed", new OrderNotificationDto(order.getOrderNumber(), headerHolder.getEmail()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new DefaultResponse(ResponseMessage.DATA_CREATED, 201));
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            globalService.updateProductStock(oldStockList, auth);
            log.error("Error when place order", e);
            throw e;
        }
    }

    public void validateStock(Order orderRequest, Map<String, InventoryInfoDto> inventoryMap) {
        try {

            List<OutOfStockItemsDto> outOfStockItems = orderRequest.getOrderItems().stream()
                    .map(item -> {
                        InventoryInfoDto inventory = inventoryMap.get(item.getSkuCode());
                        Integer available = (inventory == null || inventory.getQuantity() == null) ? 0 : inventory.getQuantity();
                        if (available < item.getQuantity()) {
                            return new OutOfStockItemsDto(
                                    item.getSkuCode(),
                                    item.getQuantity(),
                                    available
                            );
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!outOfStockItems.isEmpty()) {
                throw new OutOfStockException(outOfStockItems);
            }
        } catch (Exception e) {
            log.error("Error when validate stock", e);
            throw e;
        }
    }
}
