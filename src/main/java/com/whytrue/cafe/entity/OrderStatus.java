package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "order_statuses")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class OrderStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_status_generator")
  @SequenceGenerator(name = "order_status_generator", sequenceName = "order_statuses_seq", allocationSize = 1)
  private Long id;
  @NotNull @Column(unique = true)
  private String name;
}
