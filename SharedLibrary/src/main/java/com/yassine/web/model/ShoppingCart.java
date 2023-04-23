package com.yassine.web.model;

import java.io.Serializable;
import java.util.Set;
import com.yassine.auth.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shopping_cart_id")
  private Long id;
  private Integer totalItems;
  @Column(name = "total_price", scale = 2)
  private Double totalPrice;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;


  @Column(name = "payment_method")
  private String paymentMethod;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", fetch = FetchType.EAGER)
  private Set<CartItem> cartItemList;


  public Integer calculateTotalItems() {
    int qty = 0;
    for (CartItem item : cartItemList)
      qty += item.getQuantity();
    return qty;
  }

  public Double calculateTotalPrice() {
    double total = 0.0;
    for (CartItem item : cartItemList)
      total += item.getTotalPrice();
    return total;
  }
}
