package com.yassine.web.model;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.yassine.auth.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDate orderDate;
  private LocalDate deliveryDate;
  @Column(name = "total_price", scale = 2)
  private Double totalPrice;
  private String orderStatus;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

}
