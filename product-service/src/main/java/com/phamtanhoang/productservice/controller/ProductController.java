package com.phamtanhoang.productservice.controller;

import com.phamtanhoang.productservice.dto.ProductRequest;
import com.phamtanhoang.productservice.dto.ProductResponse;
import com.phamtanhoang.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createProduct(@RequestBody ProductRequest productRequest) {
    productService.createProduct(productRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ProductResponse> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse getProduct(@PathVariable String id) {
    return  productService.getProduct(id);
  }
}
