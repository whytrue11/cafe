package com.whytrue.cafe.repository;

import com.whytrue.cafe.entity.Role;
import com.whytrue.cafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByUsername(String username);
  List<User> findAllByRoles(Role role);
  boolean existsByUsername(String username);
}
