package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private Date date;
  @NotNull
  private Long quantity;
  private String description;
  @ManyToOne @NotNull
  private User user;
  @ManyToOne @NotNull
  private Product product;
  @ManyToOne @NotNull
  private OrderStatus orderStatus;
}
