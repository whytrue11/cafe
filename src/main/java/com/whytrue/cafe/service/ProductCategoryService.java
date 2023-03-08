package com.whytrue.cafe.service;

import com.whytrue.cafe.entity.ProductCategory;
import com.whytrue.cafe.repository.ProductCategoryRepository;
import com.whytrue.cafe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

  private static final ProductCategory defaultCategory;
  static {
    defaultCategory = new ProductCategory();
    defaultCategory.setId(1L);
    defaultCategory.setName("DEFAULT");
  }

  @Autowired
  private ProductCategoryRepository productCategoryRepository;
  @Autowired
  private ProductRepository productRepository;

  public boolean create(ProductCategory category) {
    if (category == null || productCategoryRepository.existsByName(category.getName())) {
      return false;
    }

    productCategoryRepository.save(category);
    return true;
  }

  public List<ProductCategory> readAll() {
    return productCategoryRepository.findAll();
  }

  public boolean update(ProductCategory category, Long id) {
    if (category == null || id == null ||
        !productCategoryRepository.existsById(id) ||
        productCategoryRepository.existsByName(category.getName()) ||
        id.equals(defaultCategory.getId())) {
      return false;
    }

    category.setId(id);
    productCategoryRepository.save(category);
    return true;
  }

  public boolean delete(Long id) {
    ProductCategory categoryFromDB = null;
    if (id == null ||
        (categoryFromDB = productCategoryRepository.findById(id).orElse(null)) == null ||
        id.equals(defaultCategory.getId())) {
      return false;
    }

    productRepository.setAllCategories(categoryFromDB, defaultCategory);
    productCategoryRepository.delete(categoryFromDB);
    return true;
  }
}
