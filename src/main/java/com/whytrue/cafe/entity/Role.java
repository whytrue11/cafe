package com.whytrue.cafe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class Role implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
  @SequenceGenerator(name = "role_generator", sequenceName = "roles_seq", allocationSize = 1)
  private Long id;
  @NotNull @Column(unique = true)
  private String name;

  @Override
  public String getAuthority() {
    return this.name;
  }
}
