package com.shivu.orderservice.event;

import java.util.List;

import com.shivu.orderservice.model.OrderLineItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {

    private String email_id;
    private String orderNumber;
    private double totalPrice;
    private List<OrderLineItems> orderLineItemsList;

}
