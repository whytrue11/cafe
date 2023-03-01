package com.whytrue.cafe.controller;

import com.whytrue.cafe.entity.OrderStatus;
import com.whytrue.cafe.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderStatusController {

  @Autowired
  private OrderStatusService orderStatusService;

  @GetMapping("/order_statuses")
  public ResponseEntity<List<OrderStatus>> readAll() {
    final List<OrderStatus> orderStatuses = orderStatusService.readAll();

    return orderStatuses != null
        ? new ResponseEntity<>(orderStatuses, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
