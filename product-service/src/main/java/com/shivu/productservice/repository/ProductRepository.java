package com.shivu.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shivu.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
}
