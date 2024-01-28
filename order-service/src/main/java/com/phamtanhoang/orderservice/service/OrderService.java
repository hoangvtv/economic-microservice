package com.phamtanhoang.orderservice.service;


import com.phamtanhoang.orderservice.dto.InventoryResponse;
import com.phamtanhoang.orderservice.dto.OrderLineItemsDto;
import com.phamtanhoang.orderservice.dto.OrderRequest;
import com.phamtanhoang.orderservice.model.Order;
import com.phamtanhoang.orderservice.model.OrderLineItems;
import com.phamtanhoang.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;

  public void placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
        .stream()
        .map(this::mapToDto)
        .toList();

    order.setOrderLineItemsList(orderLineItems);

    List<String> skuCodes = order.getOrderLineItemsList().stream()
        .map(OrderLineItems::getSkuCode)
        .toList();

    //call to Inventory Service, and place order if product is in
    //stock
    InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
        .uri("http://localhost:8080/api/inventory",
//        .uri("http://inventory-service/api/inventory",
            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block(); // synchronous call request

    boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

    if (allProductsInStock) {
      orderRepository.save(order);
    } else {
      throw new IllegalArgumentException("Product is not in stock, please try again later");
    }
    log.info("Order {} is saved", order.getOrderNumber());
  }

  private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

    return orderLineItems;
  }

}
