package com.yassine.auth.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.yassine.auth.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.username = ?1 or u.email =?1")
  Optional<User> findByEmailOrUsername(String emailOrUsername);

  Optional<User> findByResetPasswordToken(String token);
}
