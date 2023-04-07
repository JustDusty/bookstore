package com.yassine.auth.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.yassine.web.model.Order;
import com.yassine.web.model.ShoppingCart;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {



  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", updatable = false)
  private Long id;


  @Column(name = "first_name")
  private String firstName;


  @Column(name = "last_name")
  private String lastName;


  @NotNull
  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "username", unique = true)
  private String username;

  @NotNull
  @Column(name = "password")
  private String password;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  @Column(name = "zip_code", unique = true)
  private Integer zipCode;


  @Column(name = "phone_number", unique = true)
  private String phoneNumber;

  @Column(name = "address_line1")
  private String addressLine1;
  @Column(name = "address_line2")
  private String addressLine2;
  @Column(name = "city")
  private String city;

  @Column(name = "reset_password_token")
  private String resetPasswordToken;


  @Column(name = "reset_password_token_expiration")
  private LocalDateTime resetPasswordTokenExpiration;

  @Enumerated(EnumType.STRING)
  private Role role;


  @OneToOne(mappedBy = "user")
  private ShoppingCart shoppingCart;
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<Order> orders;


  public String getAlias() {
    return username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
    return Collections.singletonList(authority);
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


}
