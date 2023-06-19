package com.shivu.productservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shivu.productservice.dto.ProductRequest;
import com.shivu.productservice.dto.ProductResponse;
import com.shivu.productservice.model.Product;
import com.shivu.productservice.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepo;
 
    public void createProduct(ProductRequest productRequest){
        Product product = Product
                            .builder()
                            .name(productRequest.getName())
                            .description(productRequest.getDescription())
                            .price(productRequest.getPrice())
                            .build();

        productRepo.save(product);
        LOGGER.info("Product {} is saved !!", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll().stream().map(this::maptoProductResponse).collect(Collectors.toList());
    }

    private ProductResponse maptoProductResponse(Product product){
        return ProductResponse.builder()
                                .id(product.getId())
                                .description(product.getDescription())
                                .name(product.getName())
                                .price(product.getPrice())
                                .build();
    }

    public ProductResponse getProductById(String id) {
        Optional<Product> optionalProd = productRepo.findById(id);
        if(optionalProd.isPresent()){
            return maptoProductResponse(optionalProd.get());
        }
        return null;
    }
}
