package com.yassine.web.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id")
  private Long id;
  private String title;

  private String author;
  private String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publicationDate;
  private String language;
  private Integer numberOfPages;
  @Column(length = 1000)
  private String description;
  @Column(length = 5000)
  private String summary;
  private Long isbn;
  private Double price;
  private Integer quantity;
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  private Category category;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Review> reviews;

  @OrderBy("createdAt DESC")
  @Column(name = "created_at", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Lob
  @Column(columnDefinition = "MEDIUMBLOB", unique = true)
  private byte[] cover;


  public Double calculateTotalPrice() {
    return price * quantity;
  }


  public String getImageUrl() {
    return "data:image/png;base64," + Base64.getEncoder().encodeToString(cover);
  }

}
