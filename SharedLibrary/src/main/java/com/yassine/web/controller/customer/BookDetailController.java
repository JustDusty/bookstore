package com.yassine.web.controller.customer;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.yassine.auth.model.User;
import com.yassine.auth.service.UserServiceImpl;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.model.Review;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;
import com.yassine.web.service.ReviewService;

@Controller
@RequestMapping(value = "/shop/{id}")
public class BookDetailController {


  @Autowired
  public UserServiceImpl userService;
  @Autowired
  public BookService bookService;

  @Autowired
  public CategoryService categoryService;

  @Autowired
  public ReviewService reviewService;

  @ModelAttribute("reviews")
  public List<Review> getBookDetail(@PathVariable(name = "id") Long id) {
    return bookService.getAllReviewsForBook(id);

  }

  @PostMapping
  public String getBookDetail(@PathVariable(name = "id") Long id,
      @RequestParam("message") String message, @RequestParam("email") String email, Model model,
      Principal principal) {


    Review review = new Review();

    Book book = bookService.findById(id);
    review.setBook(book);

    Optional<User> optional = userService.getByEmailOrUsername(principal.getName());
    if (optional.isPresent()) {
      User user = optional.get();
      review.setUser(user);
      review.setFullName(user.getFullName());
      review.setEmail(user.getEmail());
    } else {
      review.setEmail(email);
      review.setFullName("Anonymous");
    }
    review.setMessage(message);

    reviewService.save(review);
    return "redirect:/shop/" + id;
  }

  @GetMapping
  public String getDetail(Model model, Principal principal) {
    Optional<User> optional = userService.getByEmailOrUsername(principal.getName());
    Review review = new Review();
    if (optional.isPresent()) {
      User user = optional.get();
      review.setEmail(user.getEmail());
      review.setFullName(user.getFullName());
    }

    model.addAttribute("review", review);
    return "user/detail";
  }



  @ModelAttribute("book")
  public Book getTheBook(@PathVariable(name = "id") Long id, Model model) {
    Book book = bookService.findById(id);

    Category category = book.getCategory();
    List<Book> similarBooks = bookService.findRecentSimilarBooksByCategory(category);
    model.addAttribute("similarBooks", similarBooks);

    model.addAttribute("bookQuantity", book.getQuantity());
    return book;
  }



}
