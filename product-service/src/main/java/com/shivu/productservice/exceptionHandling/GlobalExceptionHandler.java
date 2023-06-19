package com.shivu.productservice.exceptionHandling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        APIError err = APIError
                            .builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .errors(details)
                            .message("Resource not found !!")
                            .timeStamp(LocalDateTime.now())
                            .build();
        return ResponseEntityBuilder.build(err);
    }

}
