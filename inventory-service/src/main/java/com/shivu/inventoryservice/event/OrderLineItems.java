package com.shivu.inventoryservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    private Long id;
    private String skuCode;
    private Float price; 
    private Integer quantity; 

}
