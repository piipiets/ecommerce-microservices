package com.microservices.notificationservice.mapper;

import com.microservices.notificationservice.model.dto.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TdOrderMapper {
    Order getByOrderNumber(String orderNumber);
}
