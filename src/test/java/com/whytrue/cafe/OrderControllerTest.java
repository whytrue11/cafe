package com.whytrue.cafe;

import com.whytrue.cafe.entity.Order;
import com.whytrue.cafe.entity.OrderStatus;
import com.whytrue.cafe.entity.Product;
import com.whytrue.cafe.entity.User;
import com.whytrue.cafe.repository.OrderRepository;
import com.whytrue.cafe.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Sql(value = "/sql-scripts/after_order_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ProductRepository productRepository;

  @Test
  @DisplayName("POST /order OK")
  void create() {
    long productId = 6L;
    Product product = new Product();
    product.setId(productId);
    Order newOrder = new Order(null, null, 3L, "test description", null, product, null);
    User expectedUser = DBdata.users.get(0);
    OrderStatus expectedStatus = DBdata.statuses.get(0);
    Product expectedProduct = DBdata.products.get((int) (productId - 1));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_user", "dev_user"));
    ResponseEntity<?> response = restTemplate.postForEntity("/order", newOrder, Order.class);
    Order newOrderFromDB = orderRepository.findById(DBdata.orders.get(DBdata.orders.size() - 1).getId() + 1).orElse(null);
    Product productFromDB = productRepository.findById(expectedProduct.getId()).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newOrderFromDB);
    assertNotNull(newOrderFromDB.getDate());
    assertEquals(newOrder.getQuantity(), newOrderFromDB.getQuantity());
    assertEquals(newOrder.getDescription(), newOrderFromDB.getDescription());
    assertEquals(newOrder.getProduct().getId(), newOrderFromDB.getProduct().getId());
    assertEquals(expectedUser, newOrderFromDB.getUser());
    assertEquals(expectedStatus, newOrderFromDB.getOrderStatus());

    assertNotNull(productFromDB);
    assertEquals(expectedProduct, productFromDB);
  }

  @Test
  @DisplayName("GET /order/orders OK")
  void readAll() {
    List<Order> expectedOrders = DBdata.orders;

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<List<Order>> response = restTemplate.exchange("/order/orders", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {});
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedOrders.size(), response.getBody().size());
    assertTrue(expectedOrders.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /order/DEFAULT OK")
  void readAllDefault() {
    List<Order> expectedOrders = new ArrayList<>(4);
    String statusStr = DBdata.statuses.get(0).getName();
    for (Order order: DBdata.orders) {
      if (order.getOrderStatus().getName().equals(statusStr)) {
        expectedOrders.add(order);
      }
    }

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<List<Order>> response = restTemplate.exchange("/order/{status}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedOrders.size(), response.getBody().size());
    assertTrue(expectedOrders.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /order/PAID OK")
  void readAllPaid() {
    List<Order> expectedOrders = new ArrayList<>(1);
    String statusStr = DBdata.statuses.get(1).getName();
    for (Order order: DBdata.orders) {
      if (order.getOrderStatus().getName().equals(statusStr)) {
        expectedOrders.add(order);
      }
    }

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<List<Order>> response = restTemplate.exchange("/order/{status}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedOrders.size(), response.getBody().size());
    assertTrue(expectedOrders.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /order/SUCCESS OK")
  void readAllSuccess() {
    List<Order> expectedOrders = new ArrayList<>(1);
    String statusStr = DBdata.statuses.get(2).getName();
    for (Order order: DBdata.orders) {
      if (order.getOrderStatus().getName().equals(statusStr)) {
        expectedOrders.add(order);
      }
    }

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<List<Order>> response = restTemplate.exchange("/order/{status}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedOrders.size(), response.getBody().size());
    assertTrue(expectedOrders.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /order/REJECTED OK")
  void readAllRejected() {
    List<Order> expectedOrders = new ArrayList<>(1);
    String statusStr = DBdata.statuses.get(3).getName();
    for (Order order: DBdata.orders) {
      if (order.getOrderStatus().getName().equals(statusStr)) {
        expectedOrders.add(order);
      }
    }

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<List<Order>> response = restTemplate.exchange("/order/{status}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedOrders.size(), response.getBody().size());
    assertTrue(expectedOrders.containsAll(response.getBody()));
  }

  @Test
  @DisplayName("GET /order/{status} NOT_FOUND invalid status")
  void readAllInvalidStatus() {
    String statusStr = "invalid_status";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<List<Order>> response = restTemplate.exchange("/order/{status}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @DisplayName("PUT /order/{id}?status=PAID OK DEFAULT to PAID")
  void updateStatusDefaultToPaid() {
    Long orderId = DBdata.orders.get(0).getId();
    String statusStr = "PAID";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(1));
      expectedOrder.getProduct().setQuantity(expectedOrder.getProduct().getQuantity() - expectedOrder.getQuantity());

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertEquals(expectedOrder, orderFromDB);
  }

  @Test
  @DisplayName("PUT /order/{id}?status=PAID OK DEFAULT to PAID with product_quantity = null")
  void updateStatusDefaultToPaidWithNullQuantityProduct() {
    Long orderId = DBdata.orders.get(4).getId();
    String statusStr = "PAID";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(1));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertEquals(expectedOrder, orderFromDB);
  }

  @Test
  @DisplayName("PUT /order/{id}?status=PAID CONFLICT DEFAULT to PAID product_isActive = false")
  void updateStatusDefaultToPaidWithInactiveProduct() {
    Long orderId = DBdata.orders.get(5).getId();
    String statusStr = "PAID";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertFalse(orderFromDB.getProduct().getIsActive());
  }

  @Test
  @DisplayName("PUT /order/{id}?status=PAID CONFLICT DEFAULT to PAID product_quantity < order_quantity")
  void updateStatusDefaultToPaidWithALotOfQuantity() {
    Long orderId = DBdata.orders.get(6).getId();
    String statusStr = "PAID";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertTrue(orderFromDB.getProduct().getQuantity() < orderFromDB.getQuantity());
  }

  @Test
  @DisplayName("PUT /order/{id}?status=PAID OK DEFAULT to PAID product_quantity = order_quantity")
  void updateStatusDefaultToPaidBuyAllProduct() {
    Long orderId = DBdata.orders.get(7).getId();
    String statusStr = "PAID";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(1));
      expectedOrder.getProduct().setQuantity(expectedOrder.getProduct().getQuantity() - expectedOrder.getQuantity());
      expectedOrder.getProduct().setIsActive(false);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertEquals(expectedOrder, orderFromDB);
  }

  @Test
  @DisplayName("PUT /order/{id}?status=SUCCESS CONFLICT DEFAULT to SUCCESS")
  void updateStatusDefaultToSuccess() {
    Long orderId = DBdata.orders.get(0).getId();
    String statusStr = "SUCCESS";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  @DisplayName("PUT /order/{id}?status=REJECTED OK DEFAULT to REJECTED")
  void updateStatusDefaultToRejected() {
    Long orderId = DBdata.orders.get(0).getId();
    String statusStr = "REJECTED";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(3));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("PUT /order/{id}?status=PAID OK PAID to SUCCESS")
  void updateStatusPaidToSuccess() {
    Long orderId = DBdata.orders.get(1).getId();
    String statusStr = "SUCCESS";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(2));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertEquals(expectedOrder, orderFromDB);
  }

  @Test
  @DisplayName("PUT /order/{id}?status=REJECTED OK PAID to REJECTED")
  void updateStatusPaidToRejected() {
    Long orderId = DBdata.orders.get(1).getId();
    String statusStr = "REJECTED";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(3));
      expectedOrder.getProduct().setQuantity(expectedOrder.getProduct().getQuantity() + expectedOrder.getQuantity());

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertEquals(expectedOrder, orderFromDB);
  }

  @Test
  @DisplayName("PUT /order/{id}?status=REJECTED OK PAID to REJECTED product_isActive = false")
  void updateStatusPaidToRejectedInactiveProduct() {
    Long orderId = DBdata.orders.get(8).getId();
    String statusStr = "REJECTED";
    Order expectedOrder = orderRepository.findById(orderId).orElse(null);
      assertNotNull(expectedOrder);
      expectedOrder.setOrderStatus(DBdata.statuses.get(3));
      expectedOrder.getProduct().setQuantity(expectedOrder.getProduct().getQuantity() + expectedOrder.getQuantity());
      expectedOrder.getProduct().setIsActive(true);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    Order orderFromDB = orderRepository.findById(orderId).orElse(null);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(orderFromDB);
    assertEquals(expectedOrder, orderFromDB);
  }

  @Test
  @Sql(value = "/sql-scripts/after_order_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /order/{id}?status=SUCCESS CONFLICT SUCCESS to REJECTED")
  void updateStatusSuccessToRejected() {
    Long orderId = DBdata.orders.get(2).getId();
    String statusStr = "REJECTED";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  @Sql(value = "/sql-scripts/after_order_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /order/{id}?status=invalid CONFLICT invalid status")
  void updateStatusInvalid() {
    Long orderId = DBdata.orders.get(0).getId();
    String statusStr = "invalid";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_employee", "dev_employee"));
    ResponseEntity<?> response = restTemplate.exchange("/order/{id}?status={status}", HttpMethod.PUT, null,
        Order.class, orderId, statusStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }
}
