package com.whytrue.cafe.repository;

import com.whytrue.cafe.entity.Order;
import com.whytrue.cafe.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findAllByOrderStatus(OrderStatus status);
}
