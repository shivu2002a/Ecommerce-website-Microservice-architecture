package com.shivu.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "product")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product { 

    @Id
    private String id;
    private String name;
    private String description;
    private Float price; 

}
