package com.microservices.orderservice.mapper;

import com.microservices.orderservice.model.entity.OrderItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TdOrderItemsMapper {
    void insertOrderLineItems(@Param("itemList") List<OrderItems> orderItemsList);
}
