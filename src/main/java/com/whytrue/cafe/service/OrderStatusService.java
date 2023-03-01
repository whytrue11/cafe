package com.whytrue.cafe.service;

import com.whytrue.cafe.entity.OrderStatus;
import com.whytrue.cafe.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusService {

  @Autowired
  private OrderStatusRepository orderStatusRepository;

  public List<OrderStatus> readAll() {
    return orderStatusRepository.findAll();
  }
}
