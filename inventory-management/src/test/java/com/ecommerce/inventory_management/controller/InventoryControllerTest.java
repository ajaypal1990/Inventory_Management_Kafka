package com.ecommerce.inventory_management.controller;

import com.ecommerce.inventory_management.entity.Inventory;
import com.ecommerce.inventory_management.service.InventoryService;
import com.ecommerce.inventory_management.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(inventoryController).build();
    }

    @Test
    public void whenGetInventory_thenCorrect() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        when(inventoryService.getInventoryByProductId(1L)).thenReturn(inventory);

        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));
    }

    @Test
    public void whenAddInventory_thenCorrect() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        when(inventoryService.addInventory(any(Inventory.class))).thenReturn(inventory);

        mockMvc.perform(post("/api/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));
    }
    @Test
    public void whenUpdateStock_thenCorrect() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setProductId(1L);
        inventory.setQuantity(50);
        when(inventoryService.updateStock(1L, 50)).thenReturn(inventory);

        mockMvc.perform(put("/api/inventory/update")
                        .param("productId", "1")
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(50));
    }

    @Test
    public void whenNotifySupplier_thenNoContent() throws Exception {
        doNothing().when(supplierService).notifySupplier(1L);

        mockMvc.perform(post("/api/inventory/supplier/notify")
                        .param("productId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenIsProductAvailable_thenCorrect() throws Exception {
        when(inventoryService.isProductAvailable(1L)).thenReturn(true);

        mockMvc.perform(get("/api/inventory/available/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

}

