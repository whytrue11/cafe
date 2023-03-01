package com.whytrue.cafe.repository;

import com.whytrue.cafe.entity.Product;
import com.whytrue.cafe.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Transactional
  @Modifying
  @Query("UPDATE Product p SET p.productCategory = :to where p.productCategory = :from")
  void setAllCategories(@Param("from") ProductCategory from, @Param("to") ProductCategory to);
}
