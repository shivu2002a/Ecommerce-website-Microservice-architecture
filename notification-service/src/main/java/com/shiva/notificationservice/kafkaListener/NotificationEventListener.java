package com.shiva.notificationservice.kafkaListener;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Component;

import com.shiva.notificationservice.event.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificationEventListener {

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotificationEvent(OrderPlacedEvent orderPlacedEvent) {
        // Send the email
        log.info("Recieved notification for order: {}", orderPlacedEvent.getOrderNumber());
    }

}
