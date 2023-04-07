package com.yassine.auth.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AuthController {
  @Autowired
  UserService userService;

  @GetMapping("/about-us")
  public String about() {
    return "about-us";
  }



  @GetMapping("/access-denied")
  public String getAccessDenied(Model model) {
    model.addAttribute("message", "403 - Access Denied");
    return "auth/message";
  }


  @GetMapping({"/login"})
  public String login(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
      return "auth/login";
    model.addAttribute("alreadyLoggedIn", "You are already Logged in");

    if (isAdmin)
      return "redirect:/admin";
    return "redirect:/";
  }

  @GetMapping("/logout")
  public String logout() {
    return "auth/login";
  }

  @GetMapping({"/register"})
  public String register(Model model) {
    model.addAttribute("user", new User());
    return "auth/register";
  }

  @PostMapping({"/register"})
  public String registerUser(Model model, @Valid User user, HttpServletRequest request,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("successMessage", "Sign-up failed");
      model.addAttribute("bindingResult", bindingResult);
      return "auth/register";
    }

    List<Object> userPresentObj = userService.isUserPresent(user);
    if ((boolean) userPresentObj.get(0)) {
      model.addAttribute("successMessage", userPresentObj.get(1));
      return "auth/register";
    }
    userService.saveUser(user);
    model.addAttribute("successMessage", "Registered successfully!");

    return "auth/login";
  }



}
