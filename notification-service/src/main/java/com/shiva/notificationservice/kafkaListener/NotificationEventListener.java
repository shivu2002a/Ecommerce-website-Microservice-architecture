package com.shiva.notificationservice.kafkaListener;

import org.springframework.kafka.annotation.KafkaListener;

import com.shiva.notificationservice.event.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationEventListener {

    @KafkaListener
    public void handleNotificationEvent(OrderPlacedEvent orderPlacedEvent){
        // Send the email
        log.info("Recieved notification for order: {}", orderPlacedEvent.getOrderNumber());
    }
    
}
