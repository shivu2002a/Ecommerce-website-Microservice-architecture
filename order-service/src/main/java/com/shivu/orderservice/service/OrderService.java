package com.shivu.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.shivu.orderservice.dto.InventoryResponse;
import com.shivu.orderservice.dto.OrderLineItemsDTO;
import com.shivu.orderservice.dto.OrderRequest;
import com.shivu.orderservice.model.Order;
import com.shivu.orderservice.model.OrderLineItems;
import com.shivu.orderservice.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    OrderRepository orderRepo;

    @Autowired
    WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrdernumber(UUID.randomUUID().toString());
        List<OrderLineItems> olilist = orderRequest
                                        .getOrderLineItemsDtoList()
                                        .stream()
                                        .map(this::mapToOrderLineItem)
                                        .collect(Collectors.toList());
        order.setOrderLineItemsList(olilist);   

        //Call inventory service to check if items are in stock. Only then place the order
        List<String> skuCodes = olilist
                                    .stream()
                                    .map(OrderLineItems::getSkuCode)
                                    .collect(Collectors.toList());
        System.out.println(skuCodes);
        InventoryResponse[] inventoryResponsesArray = webClient
                        .get()
                        .uri("http://localhost:8083/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCodeList", skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

        boolean allInStock = Arrays
                            .stream(inventoryResponsesArray)
                            .allMatch(InventoryResponse::isInStock);

        if(allInStock){
            orderRepo.save(order); 
        } else {
            throw new IllegalArgumentException("Product is not in stock !!");
        }
    }
    

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDTO olidto) {
        OrderLineItems oli = new OrderLineItems();
        oli.setPrice(olidto.getPrice());
        oli.setQuantity(olidto.getQuantity());
        oli.setSkuCode(olidto.getSkuCode());
        return oli;
    }
}
