package com.yassine.web.model.pojo;

import java.time.LocalDate;
import com.yassine.web.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPojo {
  private Long id;
  private String isbn;
  private String title;
  private String description;
  private String author;
  private String publisher;
  private LocalDate publicationDate;
  private Category category;
  private String language;
  private Integer quantity;
  private Double price;
  private Integer numberOfPages;
  private byte[] cover;
}
