package com.shivu.inventoryservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivu.inventoryservice.dto.InventoryResponse;
import com.shivu.inventoryservice.model.Inventory;
import com.shivu.inventoryservice.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {
    
    @Autowired
    InventoryRepository invRepo;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        List<Inventory> isInStock = invRepo.findBySkuCodeIn(skuCode);
        log.info("Skucode list -> {}", skuCode);
        log.info("IsInStock list -> {}", isInStock);
        List<InventoryResponse> inventoryRespList = isInStock
            .stream()
            .map(this::mapToInventoryResponse)
            .collect(Collectors.toList());
        return inventoryRespList;
    }

    public InventoryResponse mapToInventoryResponse(Inventory inv){
        return InventoryResponse
            .builder()
            .skuCode(inv.getSkuCode())
            .isInStock(inv.getQuantity() > 0)
            .build();
    }
}
