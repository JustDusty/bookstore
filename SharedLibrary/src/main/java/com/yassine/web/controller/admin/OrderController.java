package com.yassine.web.controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.yassine.web.model.Order;
import com.yassine.web.service.OrderService;

@Controller
@RequestMapping("/admin/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping("/delete/{id}")
  public String deleteOrder(@PathVariable(value = "id") long id) {
    orderService.deleteById(id);
    return "redirect:/admin/order/";
  }


  @ModelAttribute("listStatus")
  public List<String> getAllStatus() {
    return List.of("En cours", "Livré");
  }

  @GetMapping({"/", ""})
  public String getCustomerOrders(Model model) {
    List<Order> orders = orderService.findAll();
    model.addAttribute("orders", orders);
    return "admin/orders";
  }

  @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
  public String updateCategory(@PathVariable("id") Long id, @RequestParam("status") String status,
      @RequestParam("delivery") String delivery) throws IOException {
    Order order = orderService.findById(id);
    order.setOrderStatus(status);
    order.setDeliveryDate(LocalDate.parse(delivery));
    orderService.save(order);
    return "redirect:/admin/order/";
  }
}
