package com.whytrue.cafe.repository;

import com.whytrue.cafe.entity.ProductCategory;
import com.whytrue.cafe.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
  ProductCategory findByName(String name);
  boolean existsByName(String name);
}
