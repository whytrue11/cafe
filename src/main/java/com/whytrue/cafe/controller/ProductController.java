package com.whytrue.cafe.controller;

import com.whytrue.cafe.entity.Product;
import com.whytrue.cafe.entity.ProductCategory;
import com.whytrue.cafe.repository.ProductCategoryRepository;
import com.whytrue.cafe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductCategoryRepository productCategoryRepository;

  @PostMapping()
  public ResponseEntity<?> create(@RequestBody @Validated Product product, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!productService.create(product)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/products")
  public ResponseEntity<List<Product>> readAll() {
    final List<Product> products = productService.readAll();

    return products != null
        ? new ResponseEntity<>(products, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/products/{category}")
  public ResponseEntity<List<Product>> readAll(@PathVariable(name = "category") String categoryStr) {
    ProductCategory category = productCategoryRepository.findByName(categoryStr);

    List<Product> products = null;
    if (category != null) {
      products = productService.readAll(category);
    }

    return products != null
        ? new ResponseEntity<>(products, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                  @RequestBody @Validated Product product, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!productService.update(product, id)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/status/{id}")
  public ResponseEntity<?> updateStatus(@PathVariable(name = "id") Long id,
                                        @RequestParam(value = "isActive") Boolean isActive) {
    if (!productService.updateStatus(id, isActive)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
