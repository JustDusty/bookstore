package com.yassine.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @GetMapping
  public String admin() {
    return "redirect:/admin/index";
  }

  @GetMapping({"/dashboard"})
  public String adminCategory() {
    return "redirect:/admin/index";
  }



  @GetMapping("/404")
  public String error() {
    return "404";
  }
}
