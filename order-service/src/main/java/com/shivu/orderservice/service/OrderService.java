package com.shivu.orderservice.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrdernumber(UUID.randomUUID().toString());
        List<OrderLineItems> olilist = orderRequest
                                        .getOrderLineItemsDtoList()
                                        .stream()
                                        .map(this::mapToOrderLineItem)
                                        .collect(Collectors.toList());
        order.setOrderLineItemsList(olilist);   
        orderRepo.save(order); 
    }
    

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDTO olidto) {
        OrderLineItems oli = new OrderLineItems();
        oli.setPrice(olidto.getPrice());
        oli.setQuantity(olidto.getQuantity());
        oli.setSkuCode(olidto.getSkuCode());
        return oli;
    }
}
