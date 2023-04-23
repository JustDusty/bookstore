package com.yassine.web.controller.customer;

import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserServiceImpl;
import com.yassine.web.model.pojo.ContactDetail;
import com.yassine.web.service.EmailService;

@Controller
@RequestMapping("/contact")
public class ContactController {

  @Autowired
  public UserServiceImpl userService;



  @Autowired
  public EmailService emailService;


  @GetMapping({"/", ""})
  public String checkout(Model model, Principal principal) {
    Optional<User> optional = Optional.empty();
    if (principal != null)
      optional = userService.getByEmailOrUsername(principal.getName());
    if (optional.isEmpty())
      model.addAttribute("contactDetail", new ContactDetail());
    else {
      User user = optional.get();
      ContactDetail detail = ContactDetail.builder().email(user.getEmail())
          .fullName(user.getFirstName() + " " + user.getLastName()).build();
      model.addAttribute("contactDetail", detail);
    }

    return "user/contact";
  }

  @PostMapping("/send")
  public String sendContact(@ModelAttribute("contactDetail") ContactDetail contactDetail,
      BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors())
      return "user/contact";
    String email = contactDetail.getEmail();
    String subject = contactDetail.getSubject();
    String message = contactDetail.getMessage();

    emailService.sendContactEmail(email, subject, message);
    return "redirect:/contact";
  }
}
