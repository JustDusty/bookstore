package com.yassine.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yassine.auth.model.User;
import com.yassine.web.model.ShoppingCart;
import com.yassine.web.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {
  @Autowired
  public ShoppingCartRepository cartRepository;

  public ShoppingCart findByUser(User user) {
    return cartRepository.findByUser(user);
  }

  public ShoppingCart save(ShoppingCart shoppingCart) {
    return cartRepository.save(shoppingCart);

  }
}
