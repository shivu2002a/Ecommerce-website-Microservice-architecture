package com.shivu.inventoryservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivu.inventoryservice.model.Inventory;
import com.shivu.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {
    
    @Autowired
    InventoryRepository invRepo;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        Optional<Inventory> optionalStock = invRepo.findBySkuCode(skuCode);
        return optionalStock.isPresent();
    }
}
