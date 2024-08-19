package com.ecommerce.inventory_management.repository;


import com.ecommerce.inventory_management.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);
    List<Inventory> findByQuantityLessThan(int threshold);
}
