package com.yassine.auth.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.yassine.auth.model.Role;
import com.yassine.auth.model.User;
import com.yassine.auth.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String senderEmail;

  @Override
  public Optional<User> getByEmailOrUsername(String email) {
    return userRepository.findByEmailOrUsername(email);
  }



  @Override
  public Optional<User> getByResetPasswordToken(String token) {
    return userRepository.findByResetPasswordToken(token);
  }

  @Override
  public List<Object> isUserPresent(User user) {
    boolean userExists = false;
    String message = null;
    Optional<User> existingUserEmail = userRepository.findByEmailOrUsername(user.getEmail());
    if (existingUserEmail.isPresent()) {
      userExists = true;
      message = "Email existe déjà!";
    }
    System.out.println("existingUserEmail.isPresent() - " + existingUserEmail.isPresent());
    return Arrays.asList(userExists, message);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmailOrUsername(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("USER_NOT_FOUND", email)));

  }

  public void save(User user) {
    userRepository.save(user);
  }

  @Override
  public void saveUser(User user) {
    String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
    user.setRole(Role.USER);
    userRepository.save(user);
  }

  @Override
  @Async
  public void sendPasswordEmail(String recipientEmail, String link)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

    helper.setFrom(senderEmail, "No-Reply");
    helper.setTo(recipientEmail);
    String subject = "Voici le lien pour réinitialiser votre mot de passe";

    String content =
        "<p>Bonjour,</p>" + "<p>Vous avez demandé à réinitialiser votre mot de passe.</p>"
            + "<p>Cliquez sur le lien ci-dessous pour changer votre mot de passe :</p>"
            + "<p><a href=\"" + link + "\">Changer mon mot de passe</a></p>" + "<br>"
            + "<p>Ignorez cet e-mail si vous n'avez pas fait la demande.</p>";


    helper.setSubject(subject);
    helper.setText(content, true);
    mailSender.send(message);
  }


  @Override
  public void updatePassword(User user, String newPassword) {

    String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);
    user.setResetPasswordToken(null);
    user.setResetPasswordTokenExpiration(null);
    userRepository.save(user);
  }


  @Override
  public void updateResetPasswordToken(String token, String email)
      throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmailOrUsername(email);
    if (user.isPresent()) {
      User theUser = user.get();
      theUser.setResetPasswordToken(token);
      theUser.setResetPasswordTokenExpiration(LocalDateTime.now().plusHours(24));
      userRepository.save(theUser);
    } else
      throw new UsernameNotFoundException("Nom d'utilisateur introuvable " + email);
  }


}
