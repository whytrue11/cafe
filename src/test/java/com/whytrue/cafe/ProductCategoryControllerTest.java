package com.whytrue.cafe;

import com.whytrue.cafe.entity.Product;
import com.whytrue.cafe.entity.ProductCategory;
import com.whytrue.cafe.repository.ProductCategoryRepository;
import com.whytrue.cafe.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class ProductCategoryControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ProductCategoryRepository productCategoryRepository;
  @Autowired
  private ProductRepository productRepository;

  @Test
  @Sql(value = "/sql-scripts/after_create_product_category_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /product_category/categories OK")
  void create() {
    ProductCategory newCategory = new ProductCategory(null, "NEW");

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product_category", newCategory, ProductCategory.class);
    ProductCategory newCategoryFromDB = productCategoryRepository.findByName(newCategory.getName());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newCategoryFromDB);
    assertEquals(newCategory.getName(), newCategoryFromDB.getName());
  }

  @Test
  @Sql(value = "/sql-scripts/after_create_product_category_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /product_category/categories OK invalid id")
  void createWithNotNullId() {
    ProductCategory newCategory = new ProductCategory(-10L, "NEW");

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product_category", newCategory, ProductCategory.class);
    ProductCategory newCategoryFromDB = productCategoryRepository.findByName(newCategory.getName());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newCategoryFromDB);
    assertEquals(newCategory.getName(), newCategoryFromDB.getName());
    assertNotEquals(newCategory.getId(), newCategoryFromDB.getId());
  }

  @Test
  @DisplayName("GET /product_category/categories")
  void readAll() {
    List<ProductCategory> expectedCategories = DBdata.categories;

    ResponseEntity<List<ProductCategory>> response = restTemplate.exchange("/product_category/categories", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedCategories.size(), response.getBody().size());
    assertTrue(expectedCategories.containsAll(response.getBody()));
  }

  @Test
  @Sql(value = "/sql-scripts/after_update_product_category_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /product_category/{id} OK")
  void update() {
    Long id = 2L;
    ProductCategory newCategoryData = new ProductCategory(null, "NEW");
    HttpEntity<ProductCategory> entity = new HttpEntity<>(newCategoryData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product_category/{id}", HttpMethod.PUT, entity, ProductCategory.class, id);
    ProductCategory newCategoryFromDB = productCategoryRepository.findById(id).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newCategoryFromDB);
    assertEquals(newCategoryData.getName(), newCategoryFromDB.getName());
  }

  @Test
  @DisplayName("PUT /product_category/{id} CONFLICT can't update DEFAULT category")
  void updateDefaultCategory() {
    Long id = 1L;
    ProductCategory newCategoryData = new ProductCategory(null, "NEW");
    HttpEntity<ProductCategory> entity = new HttpEntity<>(newCategoryData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product_category/{id}", HttpMethod.PUT, entity, ProductCategory.class, id);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  @DisplayName("PUT /product_category/{id} CONFLICT invalid category id")
  void updateInvalidCategory() {
    Long id = -1L;
    ProductCategory newCategoryData = new ProductCategory(null, "NEW");
    HttpEntity<ProductCategory> entity = new HttpEntity<>(newCategoryData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product_category/{id}", HttpMethod.PUT, entity, ProductCategory.class, id);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  @Sql(value = "/sql-scripts/after_delete_product_category_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("DELETE /product_category/{id} OK")
  void delete() {
    Long id = 3L;
    List<Product> expectedProducts = productRepository.findAllByProductCategory(DBdata.categories.get(2));
      for (Product product: expectedProducts) {
        product.setProductCategory(DBdata.categories.get(0));
      }
      expectedProducts.addAll(productRepository.findAllByProductCategory(DBdata.categories.get(0)));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product_category/{id}", HttpMethod.DELETE, null, ProductCategory.class, id);
    ProductCategory categoryFromDB = productCategoryRepository.findById(id).orElse(null);
    List<Product> productsFromDBDefaultCategory = productRepository.findAllByProductCategory(DBdata.categories.get(0));
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(categoryFromDB);
    assertEquals(expectedProducts.size(), productsFromDBDefaultCategory.size());
    assertTrue(expectedProducts.containsAll(productsFromDBDefaultCategory));
  }

  @Test
  @DisplayName("DELETE /product_category/{id} CONFLICT can't delete DEFAULT category")
  void deleteDefaultCategory() {
    Long id = 1L;

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product_category/{id}", HttpMethod.DELETE, null, ProductCategory.class, id);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }
}
