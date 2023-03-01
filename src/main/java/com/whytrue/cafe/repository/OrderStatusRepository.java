package com.whytrue.cafe.repository;

import com.whytrue.cafe.entity.OrderStatus;
import com.whytrue.cafe.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
