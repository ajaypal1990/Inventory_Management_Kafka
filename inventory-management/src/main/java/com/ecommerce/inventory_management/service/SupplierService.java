package com.ecommerce.inventory_management.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SupplierService {
    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

    public void notifySupplier(Long productId) {
        // Logic to notify supplier, e.g., send an email or message
        logger.info("Notified supplier for product ID: {}", productId);
    }
}
