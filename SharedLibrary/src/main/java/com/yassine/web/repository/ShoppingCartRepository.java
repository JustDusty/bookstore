package com.yassine.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yassine.auth.model.User;
import com.yassine.web.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

  ShoppingCart findByUser(User user);

}
