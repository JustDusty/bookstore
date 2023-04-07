package com.yassine.web.controller.admin;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;

@Controller
@RequestMapping("/admin/book")
public class BookController {

  @Autowired
  private BookService bookService;

  @Autowired
  private CategoryService categoryService;



  @GetMapping("/delete/{id}")
  public String deleteBook(@PathVariable(value = "id") long id) {
    bookService.deleteById(id);
    return "redirect:/admin/book/";
  }



  @GetMapping("edit-book/{id}")
  public String editBook(@PathVariable(value = "id") long id, Model model) {
    Book book = bookService.findById(id);
    model.addAttribute("categories", categoryService.findAll(null));
    model.addAttribute("book", book);
    return "admin/edit-book";
  }


  @PostMapping("/save")
  public String saveBook(@ModelAttribute("book") Book book,
      @RequestParam("categoryId") Long categoryId, @RequestParam("image") MultipartFile cover,
      @RequestParam("isbn") String isbn) throws IOException {
    // save Category to database
    if (!cover.isEmpty()) {
      byte[] img = cover.getBytes();
      book.setCover(img);
    }
    Category category = categoryService.findById(categoryId);
    book.setCategory(category);

    bookService.save(book);
    return "redirect:/admin/book/";
  }

  @GetMapping("/showNewBookForm")
  public String showNewBookForm(Model model) {
    // create model attribute to bind form data
    Book book = new Book();
    model.addAttribute("book", book);
    model.addAttribute("categories", categoryService.findAll(null));
    return "admin/add-book";
  }

  @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
  public String updateBook(@RequestBody Book book, @RequestParam("image") MultipartFile cover,
      @RequestParam("isbn") String isbn) throws IOException {
    byte[] img = cover.getBytes();
    book.setCover(img);
    bookService.save(book);
    return "redirect:/admin/book/";
  }



  @GetMapping({"", "/"})
  public String viewHomePage(Model model) {

    model.addAttribute("books", bookService.findAll());
    model.addAttribute("categories", categoryService.findAll(null));
    return "admin/books";
  }

}
