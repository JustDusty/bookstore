package com.yassine.web.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id")
  private Long id;

  @Column(columnDefinition = "VARCHAR(500)")
  private String title;
  private String author;

  private String publisher;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publicationDate;

  private String language;
  private Integer numberOfPages;
  @Column(columnDefinition = "TEXT")
  private String description;
  private String isbn;
  @Column(name = "price", scale = 2)
  private Double price;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "book_tags", joinColumns = @JoinColumn(name = "book_id"))
  @Column(name = "tag", nullable = false)
  @Cascade({org.hibernate.annotations.CascadeType.ALL})
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "book_id")
  private List<String> tags = new ArrayList<>();
  private String previewLink;
  private String infoLink;


  private Integer quantity;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  private Category category;


  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER,
      orphanRemoval = true)
  @ToString.Exclude
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Review> reviews;

  @Formula("(SELECT COUNT(*) FROM reviews r WHERE r.book_id = book_id)")
  @ToString.Exclude
  private Integer numberOfRatings;

  @Formula("(SELECT COALESCE(AVG(r.rating), 0) FROM reviews r WHERE r.book_id = book_id)")
  private Double rating;


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
    return "data:image/jpg;base64," + Base64.getEncoder().encodeToString(cover);
  }



}
