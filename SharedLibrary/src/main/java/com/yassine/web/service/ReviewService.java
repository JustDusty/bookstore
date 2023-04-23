package com.yassine.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Review;
import com.yassine.web.repository.BookRepository;
import com.yassine.web.repository.ReviewRepository;

@Service
public class ReviewService {

  @Autowired
  public ReviewRepository reviewRepository;

  @Autowired
  public BookRepository bookRepository;


  public void deleteAll(List<Review> reviews) {
    reviewRepository.deleteAll(reviews);

  }

  public List<Review> findByBook(Long bookId) {
    return reviewRepository.findByBookId(bookId);
  }

  public List<Review> findByBookId(Long bookId, Sort by) {
    return reviewRepository.findByBookId(bookId, by);
  }

  public Review save(Review review) {
    return reviewRepository.save(review);
  }
}
