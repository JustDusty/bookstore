package com.yassine.web.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.yassine.web.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  List<Review> findByBookId(Long bookId);

  List<Review> findByBookId(Long bookId, Sort by);
}
