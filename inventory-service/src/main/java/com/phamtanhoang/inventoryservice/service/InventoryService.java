package com.phamtanhoang.inventoryservice.service;


import com.phamtanhoang.inventoryservice.dto.InventoryResponse;
import com.phamtanhoang.inventoryservice.model.Inventory;
import com.phamtanhoang.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public List<InventoryResponse> isInStock(List<String> skuCode) {
    return inventoryRepository.findBySkuCodeIn(skuCode).stream()
        .map(inventory ->
            InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build()
        ).toList();
  }
}
