package com.yassine.web.controller.customer;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;


@Controller
public class IndexController {


  @Autowired
  public BookService bookService;
  @Autowired
  public CategoryService categoryService;

  @ModelAttribute("randcategories")
  public List<Category> getCategories() {
    return categoryService.findRandomTwelveCategories();
  }


  @ModelAttribute("eightbooks")
  public List<Book> getLastBooks() {
    return bookService.findLastEightBooks();
  }

  @GetMapping({"/", ""})
  public String home() {
    return "redirect:/index";
  }



  @GetMapping("/index")
  public String index(Principal principal, Model model) {
    return "user/index";
  }



}
