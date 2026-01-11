package com.microservices.orderservice.service;

import com.microservices.orderservice.model.dto.OrderNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafka(String topic, OrderNotificationDto message) {
        kafkaTemplate.send(topic, message)
                .addCallback(
                        success -> log.info("Send to Kafka topic {} with orderNumber {} succeeded", topic, message.getOrderNumber()),
                        failure -> log.error("Send to Kafka topic {} with orderNumber {} failed", topic, message.getOrderNumber(), failure)
                );
    }
}
