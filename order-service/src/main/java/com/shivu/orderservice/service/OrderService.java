package com.shivu.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.shivu.orderservice.dto.InventoryResponse;
import com.shivu.orderservice.dto.OrderLineItemsDTO;
import com.shivu.orderservice.dto.OrderRequest;
import com.shivu.orderservice.event.OrderPlacedEvent;
import com.shivu.orderservice.model.Order;
import com.shivu.orderservice.model.OrderLineItems;
import com.shivu.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    @Autowired
    OrderRepository orderRepo;

    @Autowired
    WebClient.Builder webClientBuilder;

    private final Tracer tracer;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private final String NOTIFICATION_TOPIC = "notificationTopic"; 

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrdernumber(UUID.randomUUID().toString());
        List<OrderLineItems> olilist = orderRequest
                                        .getOrderLineItemsDtoList()
                                        .stream()
                                        .map(this::mapToOrderLineItem)
                                        .collect(Collectors.toList());
        order.setOrderLineItemsList(olilist);
        order.setEmail_id(orderRequest.getEmail_id());  
        List<Integer> itemsQuantities = orderRequest
                                        .getOrderLineItemsDtoList()
                                        .stream()
                                        .map(item -> item.getQuantity())
                                        .collect(Collectors.toList());
        //Call inventory service to check if items are in stock. Only then, place the order
        List<String> skuCodes = olilist
                                    .stream()
                                    .map(OrderLineItems::getSkuCode)
                                    .collect(Collectors.toList());
        
        //  Build the traceId
        Span inventoryServiceRequestTrace = tracer.nextSpan().name("InventoryServiceRequest");
        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceRequestTrace.start())) {
            InventoryResponse[] inventoryResponsesArray = webClientBuilder.build()
                        .get()
                        .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder
                            .queryParam("skuCodeList", skuCodes)
                            .queryParam("quantityList", itemsQuantities)
                            .build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

            boolean allInStock = Arrays
                            .stream(inventoryResponsesArray)
                            .allMatch(InventoryResponse::isInStock);

            if(allInStock){
                Order savedOrder = orderRepo.save(order); 
                sendNotification(orderRequest, savedOrder);
                return "Order placed successfully !!";
            } else {
                throw new IllegalArgumentException("Product is not in stock !!");
            }
        } finally {
            inventoryServiceRequestTrace.end();
        }
        
        // InventoryResponse[] inventoryResponsesArray = webClientBuilder.build()
        //                 .get()
        //                 .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder
        //                                                 .queryParam("skuCodeList", skuCodes)
        //                                                 .queryParam("quantityList", itemsQuantities)
        //                                                 .build() 
        //                 )
        //                 .retrieve()
        //                 .bodyToMono(InventoryResponse[].class)
        //                 .block();

        // boolean allInStock = Arrays
        //                     .stream(inventoryResponsesArray)
        //                     .allMatch(InventoryResponse::isInStock);

        // if(allInStock){
        //     orderRepo.save(order); 
        //     return "Order placed successfully !!";
        // } else {
        //     throw new IllegalArgumentException("Product is not in stock !!");
        // }
    }
    

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDTO olidto) {
        OrderLineItems oli = new OrderLineItems();
        oli.setPrice(olidto.getPrice());
        oli.setQuantity(olidto.getQuantity());
        oli.setSkuCode(olidto.getSkuCode());
        return oli;
    }

    private void sendNotification(OrderRequest orderRequest, Order savedOrder){
        // Publish an event for notification
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setEmail_id(orderRequest.getEmail_id());    
        orderPlacedEvent.setOrderLineItemsList(savedOrder.getOrderLineItemsList());   
        orderPlacedEvent.setOrderNumber(savedOrder.getOrdernumber());        
        orderPlacedEvent.setEmail_id(savedOrder.getEmail_id());
        orderPlacedEvent.setTotalPrice(orderRequest
                                    .getOrderLineItemsDtoList()
                                    .stream()
                                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                                    .sum());
        kafkaTemplate.send(NOTIFICATION_TOPIC, orderPlacedEvent);
        log.info("Published a message for event - Order Placed with id : {}", orderPlacedEvent.getOrderNumber());
    }
}
