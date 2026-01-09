package com.microservices.inventoryservice.mapper;

import com.microservices.inventoryservice.model.dto.InventoryRequest;
import com.microservices.inventoryservice.model.dto.InventoryUpdateRequest;
import com.microservices.inventoryservice.model.dto.UpdateStockDto;
import com.microservices.inventoryservice.model.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TdInventoryMapper {
    List<Inventory> getListProductBySku(@Param("skuCode") List<String> skuCode);
    Inventory getProductBySku(String skuCode);
    void saveProduct(InventoryRequest inventoryRequest);
    void deleteProduct(String skuCode);
    void updateProduct(@Param("skuCode") String skuCode, @Param("request") InventoryUpdateRequest request);
    void updateProductStock(@Param("items") List<UpdateStockDto> items);
}
