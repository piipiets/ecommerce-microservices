package com.microservices.notificationservice.consumer;

import lombok.extern.slf4j.Slf4j;
import com.microservices.notificationservice.model.dto.OrderNotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import com.microservices.notificationservice.service.EmailService;

@Service
@Slf4j
public class KafkaConsumer {

    private final EmailService emailService;

    public KafkaConsumer(EmailService emailService){
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group.id}")
    public void consume(OrderNotificationDto message, Acknowledgment ack) {
        try{
            log.info("Received Order Number : {}" , message.getOrderNumber());
            emailService.sendEmail(message);
            ack.acknowledge();
        } catch (Exception e){
            log.error("Failed to process order");
        }
    }
}