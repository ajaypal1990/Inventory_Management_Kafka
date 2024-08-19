package com.ecommerce.inventory_management.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.inventory_management.entity.Inventory;
import com.ecommerce.inventory_management.event.StockThresholdEvent;
import com.ecommerce.inventory_management.exception.ResourceNotFoundException;
import com.ecommerce.inventory_management.repository.InventoryRepository;
import com.ecommerce.inventory_management.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private KafkaTemplate<String, StockThresholdEvent> kafkaTemplate;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetInventoryByProductId_thenSuccess() {
        Inventory mockInventory = new Inventory();
        mockInventory.setId(1L);
        mockInventory.setQuantity(20);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(mockInventory));

        Inventory result = inventoryService.getInventoryByProductId(1L);
        assertEquals(20, result.getQuantity());
    }

    @Test
    void whenGetInventoryByProductId_thenResourceNotFoundException() {
        when(inventoryRepository.findByProductId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getInventoryByProductId(1L));
    }

    @Test
    void whenAddInventory_thenSuccess() {
        Inventory newInventory = new Inventory();
        newInventory.setQuantity(15);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(newInventory);

        Inventory savedInventory = inventoryService.addInventory(newInventory);
        assertEquals(15, savedInventory.getQuantity());
    }

    @Test
    void whenUpdateStock_thenSuccess() {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setQuantity(5);
        inventory.setThreshold(10);

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory updatedInventory = inventoryService.updateStock(1L, 5);
        verify(kafkaTemplate, times(1)).send(eq("stock-level"), any(StockThresholdEvent.class));
        assertEquals(5, updatedInventory.getQuantity());
    }
    @Test
    void whenIsProductAvailable_thenSuccess() {
        Inventory inventory = new Inventory();
        inventory.setQuantity(1);

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        assertTrue(inventoryService.isProductAvailable(1L));
    }

    @Test
    void whenIsProductAvailable_thenFalse() {
        Inventory inventory = new Inventory();
        inventory.setQuantity(0);

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        assertFalse(inventoryService.isProductAvailable(1L));
    }

}
