package com.yassine.web.controller.customer;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserServiceImpl;
import com.yassine.web.model.CartItem;
import com.yassine.web.model.Order;
import com.yassine.web.model.ShoppingCart;
import com.yassine.web.model.pojo.AddressForm;
import com.yassine.web.service.EmailService;
import com.yassine.web.service.OrderService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {



  @Autowired
  public UserServiceImpl userService;

  @Autowired
  public OrderService orderService;

  @Autowired
  public EmailService emailService;

  @Autowired
  public JavaMailSender mailSender;

  @GetMapping({"/", ""})
  public String checkout(Model model, Principal principal) {
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    AddressForm addressForm = AddressForm.builder().firstName(user.getFirstName())
        .lastName(user.getLastName()).phoneNumber(user.getPhoneNumber())
        .addressLine1(user.getAddressLine1()).addressLine2(user.getAddressLine2())
        .city(user.getCity()).email(user.getEmail()).zipCode(user.getZipCode()).build();

    model.addAttribute("addressForm", addressForm);

    return "user/checkout";
  }

  @PostMapping("/confirm-order")
  public String confirmOrder(@ModelAttribute("order") Order order, Model model,
      BindingResult bindingResult, Principal principal) {

    if (bindingResult.hasErrors())
      return "user/checkout";
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    Double totalPrice = user.getShoppingCart().getTotalPrice();

    order.setOrderDate(LocalDate.now());
    order.setDeliveryDate(LocalDate.now().plusDays(3));
    order.setOrderStatus("En cours");
    order.setUser(user);
    order.setTotalPrice(totalPrice);
    order.setPaymentMethod("Paiement à la livraison");
    order.setIsDeleted(false);

    Order savedOrder = orderService.save(order);

    emailService.sendOrderConfirmationEmail(savedOrder);

    model.addAttribute("order", savedOrder);

    return "redirect:/checkout";
  }

  @ModelAttribute("shoppingCart")
  public ShoppingCart getCart(Principal principal) {
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    return user.getShoppingCart();

  }


  @ModelAttribute("allCartItems")
  public Set<CartItem> getCartItems(Principal principal) {
    User user = userService.getByEmailOrUsername(principal.getName()).get();
    ShoppingCart cart = user.getShoppingCart();
    return cart.getCartItemList();
  }

  @PostMapping("/save-address")
  public String saveAddress(@ModelAttribute("addressForm") AddressForm addressForm,
      BindingResult bindingResult, Model model, Principal principal) {

    if (bindingResult.hasErrors())
      return "user/checkout";
    User user = userService.getByEmailOrUsername(principal.getName()).get();

    user.setFirstName(addressForm.getFirstName());
    user.setLastName(addressForm.getLastName());
    user.setPhoneNumber(addressForm.getPhoneNumber());
    user.setAddressLine1(addressForm.getAddressLine1());
    user.setAddressLine2(addressForm.getAddressLine2());
    user.setCity(addressForm.getCity());
    user.setZipCode(addressForm.getZipCode());

    userService.save(user);

    return "redirect:/checkout";
  }
}
