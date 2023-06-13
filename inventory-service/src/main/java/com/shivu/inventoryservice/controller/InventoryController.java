package com.shivu.inventoryservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shivu.inventoryservice.dto.InventoryResponse;
import com.shivu.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    // http://localhost:8083/api/inventory?skuCodeList=Samsung_M21,MotoRola_G3&quantityList=2,1
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodeList, @RequestParam List<Integer> quantityList) {
        return inventoryService.isInStock(skuCodeList, quantityList);
    }

}
