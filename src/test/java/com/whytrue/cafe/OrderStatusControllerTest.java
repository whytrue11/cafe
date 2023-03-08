package com.whytrue.cafe;

import com.whytrue.cafe.entity.OrderStatus;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class OrderStatusControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @DisplayName("GET /order_statuses OK")
  void readAll() {
    List<OrderStatus> expectedStatuses = DBdata.statuses;

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<List<OrderStatus>> response = restTemplate.exchange("/order_statuses", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {});
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedStatuses.size(), response.getBody().size());
    assertTrue(expectedStatuses.containsAll(response.getBody()));
  }
}
