package com.ecommerce.inventory_management.controller;

import com.ecommerce.inventory_management.entity.Inventory;
import com.ecommerce.inventory_management.service.InventoryService;
import com.ecommerce.inventory_management.service.SupplierService;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable Long productId) {
        logger.info("Fetching inventory for product ID: {}", productId);
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping("/add")
    public ResponseEntity<Inventory> addStock(@RequestBody Inventory inventory) {
        logger.info("Adding new stock for product ID: {}", inventory.getProductId());
        Inventory newInventory = inventoryService.addInventory(inventory);
        logger.info("Stock added successfully for product ID: {}", inventory.getProductId());
        return ResponseEntity.ok(newInventory);
    }

    @PutMapping("/update")
    public Inventory updateStock(@RequestParam Long productId, @RequestParam @Min(0) int quantity) {
        logger.info("Updating stock for product ID: {} with quantity {}", productId, quantity);
        return inventoryService.updateStock(productId, quantity);
    }

    @GetMapping("/below-threshold")
    public List<Inventory> getProductsBelowThreshold() {
        logger.info("Fetching products below threshold");
        return inventoryService.getProductsBelowThreshold();
    }

    @PostMapping("/supplier/notify")
    public void notifySupplier(@RequestParam Long productId) {
        logger.info("Notifying supplier for product ID: {}", productId);
        supplierService.notifySupplier(productId);
    }

    @GetMapping("/available/{productId}")
    public boolean isProductAvailable(@PathVariable Long productId) {
        logger.info("Checking availability for product ID: {}", productId);
        return inventoryService.isProductAvailable(productId);
    }
}
