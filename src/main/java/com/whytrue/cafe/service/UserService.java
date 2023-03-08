package com.whytrue.cafe.service;

import com.whytrue.cafe.entity.Role;
import com.whytrue.cafe.entity.User;
import com.whytrue.cafe.repository.RoleRepository;
import com.whytrue.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
  @Autowired
  UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;
  @Autowired
  PasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    return user;
  }

  public boolean create(User user, Role role) {
    if (user == null || role == null) {
      return false;
    }
    user.setId(null);

    User userFromDB = userRepository.findByUsername(user.getUsername());
    Role roleFromDB = roleRepository.findByName(role.getName());
    if (userFromDB != null || roleFromDB == null) {
      return false;
    }

    user.setRoles(Collections.singleton(role));
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return true;
  }

  public boolean create(User user) {
    return create(user, new Role(1L, "ROLE_USER"));
  }

  public List<User> readAll(Role role) {
    return userRepository.findAllByRoles(role);
  }

  public boolean update(User user, UUID id, Principal principal) {
    User userFromDB = null;
    if (user == null || id == null || principal == null ||
        (userFromDB = userRepository.findById(id).orElse(null)) == null ||
        userRepository.existsByUsername(user.getUsername()) && !user.getUsername().equals(principal.getName())) {
      return false;
    }

    User curUser = userRepository.findByUsername(principal.getName());
    if (!curUser.getRoles().contains(new Role(3L, "ROLE_ADMIN")) && !id.equals(curUser.getId())) {
      return false;
    }

    user.setId(id);
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setRoles(userFromDB.getRoles());
    userRepository.save(user);
    return true;
  }
}
