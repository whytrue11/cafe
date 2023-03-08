package com.whytrue.cafe.service;

import com.whytrue.cafe.entity.Product;
import com.whytrue.cafe.entity.ProductCategory;
import com.whytrue.cafe.repository.ProductCategoryRepository;
import com.whytrue.cafe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ProductCategoryRepository productCategoryRepository;

  public boolean create(Product product) {
    if (product == null || product.getProductCategory() == null ||
        !productCategoryRepository.existsById(product.getProductCategory().getId())) {
      return false;
    }
    product.setId(null);

    productRepository.save(product);
    return true;
  }

  public List<Product> readAll() {
    return productRepository.findAll();
  }

  public List<Product> readAll(ProductCategory category) {
    return productRepository.findAllByProductCategory(category);
  }

  public boolean update(Product product, Long id) {
    if (product == null || id == null ||
        product.getProductCategory() == null || !productCategoryRepository.existsById(product.getProductCategory().getId()) ||
        !productRepository.existsById(id)) {
      return false;
    }

    product.setId(id);
    productRepository.save(product);
    return true;
  }

  public boolean updateStatus(Long id, Boolean isActive) {
    Product productFromDB = null;
    if (id == null || isActive == null ||
        (productFromDB = productRepository.findById(id).orElse(null)) == null ||
        (productFromDB.getQuantity() != null && productFromDB.getQuantity() == 0)
    ) {
      return false;
    }

    productFromDB.setIsActive(isActive);
    productRepository.save(productFromDB);
    return true;
  }
}
