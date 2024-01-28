package com.phamtanhoang.productservice.service;

import com.phamtanhoang.productservice.dto.ProductRequest;
import com.phamtanhoang.productservice.dto.ProductResponse;
import com.phamtanhoang.productservice.model.Product;
import com.phamtanhoang.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;

  public void createProduct(ProductRequest productRequest) {
    Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).build();

    productRepository.save(product);
    log.info("Product {} is saved", product.getId());
  }

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();

    log.info("Get all product is successed");
    return products.stream().map(this::mapToProductResponse).toList();
  }

  public ProductResponse getProduct(String id) {
    Optional<Product> product = productRepository.findById(id);

    log.info("Get product {} is successed", product.get().getId());
    return mapToProductResponse(product.get());
  }

  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .build();
  }

}
