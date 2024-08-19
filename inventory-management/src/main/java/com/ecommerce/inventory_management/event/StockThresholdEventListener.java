package com.ecommerce.inventory_management.event;

import com.ecommerce.inventory_management.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StockThresholdEventListener {
    private static final Logger logger = LoggerFactory.getLogger(StockThresholdEventListener.class);

    @Autowired
    private SupplierService supplierService;

    @EventListener
    public void handleStockThresholdEvent(StockThresholdEvent event) {
        supplierService.notifySupplier(event.getProductId());
        logger.info("Handled stock threshold event for product ID: {}", event.getProductId());
    }
}
