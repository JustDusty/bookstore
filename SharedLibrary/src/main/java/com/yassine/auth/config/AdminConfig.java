package com.yassine.auth.config;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.yassine.auth.model.Role;
import com.yassine.auth.model.User;
import com.yassine.auth.repository.UserRepository;


@Configuration
@PropertySource("classpath:auth.properties")
public class AdminConfig {
  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  UserRepository userRepository;


  @Bean
  public CommandLineRunner createAdminAccount(@Value("${admin.username}") String username,
      @Value("${admin.email}") String email, @Value("${admin.password}") String password) {
    return args -> {
      Optional<User> admin = userRepository.findByEmailOrUsername(email);
      if (!admin.isPresent()) {
        User theAdmin = new User();
        theAdmin.setUsername(username);
        theAdmin.setEmail(email);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        theAdmin.setPassword(encodedPassword);
        theAdmin.setRole(Role.ADMIN);
        userRepository.save(theAdmin);
      }
    };
  }
}
