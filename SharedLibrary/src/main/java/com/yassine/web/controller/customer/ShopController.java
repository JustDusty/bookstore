package com.yassine.web.controller.customer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;

@Controller
public class ShopController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  public BookService bookService;

  @Autowired
  public CategoryService categoryService;



  @GetMapping("/shop")
  public String filterBooks(Model model, @Param("keyword") String keyword,
      @RequestParam(name = "category", required = false) Long categoryId,
      @RequestParam(name = "minPrice", required = false) Double minPrice,
      @RequestParam(name = "maxPrice", required = false) Double maxPrice,
      @RequestParam(name = "author", required = false) String author,
      @RequestParam(defaultValue = "0", name = "page") int page,
      @RequestParam(defaultValue = "9", name = "size") int size,
      @RequestParam(name = "currentSize", required = false) Integer currentSize) {
    Page<Book> bookPage;
    String currAuthor = null;
    Category currCategory = null;
    Double currMinPrice = null;
    Double currMaxPrice = null;
    if (currentSize != null)
      size = currentSize;
    if (keyword != null)
      bookPage = bookService.findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(
          keyword, PageRequest.of(page, size));
    else if (author != null && categoryId != null) {
      bookPage = bookService.getAllSpecifications(author, categoryId, minPrice, maxPrice,
          PageRequest.of(page, size));
      currAuthor = author;
      currCategory = categoryService.findById(categoryId);

    } else if (author == null && categoryId != null) {
      Category category = categoryService.findById(categoryId);
      bookPage = bookService.findByCategoryAndPriceBetween(category, minPrice, maxPrice,
          PageRequest.of(page, size));
      currAuthor = author;
      currCategory = category;
    } else if (author != null) {
      bookPage = bookService.findByAuthorAndPriceBetween(author, minPrice, maxPrice,
          PageRequest.of(page, size));
      currAuthor = author;
    } else if (minPrice != null) {
      currMinPrice = minPrice;
      currMaxPrice = maxPrice;
      bookPage = bookService.findByPriceBetween(minPrice, maxPrice, PageRequest.of(page, size));
    } else
      bookPage = bookService.findAll(PageRequest.of(page, size));
    model.addAttribute("currAuthor", currAuthor);
    model.addAttribute("currCategory", currCategory);
    model.addAttribute("currMinPrice", currMinPrice);
    model.addAttribute("currMaxPrice", currMaxPrice);
    model.addAttribute("keyword", keyword);
    model.addAttribute("allbooks", bookPage.getContent());
    model.addAttribute("currentSize", size);
    model.addAttribute("currentPage", bookPage.getNumber());
    model.addAttribute("totalPages", bookPage.getTotalPages());
    model.addAttribute("totalItems", bookPage.getTotalElements());
    return "user/shop";
  }


  @ModelAttribute("authors")
  public List<Object[]> getAuthors() {
    return bookService.countBooksByAuthorOrd();
  }

  @ModelAttribute("allcategories")
  public List<Category> getCategories() {
    for (Category category : categoryService.findAll(null))
      logger.warn("Number of Books :" + category.getNumberOfBooks());
    return categoryService.findAllByOrderByNameAsc();
  }

  @ModelAttribute("randcategories")
  public List<Category> getRandCategories() {
    return categoryService.findRandomTwelveCategories();
  }



}
