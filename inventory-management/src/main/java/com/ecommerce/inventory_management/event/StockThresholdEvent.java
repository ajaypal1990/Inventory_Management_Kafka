package com.ecommerce.inventory_management.event;

import org.springframework.context.ApplicationEvent;

public class StockThresholdEvent extends ApplicationEvent {
    private final Long productId;

    public StockThresholdEvent(Object source, Long productId) {
        super(source);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
