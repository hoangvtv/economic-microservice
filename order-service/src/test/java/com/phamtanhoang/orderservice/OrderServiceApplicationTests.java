package com.phamtanhoang.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phamtanhoang.orderservice.dto.OrderLineItemsDto;
import com.phamtanhoang.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

  @Container
  static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:5.7");

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private OrderRepository orderRepository;


  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @Test
  void shouldPlaceOrder() throws Exception {
    List<OrderLineItemsDto> orderLineItemsDtoList = getListOrderLineItemsDto();
    String orderLineItemsDtoListString = objectMapper.writeValueAsString(orderLineItemsDtoList);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderLineItemsDtoListString))
        .andExpect(status().isCreated());

    Assertions.assertEquals(1, orderRepository.findAll().size());
  }

  private List<OrderLineItemsDto> getListOrderLineItemsDto() {
    List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<>();

    orderLineItemsDtoList.add(OrderLineItemsDto.builder()
        .id(Long.valueOf(12121212))
        .skuCode("iphone_13")
        .price(BigDecimal.valueOf(1200))
        .quantity(1)
        .build());

    return orderLineItemsDtoList;
  }

}
