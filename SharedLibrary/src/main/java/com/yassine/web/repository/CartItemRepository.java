package com.yassine.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yassine.web.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
