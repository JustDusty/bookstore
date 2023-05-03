package com.yassine.web.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserServiceImpl;
import com.yassine.web.model.Category;
import com.yassine.web.model.ShoppingCart;
import com.yassine.web.service.CategoryService;
import com.yassine.web.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

  @Autowired
  public UserServiceImpl userService;
  @Autowired
  public ShoppingCartService shoppingCartService;
  @Autowired
  public CategoryService categoryService;

  @ModelAttribute("userCartItems")
  public Integer getCartSize(Principal principal) {
    Integer cartSize = 0;
    if (isAuthenticated(principal)) {
      Optional<User> optionalUser = userService.getByEmailOrUsername(principal.getName());
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
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
    }
    return cartSize;
  }


  @ModelAttribute("randcategories")
  public List<Category> getCategories() {
    return categoryService.findRandomTwelveCategories();
  }


  @ModelAttribute("currency")
  public String getCurrency() {
    return "MAD";
  }

  @ModelAttribute("requestURI")
  public String getRequest(HttpServletRequest request) {
    return request.getRequestURI();
  }

  @ModelAttribute("theUser")
  public User getUser(Principal principal) {
    User user = null;
    if (isAuthenticated(principal)) {
      Optional<User> optionalUser = userService.getByEmailOrUsername(principal.getName());
      if (optionalUser.isPresent())
        user = optionalUser.get();
    }
    return user;
  }

  @ModelAttribute("authenticated")
  public boolean isAuthenticated(Principal principal) {
    return principal != null;
  }
}
