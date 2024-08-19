package com.ecommerce.inventory_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product ID cannot be null")
    @Column(name = "product_id")
    private Long productId;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(name = "quantity")
    private int quantity;

    @Min(value = 0, message = "Threshold cannot be negative")
    @Column(name = "threshold")
    private int threshold;

}

