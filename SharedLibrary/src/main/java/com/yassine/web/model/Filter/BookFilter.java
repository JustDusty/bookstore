package com.yassine.web.model.Filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookFilter {
  private Long categoryId;
  private String author;
  private Double minPrice;
  private Double maxPrice;
}
