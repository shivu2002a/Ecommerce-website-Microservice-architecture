package com.shivu.inventoryservice.event;
import java.util.List;

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
