package com.whytrue.cafe;

import com.whytrue.cafe.entity.*;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class ProductControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ProductRepository productRepository;


  @Test
  @Sql(value = "/sql-scripts/after_create_product_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /product OK")
  void create() {
    Product newProduct = new Product(null, "NEW", BigDecimal.valueOf(20000, 2), 50L,
        null, true, new ProductCategory(1L, "DEFAULT"));
    Product expectedProduct = new Product(DBdata.products.get(DBdata.products.size() - 1).getId() + 1, newProduct.getName(), newProduct.getPrice(),
        newProduct.getQuantity(), newProduct.getDescription(), newProduct.getIsActive(), new ProductCategory(1L, "DEFAULT"));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product", newProduct, Product.class);
    Product newProductFromDB = productRepository.findById(DBdata.products.get(DBdata.products.size() - 1).getId() + 1).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newProductFromDB);
    assertEquals(expectedProduct.getProductCategory().getId(), newProductFromDB.getProductCategory().getId());
    assertEquals(expectedProduct, newProductFromDB);
  }

  @Test
  @Sql(value = "/sql-scripts/after_create_product_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /product OK invalid product_category name")
  void createWithInvalidCategoryName() {
    Product newProduct = new Product(null, "NEW", BigDecimal.valueOf(20000, 2), 50L,
        null, true, new ProductCategory(1L, "invalid_name"));
    Product expectedProduct = new Product(DBdata.products.get(DBdata.products.size() - 1).getId() + 1, newProduct.getName(), newProduct.getPrice(),
        newProduct.getQuantity(), newProduct.getDescription(), newProduct.getIsActive(), new ProductCategory(1L, "DEFAULT"));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product", newProduct, Product.class);
    Product newProductFromDB = productRepository.findById(DBdata.products.get(DBdata.products.size() - 1).getId() + 1).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newProductFromDB);
    assertEquals(expectedProduct.getProductCategory().getId(), newProductFromDB.getProductCategory().getId());
    assertEquals(expectedProduct, newProductFromDB);
  }

  @Test
  @DisplayName("POST /product BAD name = null")
  void createWithNullName() {
    Product newProduct = new Product(null, null, BigDecimal.valueOf(20000, 2), 50L,
        null, true, new ProductCategory(1L, "DEFAULT"));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product", newProduct, Product.class);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("POST /product BAD product_category = null")
  void createWithNullCategory() {
    Product newProduct = new Product(null, "NEW", BigDecimal.valueOf(20000, 2), 50L,
        null, true, null);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product", newProduct, Product.class);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("POST /product BAD isActive = null")
  void createWithNullIsActive() {
    Product newProduct = new Product(null, "NEW", BigDecimal.valueOf(20000, 2), 50L,
        null, null, new ProductCategory(1L, "random_name"));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/product", newProduct, Product.class);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("GET /product/products OK")
  void readAll() {
    List<Product> expectedProducts = DBdata.products;

    ResponseEntity<List<Product>> response = restTemplate.exchange("/product/products", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedProducts.size(), response.getBody().size());
    assertTrue(expectedProducts.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /product/products/{category} OK")
  void readAllByCategory() {
    List<Product> expectedProducts = new ArrayList<>();
    ProductCategory category = DBdata.categories.get(0);
    for (Product product: DBdata.products) {
      if (product.getProductCategory().equals(category)) {
        expectedProducts.add(product);
      }
    }

    ResponseEntity<List<Product>> response = restTemplate.exchange("/product/products/{category}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, category.getName());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedProducts.size(), response.getBody().size());
    assertTrue(expectedProducts.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /product/products/{category} NOT_FOUND invalid category")
  void readAllByInvalidCategory() {
    String category = "invalid";

    ResponseEntity<List<Product>> response = restTemplate.exchange("/product/products/{category}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, category);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  @Sql(value = "/sql-scripts/after_update_product_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /product/{id} OK")
  void update() {
    Long id = DBdata.products.get(0).getId();
    Product newProductData = new Product(null, "New salad", BigDecimal.valueOf(78000, 2),
        null, null, true, new ProductCategory(2L, "FOOD"));
    Product expectedProduct = new Product(id, newProductData.getName(), newProductData.getPrice(),
        newProductData.getQuantity(), newProductData.getDescription(), newProductData.getIsActive(), newProductData.getProductCategory());
    HttpEntity<Product> entity = new HttpEntity<>(newProductData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product/{id}", HttpMethod.PUT, entity, Product.class, id);
    Product productFromDB = productRepository.findById(id).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(productFromDB);
    assertEquals(expectedProduct, productFromDB);
  }

  @Test
  @DisplayName("PUT /product/{id} CONFLICT invalid id")
  void updateWithInvalidId() {
    Long id = DBdata.products.get(DBdata.products.size() - 1).getId() + 1;
    Product newProductData = new Product(null, "New salad", BigDecimal.valueOf(78000, 2),
        null, null, true, new ProductCategory(2L, "FOOD"));
    HttpEntity<Product> entity = new HttpEntity<>(newProductData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/product/{id}", HttpMethod.PUT, entity, Product.class, id);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  @Sql(value = "/sql-scripts/after_update_product_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /product/status/{id}?isActive={isActive} OK")
  void updateIsActive() {
    Long id = DBdata.products.get(0).getId();
    boolean isActive = true;

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/product/status/{id}?isActive={isActive}", HttpMethod.PUT, null, Product.class, id, Boolean.toString(isActive));
    Product productFromDB = productRepository.findById(id).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(productFromDB);
    assertEquals(isActive, productFromDB.getIsActive());
  }

  @Test
  @DisplayName("PUT /product/status/{id}?isActive={isActive} CONFLICT product_quantity = 0")
  void updateIsActiveWithZeroQuantityProduct() {
    Long id = DBdata.products.get(6).getId();
    boolean expectedIsActive = false;
    boolean isActive = true;

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/product/status/{id}?isActive={isActive}", HttpMethod.PUT, null, Product.class, id, Boolean.toString(isActive));
    Product productFromDB = productRepository.findById(id).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(productFromDB);
    assertEquals(expectedIsActive, productFromDB.getIsActive());
  }
}
