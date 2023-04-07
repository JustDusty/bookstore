package com.yassine.web.controller.customer;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.javafaker.Faker;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;

@Controller
public class ShopController {

  @Autowired
  public BookService bookService;

  @Autowired
  public CategoryService categoryService;



  @GetMapping("/shop")
  public String filterBooks(Model model, @Param("keyword") String keyword,
      @RequestParam(name = "category", required = false) Long categoryId,
      @RequestParam(name = "minPrice", required = false) Double minPrice,
      @RequestParam(name = "maxPrice", required = false) Double maxPrice,
      @RequestParam(name = "author", required = false) String author) {
    List<Book> books = new ArrayList<>();



    if (keyword != null)
      books =
          bookService.findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(keyword);
    else if (author != null && categoryId != null) {
      books = bookService.getAllSpecifications(author, categoryId, minPrice, maxPrice);
      model.addAttribute("currAuthor", author);
      model.addAttribute("currCategory", categoryService.findById(categoryId));
    } else if (author == null && categoryId != null) {
      Category category = categoryService.findById(categoryId);
      books = bookService.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
      model.addAttribute("currCategory", category);
    } else if (author != null) {
      books = bookService.findByAuthorAndPriceBetween(author, minPrice, maxPrice);
      model.addAttribute("currAuthor", author);
    } else
      books = bookService.findAll();


    model.addAttribute("keyword", keyword);
    model.addAttribute("allbooks", books);
    model.addAttribute("img", new Faker().avatar().image());
    return "user/shop";
  }

  @ModelAttribute("authors")
  public List<Object[]> getAuthors() {
    return bookService.countBooksByAuthorOrd();
  }

  @ModelAttribute("allcategories")
  public List<Category> getCategories() {
    return categoryService.findAllByOrderByNameAsc();
  }

  @ModelAttribute("randcategories")
  public List<Category> getRandCategories() {
    return categoryService.findRandomTwelveCategories();
  }



}
