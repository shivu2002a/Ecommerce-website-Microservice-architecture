package com.shivu.productservice.exceptionHandling;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    
    public static ResponseEntity<Object> build(APIError apiError){
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
