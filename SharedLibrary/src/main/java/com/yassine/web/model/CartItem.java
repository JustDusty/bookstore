package com.yassine.web.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private Long id;
  private Integer quantity;
  @Column(name = "total_price", scale = 2)
  private Double totalPrice;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")
  private ShoppingCart cart;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", referencedColumnName = "book_id")
  private Book book;

  public Double calculateTotalPrice() {
    return book.getPrice() * quantity;
  }



}
