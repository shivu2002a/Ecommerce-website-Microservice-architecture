package com.shivu.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shivu.productservice.dto.ProductRequest;
import com.shivu.productservice.dto.ProductResponse;
import com.shivu.productservice.exceptionHandling.ResourceNotFoundException;
import com.shivu.productservice.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productservice;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productservice.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") String id) {
        ProductResponse prod = productservice.getProductById(id);
        if(prod != null) {
            return new ResponseEntity<ProductResponse>(prod, HttpStatus.FOUND); 
        } else {
            throw new ResourceNotFoundException("Product not found: " + id); 
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
        productservice.createProduct(productRequest);
    }

    @PutMapping
    public void updateProduct(@RequestBody ProductRequest productRequest ){

    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") String id){
        
    }   

}
