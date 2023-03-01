package com.whytrue.cafe.controller;

import com.whytrue.cafe.entity.Order;
import com.whytrue.cafe.entity.OrderStatus;
import com.whytrue.cafe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping()
  public ResponseEntity<?> create(@RequestBody Order order, Principal principal) {
    if (!orderService.create(order, principal)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/orders")
  public ResponseEntity<List<Order>> readAll() {
    final List<Order> orders = orderService.readAll();

    return orders != null
        ? new ResponseEntity<>(orders, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/{status}")
  public ResponseEntity<List<Order>> readAll(@PathVariable(name = "status") String statusStr) {
    OrderStatus status = switch (statusStr) {
      case "DEFAULT" -> new OrderStatus(1L, statusStr);
      case "PAID" -> new OrderStatus(2L, statusStr);
      case "SUCCESS" -> new OrderStatus(3L, statusStr);
      case "REJECTED" -> new OrderStatus(4L, statusStr);
      default -> null;
    };

    List<Order> orders = null;
    if (status != null) {
      orders = orderService.readAll(status);
    }

    return orders != null
        ? new ResponseEntity<>(orders, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateStatus(@PathVariable(name = "id") Long id, @RequestBody OrderStatus status) {
    if (!orderService.updateStatus(id, status)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
