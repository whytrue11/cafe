package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
  @SequenceGenerator(name = "order_generator", sequenceName = "orders_seq", allocationSize = 1)
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
