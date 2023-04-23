package com.yassine.web.model;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;
  private String name;


  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      orphanRemoval = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<Book> books;

  @Formula("(SELECT COUNT(*) FROM book b WHERE b.category_id = category_id)")
  private Long numberOfBooks;

  @Column(length = 500)
  private String description;


  @Column(name = "created_at", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Lob
  @Column(columnDefinition = "MEDIUMBLOB", unique = true)
  private byte[] coverImage;

  public String getImageUrl() {
    return "data:image/png;base64," + Base64.getEncoder().encodeToString(coverImage);
  }



}
