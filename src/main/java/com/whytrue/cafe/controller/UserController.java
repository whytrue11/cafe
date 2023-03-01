package com.whytrue.cafe.controller;

import com.whytrue.cafe.entity.Role;
import com.whytrue.cafe.entity.User;
import com.whytrue.cafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/registration")
  public ResponseEntity<?> create(@RequestBody @Validated User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!userService.create(user)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/registration/{role}")
  public ResponseEntity<?> create(@PathVariable(name = "role") String roleStr, @RequestBody @Validated User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Role role = switch (roleStr) {
      case "employee" -> new Role(2L, "ROLE_EMPLOYEE");
      case "admin" -> new Role(3L, "ROLE_ADMIN");
      default -> null;
    };
    if (role == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (!userService.create(user, role)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{role}")
  public ResponseEntity<?> readAll(@PathVariable(name = "role") String roleStr) {
    Role role = switch (roleStr) {
      case "users" -> new Role(1L, "ROLE_USER");
      case "employees" -> new Role(2L, "ROLE_EMPLOYEE");
      case "admins" -> new Role(3L, "ROLE_ADMIN");
      default -> null;
    };

    List<User> users = null;
    if (role != null) {
      users = userService.readAll(role);
    }

    return users != null
        ? new ResponseEntity<>(users, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") UUID id, @RequestBody @Validated User user,
                                  BindingResult bindingResult, Principal principal) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!userService.update(user, id, principal)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
