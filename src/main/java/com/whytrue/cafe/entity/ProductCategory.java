package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product_categories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class ProductCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_generator")
  @SequenceGenerator(name = "product_category_generator", sequenceName = "product_categories_seq", allocationSize = 1)
  private Long id;
  @NotNull @Column(unique = true)
  private String name;
}
