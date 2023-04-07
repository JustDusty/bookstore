package com.yassine.auth.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.yassine.auth.model.Role;
import com.yassine.auth.model.User;
import com.yassine.auth.repository.UserRepository;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  @Autowired
  private UserRepository userRepository;



  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oauth2User = delegate.loadUser(userRequest);
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    String email = oauth2User.getAttribute("email");
    String firstName = oauth2User.getAttribute("given_name");
    String lastName = oauth2User.getAttribute("family_name");


    Optional<User> userOptional = userRepository.findByEmailOrUsername(email);
    User user;
    if (userOptional.isPresent()) {
      user = userOptional.get();
      user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
      userRepository.save(user);
    } else {

      user = new User();
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
      user.setRole(Role.USER);
      user.setCreatedAt(LocalDateTime.now());
      userRepository.save(user);
    }

    // Create a new OAuth2User with the updated user attributes
    Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
    attributes.put("email", email);
    attributes.put("given_name", firstName);
    attributes.put("family_name", lastName);
    return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")),
        attributes, "email");
  }
}
