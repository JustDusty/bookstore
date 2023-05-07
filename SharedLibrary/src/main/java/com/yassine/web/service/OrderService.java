package com.yassine.web.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Order;
import com.yassine.web.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {
  @Autowired
  public OrderRepository orderRepository;


  public void deleteById(long id) {
    Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    orderRepository.delete(order);
  }


  public List<Order> findAll() {
    return orderRepository.findAll();
  }

  public Order findById(Long id) {
    if (id == null)
      return null;
    Optional<Order> optional = orderRepository.findById(id);
    Order order = null;
    if (optional.isPresent())
      order = optional.get();
    return order;
  }

  public Order save(Order order) {
    return orderRepository.save(order);
  }
}
