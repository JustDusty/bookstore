package com.yassine.web.controller.customer;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserServiceImpl;
import com.yassine.web.model.Book;
import com.yassine.web.model.CartItem;
import com.yassine.web.model.ShoppingCart;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CartItemService;
import com.yassine.web.service.ShoppingCartService;

@Controller
public class CartController {

  @Autowired
  public BookService bookService;

  @Autowired
  public CartItemService cartItemService;

  @Autowired
  public ShoppingCartService shoppingCartService;

  @Autowired
  public UserServiceImpl userService;


  @PostMapping("add-to-cart")
  public String addToCart(@RequestParam("bookId") Long bookId,
      @RequestParam("quantity") Integer quantity, Principal principal) {
    Book book = bookService.findById(bookId);
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    ShoppingCart shoppingCart = user.getShoppingCart();

    if (shoppingCart == null) {
      shoppingCart = new ShoppingCart();
      shoppingCart.setUser(user);
      user.setShoppingCart(shoppingCart);
      shoppingCartService.save(shoppingCart);
      userService.save(user);
    }

    Set<CartItem> cartItems = shoppingCart.getCartItemList();
    Optional<CartItem> optionalCartItem =
        cartItems.stream().filter(item -> item.getBook().getId().equals(bookId)).findFirst();

    if (optionalCartItem.isPresent()) {
      CartItem cartItem = optionalCartItem.get();
      cartItem.setQuantity(quantity + cartItem.getQuantity());
      cartItem.setTotalPrice(cartItem.calculateTotalPrice());
    } else {
      CartItem cartItem = new CartItem();
      cartItem.setBook(book);
      cartItem.setQuantity(quantity);
      cartItem.setTotalPrice(cartItem.calculateTotalPrice());
      cartItemService.save(cartItem);
      cartItem.setCart(shoppingCart);
      cartItems.add(cartItem);

    }

    shoppingCart.setCartItemList(cartItems);
    System.out.println(cartItems);
    shoppingCart.setTotalItems(shoppingCart.calculateTotalItems());
    shoppingCart.setTotalPrice(shoppingCart.calculateTotalPrice());
    shoppingCartService.save(shoppingCart);

    return "redirect:/cart";
  }

  @PostMapping("/cart/delete/{itemId}")
  public String deleteFromCart(@PathVariable("itemId") Long itemId, Principal principal) {
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    ShoppingCart cart = shoppingCartService.findByUser(user);
    Set<CartItem> cartItems = cart.getCartItemList();

    Optional<CartItem> optionalCartItem =
        cartItems.stream().filter(item -> item.getId().equals(itemId)).findFirst();

    if (optionalCartItem.isPresent()) {
      CartItem itemToDelete = optionalCartItem.get();
      cartItems.remove(itemToDelete);
      cart.setCartItemList(cartItems);
      cart.setTotalItems(cart.calculateTotalItems());
      cart.setTotalPrice(cart.calculateTotalPrice());
      cartItemService.delete(itemToDelete);
      shoppingCartService.save(cart);
    }

    return "redirect:/cart";
  }

  @GetMapping("/cart")
  public String getCart(Principal principal, Model model) {
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    ShoppingCart cart = shoppingCartService.findByUser(user);
    model.addAttribute("shoppingCart", cart);
    return "user/cart";

  }



}
