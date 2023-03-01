package com.whytrue.cafe.controller;

import com.whytrue.cafe.entity.ProductCategory;
import com.whytrue.cafe.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_category")
public class ProductCategoryController {

  @Autowired
  private ProductCategoryService productCategoryService;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody @Validated ProductCategory category, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!productCategoryService.create(category)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/categories")
  public ResponseEntity<List<ProductCategory>> readAll() {
    final List<ProductCategory> categories = productCategoryService.readAll();

    return categories != null
        ? new ResponseEntity<>(categories, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                  @RequestBody @Validated ProductCategory productCategory, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!productCategoryService.update(productCategory, id)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
    if (!productCategoryService.delete(id)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
