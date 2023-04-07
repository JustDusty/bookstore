package com.yassine.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Review;
import com.yassine.web.repository.ReviewRepository;

@Service
public class ReviewService {

  @Autowired
  public ReviewRepository reviewRepository;


  public List<Review> findByBook(Long bookId) {
    return reviewRepository.findByBookId(bookId);
  }

  public Review save(Review review) {
    return reviewRepository.save(review);
  }
}
