package com.phamtanhoang.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phamtanhoang.productservice.dto.ProductRequest;
import com.phamtanhoang.productservice.dto.ProductResponse;
import com.phamtanhoang.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProductRepository productRepository;

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @Test
  void shouldCreateProduct() throws Exception {
    ProductRequest productRequest = getProductRequest();
    String productRequestString = objectMapper.writeValueAsString(productRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productRequestString))
        .andExpect(status().isCreated());

    Assertions.assertEquals(1, productRepository.findAll().size());
  }

//  @Test
//  void shouldGetAllProduct() throws Exception {
//    ProductResponse productResponse1 = createProductResponse("Product 1", "Product 1"
//        , BigDecimal.valueOf(200));
//    ProductResponse productResponse2 = createProductResponse("Product 1", "Product 1"
//        , BigDecimal.valueOf(200));
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))  // Expect a list of 2 products
//        .andExpect((ResultMatcher) jsonPath("$[0].name", is(productResponse1.getName())))
//        .andExpect((ResultMatcher) jsonPath("$[1].name", is(productResponse2.getName())));
//  }

  private ProductRequest getProductRequest() {
    return ProductRequest.builder()
        .name("Iphone 13")
        .description("Iphone 13")
        .price(BigDecimal.valueOf(1200))
        .build();
  }

  private ProductResponse createProductResponse(String name, String description, BigDecimal price) {
    return ProductResponse.builder()
        .name(name)
        .description(description)
        .price(price)
        .build();
  }


}
