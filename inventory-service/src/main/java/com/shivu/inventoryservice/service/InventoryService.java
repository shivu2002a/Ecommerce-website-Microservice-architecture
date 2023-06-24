package com.shivu.inventoryservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivu.inventoryservice.dto.InventoryResponse;
import com.shivu.inventoryservice.event.OrderLineItems;
import com.shivu.inventoryservice.event.OrderPlacedEvent;
import com.shivu.inventoryservice.model.Inventory;
import com.shivu.inventoryservice.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {
    
    @Autowired
    InventoryRepository invRepo;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode, List<Integer> quantityList){
        List<Inventory> isInStock = invRepo.findBySkuCodeIn(skuCode);
        log.info("Skucode list -> {}", skuCode);        
        log.info("Quantity list -> {}", quantityList);

        log.info("IsInStock list -> {}", isInStock);
        List<InventoryResponse> inventoryRespList = new ArrayList<>();
        for (int i = 0; i < isInStock.size(); i++) {
            inventoryRespList.add(mapToInventoryResponse(isInStock.get(i), quantityList.get(i)));

        }
        return inventoryRespList;
    }

    public InventoryResponse mapToInventoryResponse(Inventory inv, Integer quantity){
        return InventoryResponse
            .builder()
            .skuCode(inv.getSkuCode())
            .isInStock(inv.getQuantity() > 0 && inv.getQuantity() >= quantity)
            .build();
    }

    public void updateInventory(OrderPlacedEvent orderPlacedEvent) {
        List<OrderLineItems> orderLineItemsList = orderPlacedEvent.getOrderLineItemsList();
        orderLineItemsList.forEach(p -> updateItem(p));
    }

    private void updateItem(OrderLineItems item) {
        Inventory repoItem = invRepo.findBySkuCode(item.getSkuCode()).get();
        repoItem.setQuantity(repoItem.getQuantity() - item.getQuantity());
        invRepo.save(repoItem);
    }

    
}
