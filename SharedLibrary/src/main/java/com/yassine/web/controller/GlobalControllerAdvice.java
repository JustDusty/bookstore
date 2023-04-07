package com.yassine.web.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserServiceImpl;
import com.yassine.web.model.ShoppingCart;
import com.yassine.web.service.ShoppingCartService;

@ControllerAdvice
public class GlobalControllerAdvice {

  @Autowired
  public UserServiceImpl userService;
  @Autowired
  public ShoppingCartService shoppingCartService;

  @ModelAttribute("userCartItems")
  public Integer getCartSize(Principal principal) {
    Integer cartSize = 0;
    if (isAuthenticated(principal)) {
      User user = userService.getByEmailOrUsername(principal.getName()).get();
      ShoppingCart shoppingCart = user.getShoppingCart();
      if (shoppingCart == null) {
        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        user.setShoppingCart(shoppingCart);
        shoppingCartService.save(shoppingCart);
        userService.save(user);
      } else
        cartSize = shoppingCart.calculateTotalItems();
    }
    return cartSize;
  }



  @ModelAttribute("theUser")
  public User getUser(Principal principal) {
    if (isAuthenticated(principal)) {
      User user = userService.getByEmailOrUsername(principal.getName()).get();
      return user;
    }
    return null;
  }

  @ModelAttribute("authenticated")
  public boolean isAuthenticated(Principal principal) {
    return principal != null;
  }
}
