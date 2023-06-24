package com.shivu.inventoryservice.kafkaListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.shivu.inventoryservice.event.OrderPlacedEvent;
import com.shivu.inventoryservice.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderEventListener {

    @Autowired
    private InventoryService invService;

    @KafkaListener(topics = "notificationTopic")
    public void handleNotificationEvent(OrderPlacedEvent orderPlacedEvent) {
        log.info("Recieved notification for order: {}", orderPlacedEvent.getOrderNumber());
        //Update the products quantity
        invService.updateInventory(orderPlacedEvent);
    }

}
