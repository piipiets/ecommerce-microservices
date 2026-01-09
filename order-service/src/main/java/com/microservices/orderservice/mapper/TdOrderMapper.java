package com.microservices.orderservice.mapper;

import com.microservices.orderservice.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TdOrderMapper {
    void insertOrder(Order order);
    String getOrderByOrderNumber(String orderNumber);
}
