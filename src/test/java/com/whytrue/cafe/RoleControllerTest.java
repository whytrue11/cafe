package com.whytrue.cafe;

import com.whytrue.cafe.entity.Role;
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
public class RoleControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @DisplayName("GET /roles OK")
  void readAll() {
    List<Role> expectedRoles = DBdata.roles;

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<List<Role>> response = restTemplate.exchange("/roles", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedRoles.size(), response.getBody().size());
    assertTrue(expectedRoles.containsAll(response.getBody()));
  }
}
