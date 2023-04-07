package com.yassine.auth.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import com.yassine.auth.model.User;
import jakarta.mail.MessagingException;


public interface UserService {
  Optional<User> getByEmailOrUsername(String email);



  Optional<User> getByResetPasswordToken(String token);


  List<Object> isUserPresent(User user);

  void saveUser(User user);


  void sendPasswordEmail(String recipientEmail, String link)
      throws MessagingException, UnsupportedEncodingException;


  void updatePassword(User user, String newPassword);


  void updateResetPasswordToken(String token, String email);

}
