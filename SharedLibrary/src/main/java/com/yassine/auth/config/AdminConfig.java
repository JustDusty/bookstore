package com.yassine.auth.config;

import java.util.List;
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
@PropertySource("classpath:admin-accounts.properties")
public class AdminConfig {
  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  UserRepository userRepository;

  @Value("${admin.accounts}")
  private List<String> adminAccounts;

  @Bean
  public CommandLineRunner createAdminAccounts() {
    return args -> {
      for (String account : adminAccounts) {
        String[] parts = account.split(":");
        String username = parts[0];
        String email = parts[1];
        String password = parts[2];

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
      }
    };
  }
}
