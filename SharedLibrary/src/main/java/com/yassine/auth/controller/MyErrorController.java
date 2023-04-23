package com.yassine.auth.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value())
        model.addAttribute("message404", "404 - Page Introuvable");
      else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
        model.addAttribute("message500", "500 - Erreur interne du serveur");
      else if (statusCode == HttpStatus.FORBIDDEN.value())
        model.addAttribute("message", "403 - Accès Interdit");
    }
    return "auth/message";
  }
}
