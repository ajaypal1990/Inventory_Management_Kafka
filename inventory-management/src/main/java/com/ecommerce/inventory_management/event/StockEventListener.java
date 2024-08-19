package com.ecommerce.inventory_management.event;

import com.ecommerce.inventory_management.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockEventListener {
    @Autowired
    private SupplierService supplierService;

    @KafkaListener(topics = "stock-events", containerFactory = "kafkaListenerContainerFactory")
    public void onStockThresholdEvent(StockThresholdEvent event) {
        System.out.println("Received stock threshold event for product ID: " + event.getProductId());
        // Invoke the supplier service to notify the supplier
        supplierService.notifySupplier(event.getProductId());
    }
}


