package com.yassine.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Order;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;
  @Value("${spring.mail.username}")
  private String senderEmail;

  @Async
  public void sendContactEmail(String email, String subject, String message) {
    SimpleMailMessage toSend = new SimpleMailMessage();
    toSend.setFrom(email);
    toSend.setTo(senderEmail);
    toSend.setSubject(subject);
    toSend.setText(message);
    mailSender.send(toSend);

  }

  @Async
  public void sendOrderConfirmationEmail(Order order) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(senderEmail);
    message.setTo(order.getUser().getEmail());
    message.setSubject("Order Confirmation");

    String emailText =
        "Merci pour votre commande ! Voici les détails :\n\n" + "ID de commande : " + order.getId()
            + "\n" + "Date de commande : " + order.getOrderDate() + "\n" + "Date de livraison : "
            + order.getDeliveryDate() + "\n" + "Prix total : " + order.getTotalPrice() + "\n"
            + "Méthode de paiement : " + order.getPaymentMethod() + "\n" + "Statut de commande : "
            + order.getOrderStatus() + "\n\n" + "Merci d'avoir choisi notre magasin !";

    message.setText(emailText);
    mailSender.send(message);
  }
}
