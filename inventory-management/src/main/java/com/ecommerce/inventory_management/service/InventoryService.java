package com.ecommerce.inventory_management.service;

import com.ecommerce.inventory_management.entity.Inventory;
import com.ecommerce.inventory_management.event.StockThresholdEvent;
import com.ecommerce.inventory_management.exception.ResourceNotFoundException;
import com.ecommerce.inventory_management.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private KafkaTemplate<String, StockThresholdEvent> kafkaTemplate;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Cacheable(value = "inventoryCache", key = "#productId")
    public Inventory getInventoryByProductId(Long productId) {
        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
        return inventory.orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public Inventory updateStock(Long productId, int quantity) {
        Inventory inventory = getInventoryByProductId(productId);
        inventory.setQuantity(quantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);

        if (updatedInventory.getQuantity() <= updatedInventory.getThreshold()) {
            kafkaTemplate.send("stock-level", new StockThresholdEvent(this, productId));
        }
        return updatedInventory;
    }

    public List<Inventory> getProductsBelowThreshold() {
        return inventoryRepository.findByQuantityLessThan(10);  // Assuming 10 is the threshold
    }

    @Cacheable(value = "productAvailability", key = "#productId")
    public boolean isProductAvailable(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product ID: " + productId));
        return inventory.getQuantity() > 0;
    }
}

