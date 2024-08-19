package com.ecommerce.inventory_management.repository;


import com.ecommerce.inventory_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
