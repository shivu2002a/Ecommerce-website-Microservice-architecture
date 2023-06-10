package com.shivu.orderservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_order_line_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    @Id
    // AUTO_INCREMENT PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String skuCode;
    private Float price; 
    private Integer quantity; 

}
