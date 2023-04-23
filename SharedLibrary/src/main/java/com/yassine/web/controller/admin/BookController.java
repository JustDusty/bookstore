package com.yassine.web.controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @RequestMapping(value = "/edit/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
  public String updateBook(@PathVariable("id") Long id, @RequestParam("title") String title,
      @RequestParam("isbn") String isbn, @RequestParam("categoryId") Long categoryId,
      @RequestParam("author") String author, @RequestParam("publisher") String publisher,
      @RequestParam("publicationDate") String publicationDate,
      @RequestParam("language") String language,
      @RequestParam("numberOfPages") Integer numberOfPages, @RequestParam("price") Double price,
      @RequestParam("quantity") Integer quantity, @RequestParam("description") String description,
      @RequestParam("image") MultipartFile image) throws IOException {
    Book book = bookService.findById(id);
    book.setTitle(title);
    book.setIsbn(isbn);
    book.setAuthor(author);
    book.setPublicationDate(LocalDate.parse(publicationDate));
    book.setPublisher(publisher);
    book.setLanguage(language);
    book.setNumberOfPages(numberOfPages);
    book.setCategory(categoryService.findById(categoryId));
    book.setPrice(price);
    book.setQuantity(quantity);
    book.setDescription(description);

    if (!image.isEmpty()) {
      byte[] cover = image.getBytes();
      book.setCover(cover);
    }

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
