package com.microservices.orderservice.service;

import com.microservices.orderservice.model.dto.InventoryResponse;
import com.microservices.orderservice.model.dto.UpdateStockDto;
import com.microservices.orderservice.model.response.DataAllResponse;
import com.microservices.orderservice.model.response.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GlobalService {

    private final RestTemplate restTemplate;

    public List<InventoryResponse> getProductsQuantity(List<String> skuCodeList, String auth) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", auth);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            URI uri = UriComponentsBuilder
                    .fromUriString("http://inventory-service/api/inventory/check-stock")
                    .queryParam("skuCodeList", skuCodeList)
                    .build(true)
                    .toUri();

            ResponseEntity<DataAllResponse<InventoryResponse>> res = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<DataAllResponse<InventoryResponse>>() {}
            );

            return res.getBody() != null ? res.getBody().getData() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error when accessing inventory service", e);
            throw e;
        }
    }

    public DefaultResponse updateProductStock(List<UpdateStockDto> updateStockDtos, String auth) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", auth);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<UpdateStockDto>> entity = new HttpEntity<>(updateStockDtos, headers);

            URI uri = UriComponentsBuilder
                    .fromUriString("http://inventory-service/api/inventory/update-stock")
                    .build(true)
                    .toUri();

            ResponseEntity<DefaultResponse> res = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    entity,
                    new ParameterizedTypeReference<DefaultResponse>() {}
            );

            if (res.getBody() != null && res.getBody().getCode() == 200) {
                return res.getBody();
            }
            return null;
        } catch (Exception e) {
            log.error("Error when accessing update stock in inventory service", e);
            return null;
        }
    }
}
