package com.shivu.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDTO {
    
    private Long id ;
    private String skuCode;
    private Float price; 
    private Integer quantity; 
}
