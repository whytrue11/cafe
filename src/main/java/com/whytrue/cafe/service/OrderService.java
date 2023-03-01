package com.whytrue.cafe.service;

import com.whytrue.cafe.entity.Order;
import com.whytrue.cafe.entity.OrderStatus;
import com.whytrue.cafe.entity.Product;
import com.whytrue.cafe.repository.OrderRepository;
import com.whytrue.cafe.repository.OrderStatusRepository;
import com.whytrue.cafe.repository.ProductRepository;
import com.whytrue.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
  private static final List<OrderStatus> statuses;
  static {
    statuses = new ArrayList<>(4);
    statuses.add(new OrderStatus(1L, "DEFAULT"));
    statuses.add(new OrderStatus(2L, "PAID"));
    statuses.add(new OrderStatus(3L, "SUCCESS"));
    statuses.add(new OrderStatus(4L, "REJECTED"));
  }

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private OrderStatusRepository orderStatusRepository;

  public boolean create(Order order, Principal principal) {
    Product productFromDB = null;
    if (order == null || principal == null ||
        //product existing
        order.getProduct() == null || (productFromDB = productRepository.findById(order.getProduct().getId()).orElse(null)) == null ||
        //product active
        !productFromDB.getIsActive() ||
        //product quantity less when in order
        order.getQuantity() == null || (productFromDB.getQuantity() != null && order.getQuantity() > productFromDB.getQuantity())
    ) {
      return false;
    }

    order.setId(null);
    order.setDate(new Date());
    order.setUser(userRepository.findByUsername(principal.getName()));
    order.setOrderStatus(statuses.get(0));

    orderRepository.save(order);
    return true;
  }

  public List<Order> readAll() {
    return orderRepository.findAll();
  }

  public List<Order> readAll(OrderStatus status) {
    if (status == null ||
        status.getId() == null || (status = orderStatusRepository.findById(status.getId()).orElse(null)) == null) {
      return null;
    }
    return orderRepository.findAllByOrderStatus(status);
  }

  public boolean updateStatus(Long orderId, OrderStatus status) {
    Order order = null;
    if (orderId == null || status == null ||
        //order existing
        (order = orderRepository.findById(orderId).orElse(null)) == null ||
        //status existing
        status.getId() == null || (status = orderStatusRepository.findById(status.getId()).orElse(null)) == null) {
      return false;
    }

    switch (status.getName()) {
      case "PAID": {
        if (!order.getOrderStatus().equals(statuses.get(0)) ||
            //product active
            !order.getProduct().getIsActive() ||
            //product quantity less when in order
            order.getQuantity() == null ||
            (order.getProduct().getQuantity() != null && order.getQuantity() > order.getProduct().getQuantity())) {
          return false;
        }

        Product product = order.getProduct();
        if (product.getQuantity() != null) {
          long totalQuantity = product.getQuantity() - order.getQuantity();
          product.setQuantity(totalQuantity);
          if (totalQuantity == 0) {
            product.setIsActive(false);
          }
        }
        productRepository.save(product);
        break;
      }
      case "SUCCESS":
        break;
      case "REJECT": {
        if (!order.getOrderStatus().equals(statuses.get(1))) {
          return false;
        }

        Product product = order.getProduct();
        if (product.getQuantity() != null) {
          product.setQuantity(product.getQuantity() + order.getQuantity());
          product.setIsActive(true);
        }
        break;
      }
      default:
        return false;
    }
    order.setOrderStatus(status);
    orderRepository.save(order);
    return true;
  }
}
