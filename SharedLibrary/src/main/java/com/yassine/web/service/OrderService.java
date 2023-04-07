package com.yassine.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Order;
import com.yassine.web.repository.OrderRepository;

@Service
public class OrderService {
  @Autowired
  public OrderRepository orderRepository;


  public Order save(Order order) {
    return orderRepository.save(order);
  }
}
