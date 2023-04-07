package com.yassine.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yassine.web.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
