package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  @NotNull
  private String name;
  private BigDecimal price;
  private Long quantity;
  private String description;
  @NotNull
  private Boolean isActive;
  @ManyToOne @NotNull
  private ProductCategory productCategory;
}
