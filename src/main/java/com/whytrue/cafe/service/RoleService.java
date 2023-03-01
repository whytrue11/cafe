package com.whytrue.cafe.service;

import com.whytrue.cafe.entity.Role;
import com.whytrue.cafe.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  public List<Role> readAll() {
    return roleRepository.findAll();
  }
}
