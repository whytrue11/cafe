package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
  @SequenceGenerator(name = "product_generator", sequenceName = "products_seq", allocationSize = 1)
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
