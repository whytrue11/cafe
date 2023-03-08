package com.whytrue.cafe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @NotNull
  private String name;
  @NotNull @Size(min = 6)
  private String password;
  @NotNull @Size(min = 3) @Column(unique = true)
  private String username;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @JsonCreator
  public User(@JsonProperty("id") UUID id,
              @JsonProperty("name") String name,
              @JsonProperty("password") String password,
              @JsonProperty("username") String username,
              @JsonProperty("roles") Set<Role> roles,
              @JsonProperty("authorities") Set<Role> authorities,
              @JsonProperty("accountNonLocked") boolean accountNonLocked,
              @JsonProperty("accountNonExpired") boolean accountNonExpired,
              @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.username = username;
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
