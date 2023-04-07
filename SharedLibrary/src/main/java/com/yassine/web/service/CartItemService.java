package com.yassine.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yassine.web.model.CartItem;
import com.yassine.web.repository.CartItemRepository;

@Service
public class CartItemService {
  @Autowired
  public CartItemRepository cartItemRepository;

  public void delete(CartItem cartItem) {
    cartItemRepository.delete(cartItem);

  }

  public CartItem save(CartItem cartItem) {
    return cartItemRepository.save(cartItem);
  }
}
