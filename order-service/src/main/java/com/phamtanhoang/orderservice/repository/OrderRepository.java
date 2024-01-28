package com.phamtanhoang.orderservice.repository;

import com.phamtanhoang.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Long> {
}
