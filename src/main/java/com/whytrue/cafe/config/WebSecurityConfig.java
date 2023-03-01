package com.whytrue.cafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService)
      throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(bCryptPasswordEncoder)
        .and()
        .build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/roles").hasRole("ADMIN")

        .requestMatchers("/user/registration").permitAll()
        .requestMatchers("/user/registration/*").hasRole("ADMIN")
        .requestMatchers("/user/users").hasRole("ADMIN")
        .requestMatchers("/user/employees").hasRole("ADMIN")
        .requestMatchers("/user/admins").hasRole("ADMIN")

        .requestMatchers("/product_category/categories").authenticated()
        .requestMatchers("/product_category").hasRole("ADMIN")
        .requestMatchers("/product_category/*").hasRole("ADMIN")

        .requestMatchers("/product/products").authenticated()
        .requestMatchers("/product/status/*").hasAnyRole("EMPLOYEE" ,"ADMIN")
        .requestMatchers("/product/*").hasRole("ADMIN")

        .requestMatchers("/order_statuses").hasRole("ADMIN")

        .requestMatchers("/order").authenticated()
        .requestMatchers("/order/*").hasAnyRole("EMPLOYEE", "ADMIN")

        .anyRequest().authenticated()
        .and().httpBasic()
        .and().sessionManagement().disable();

    return http.build();
  }
}
