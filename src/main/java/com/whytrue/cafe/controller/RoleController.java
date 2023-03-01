package com.whytrue.cafe.controller;

import com.whytrue.cafe.entity.Role;
import com.whytrue.cafe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

  @Autowired
  private RoleService roleService;

  @GetMapping("/roles")
  public ResponseEntity<List<Role>> readAll() {
    final List<Role> roles = roleService.readAll();

    return roles != null
        ? new ResponseEntity<>(roles, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
