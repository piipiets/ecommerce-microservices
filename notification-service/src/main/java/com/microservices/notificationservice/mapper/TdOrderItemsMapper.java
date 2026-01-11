package com.microservices.notificationservice.mapper;

import com.microservices.notificationservice.model.dto.OrderItems;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TdOrderItemsMapper {
    List<OrderItems> getItemsByOrderNumber(String orderNumber);
}
