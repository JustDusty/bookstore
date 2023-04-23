package com.yassine.auth.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

  @Autowired
  private UserService userService;

  @PostMapping
  public String processForgotPassword(HttpServletRequest request, Model model) {
    String email = request.getParameter("email");
    String token = UUID.randomUUID().toString();


    try {
      userService.updateResetPasswordToken(token, email);
      String url = request.getRequestURL().toString();
      url.replace(request.getServletPath(), "");
      String resetPasswordLink = request.getRequestURL() + "/resetPassword?token=" + token;

      userService.sendPasswordEmail(email, resetPasswordLink);
      model.addAttribute("message",
          "On vous a envoyé un mail pour récuperer votre mot de passe. Merci de vérifier votre boîte de réception");

    } catch (UsernameNotFoundException ex) {
      model.addAttribute("error", ex.getMessage());
    } catch (UnsupportedEncodingException | MessagingException e) {
      model.addAttribute("error", "Erreur d'envoi de mail");
    }

    return "auth/forgot_password_form";
  }



  @PostMapping("/resetPassword")
  public String processResetPassword(HttpServletRequest request, Model model) {

    String token = request.getParameter("token");
    String password = request.getParameter("password");

    Optional<User> user = userService.getByResetPasswordToken(token);

    model.addAttribute("title", "Réinitialiser mot de passe");
    if (user.isEmpty())
      model.addAttribute("message", "Invalid Token");
    else {
      User theUser = user.get();
      LocalDateTime expiryDate = theUser.getResetPasswordTokenExpiration();
      if (expiryDate.isBefore(LocalDateTime.now()))
        model.addAttribute("message", "Le token de réinitialisation de mot de passe est éxpiré");
      else {
        userService.updatePassword(theUser, password);
        model.addAttribute("message", "Vous avez réinitialisé votre mot de passe avec succès!");
      }
    }

    return "auth/message";

  }



  @GetMapping
  public String showForgotPasswordForm() {
    return "auth/forgot_password_form";

  }

  @GetMapping("/resetPassword")
  public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
    Optional<User> user = userService.getByResetPasswordToken(token);
    model.addAttribute("token", token);

    if (!user.isPresent()) {
      model.addAttribute("message", "Token invalide.");
      return "message";
    }

    return "auth/reset_password_form";
  }


}
