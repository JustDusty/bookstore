package com.yassine.web.model.Filter;

import org.springframework.data.jpa.domain.Specification;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookSpecifications {
  public static Specification<Book> hasAuthor(String author) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("author"),
        "%" + author + "%");
  }

  public static Specification<Book> hasCategory(Long categoryId) {
    return (root, query, criteriaBuilder) -> {
      Join<Book, Category> categoryJoin = root.join("category");
      return criteriaBuilder.equal(categoryJoin.get("id"), categoryId);
    };
  }

  public static Specification<Book> priceGreaterThanOrEqual(Double double1) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
        double1);
  }

  public static Specification<Book> priceLessThanOrEqual(Double double1) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"),
        double1);
  }
}

