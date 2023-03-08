package com.whytrue.cafe;

import com.whytrue.cafe.entity.Role;
import com.whytrue.cafe.entity.User;
import com.whytrue.cafe.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class UserControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder bCryptPasswordEncoder;


  @Test
  @Sql(value = "/sql-scripts/after_create_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /user/registration OK")
  void create() {
    User newUser = new User(null, "notnull_name", "good_password", "uniq_username", null);
    Set<Role> expectedNewUserRoles = Collections.singleton(new Role(1L, "ROLE_USER"));

    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration", newUser, User.class);
    User newUserFromDB = userRepository.findByUsername(newUser.getUsername());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newUserFromDB.getId());
    assertEquals(newUser.getName(), newUserFromDB.getName());
    assertEquals(expectedNewUserRoles.size(), newUserFromDB.getRoles().size());
    assertTrue(expectedNewUserRoles.containsAll(newUserFromDB.getRoles()));
  }

  @Test
  @Sql(value = "/sql-scripts/after_create_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /user/registration OK not empty roles")
  void createWithNotEmptyRoles() {
    User newUser = new User(null, "notnull_name", "good_password", "uniq_username",
        Collections.singleton(new Role(3L, "ROLE_ADMIN")));
    Set<Role> expectedNewUserRoles = Collections.singleton(new Role(1L, "ROLE_USER"));

    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration", newUser, User.class);
    User newUserFromDB = userRepository.findByUsername(newUser.getUsername());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newUserFromDB.getId());
    assertEquals(newUser.getName(), newUserFromDB.getName());
    assertEquals(expectedNewUserRoles.size(), newUserFromDB.getRoles().size());
    assertTrue(expectedNewUserRoles.containsAll(newUserFromDB.getRoles()));
  }

  @Test
  @DisplayName("POST /user/registration BAD name = null")
  void createWithNullName() {
    User newUser = new User(null, null, "good_password", "uniq_username", null);

    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration", newUser, User.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @DisplayName("POST /user/registration CONFLICT not unique username")
  void createWithNotUniqUsername() {
    User newUser = new User(null, "notnull_name", "good_password", "dev_user", null);

    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration", newUser, User.class);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  @DisplayName("POST /user/registration BAD short password")
  void createWithShortPassword() {
    //min length = 6
    User newUser = new User(null, "notnull_name", "12345", "uniq_username", null);

    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration", newUser, User.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @Sql(value = "/sql-scripts/after_create_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /user/registration/admin OK")
  void createAdmin() {
    User newUser = new User(null, "notnull_name", "good_password", "uniq_username", null);
    Set<Role> expectedNewUserRoles = Collections.singleton(new Role(3L, "ROLE_ADMIN"));
    String roleStr = "admin";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration/{role}", newUser, User.class, roleStr);
    User newUserFromDB = userRepository.findByUsername(newUser.getUsername());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newUserFromDB.getId());
    assertEquals(newUser.getName(), newUserFromDB.getName());
    assertEquals(expectedNewUserRoles.size(), newUserFromDB.getRoles().size());
    assertTrue(expectedNewUserRoles.containsAll(newUserFromDB.getRoles()));
  }

  @Test
  @Sql(value = "/sql-scripts/after_create_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("POST /user/registration/employee OK")
  void createEmployee() {
    User newUser = new User(null, "notnull_name", "good_password", "uniq_username", null);
    Set<Role> expectedNewUserRoles = Collections.singleton(new Role(2L, "ROLE_EMPLOYEE"));
    String roleStr = "employee";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.postForEntity("/user/registration/{role}", newUser, User.class, roleStr);
    User newUserFromDB = userRepository.findByUsername(newUser.getUsername());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(newUserFromDB.getId());
    assertEquals(newUser.getName(), newUserFromDB.getName());
    assertEquals(expectedNewUserRoles.size(), newUserFromDB.getRoles().size());
    assertTrue(expectedNewUserRoles.containsAll(newUserFromDB.getRoles()));
  }

  @Test
  @DisplayName("GET /user/users")
  void readAllUsers() {
    List<User> expectedUsers = new ArrayList<>(1);
    expectedUsers.add(DBdata.users.get(0));
    String roleStr = "users";

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<List<User>> response = restTemplate.exchange("/user/{role}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, roleStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedUsers.size(), response.getBody().size());
    assertTrue(containsAll(expectedUsers, response.getBody()));
  }

  @Test
  @DisplayName("GET /user/employees OK")
  void readAllEmployees() {
    List<User> expectedUsers = new ArrayList<>(1);
    expectedUsers.add(DBdata.users.get(1));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    String roleStr = "employees";
    ResponseEntity<List<User>> response = restTemplate.exchange("/user/{role}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, roleStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedUsers.size(), response.getBody().size());
    assertTrue(containsAll(expectedUsers, response.getBody()));
  }

  @Test
  @DisplayName("GET /user/admin OK")
  void readAllAdmins() {
    List<User> expectedUsers = new ArrayList<>(1);
    expectedUsers.add(DBdata.users.get(2));

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    String roleStr = "admins";
    ResponseEntity<List<User>> response = restTemplate.exchange("/user/{role}", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}, roleStr);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedUsers.size(), response.getBody().size());
    assertTrue(containsAll(expectedUsers, response.getBody()));
  }

  @Test
  @Sql(value = "/sql-scripts/after_update_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /user/{id} OK admin to other user")
  void updateByIdAdmin() {
    UUID id = DBdata.users.get(0).getId();
    User newUserData = new User(null, "notnull_name", "good_password", "uniq_username", null);
    User expectedUser = new User(id, newUserData.getName(), newUserData.getPassword(), newUserData.getUsername(), DBdata.users.get(0).getRoles());
    HttpEntity<User> entity = new HttpEntity<>(newUserData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/user/{id}", HttpMethod.PUT, entity, User.class, id);
    User userFromDB = userRepository.findByUsername(newUserData.getUsername());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(equals(expectedUser, userFromDB));
  }

  @Test
  @Sql(value = "/sql-scripts/after_update_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /user/{id} OK user himself")
  void updateByIdAuthenticatedUser() {
    UUID id = DBdata.users.get(0).getId();
    User newUserData = new User(null, "notnull_name", "good_password", "uniq_username", null);
    User expectedUser = new User(id, newUserData.getName(), newUserData.getPassword(), newUserData.getUsername(), DBdata.users.get(0).getRoles());
    HttpEntity<User> entity = new HttpEntity<>(newUserData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_user", "dev_user"));
    ResponseEntity<?> response = restTemplate.exchange("/user/{id}", HttpMethod.PUT, entity, User.class, id);
    User userFromDB = userRepository.findByUsername(newUserData.getUsername());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(equals(expectedUser, userFromDB));
  }

  @Test
  @Sql(value = "/sql-scripts/after_update_user_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("PUT /user/{id} OK can't update role")
  void updateRoleByIdAdmin() {
    UUID id = DBdata.users.get(0).getId();
    User newUserData = new User(null, "notnull_name", "good_password", "uniq_username", Collections.singleton(new Role(3L, "ROLE_ADMIN")));
    User expectedUser = new User(id, newUserData.getName(), newUserData.getPassword(), newUserData.getUsername(), DBdata.users.get(0).getRoles());
    HttpEntity<User> entity = new HttpEntity<>(newUserData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_admin", "dev_admin"));
    ResponseEntity<?> response = restTemplate.exchange("/user/{id}", HttpMethod.PUT, entity, User.class, id);
    User userFromDB = userRepository.findByUsername(newUserData.getUsername());
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(equals(expectedUser, userFromDB));
  }

  @Test
  @DisplayName("PUT /user/{id} CONFLICT user can't update not himself")
  void updateByIdOtherUser() {
    UUID id = DBdata.users.get(1).getId();
    User newUserData = new User(null, "notnull_name", "good_password", "uniq_username", null);
    HttpEntity<User> entity = new HttpEntity<>(newUserData);

    restTemplate.getRestTemplate().getInterceptors().add(new BasicAuthenticationInterceptor("dev_user", "dev_user"));
    ResponseEntity<?> response = restTemplate.exchange("/user/{id}", HttpMethod.PUT, entity, User.class, id);
    restTemplate.getRestTemplate().getInterceptors().clear();

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  private boolean equals(User a, User b) {
    if (a == b) return true;
    if (b == null) return false;
    return a.getId().equals(b.getId()) && a.getName().equals(b.getName()) &&
        (bCryptPasswordEncoder.matches(a.getPassword(), b.getPassword()) ||
            bCryptPasswordEncoder.matches(b.getPassword(), a.getPassword()) || a.getPassword().equals(b.getPassword())) &&
        a.getUsername().equals(b.getUsername()) &&
        a.getRoles().size() == b.getRoles().size() && a.getRoles().containsAll(b.getRoles());
  }

  private boolean containsAll(List<User> a, List<User> b) {
    for (User userA: a) {
      int i = 0;
      for (; i < b.size(); ++i) {
        if (equals(userA, b.get(i))) {
          break;
        }
      }
      if (i == b.size()) {
        return false;
      }
    }
    return true;
  }
}
