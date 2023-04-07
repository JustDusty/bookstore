package com.yassine.web.config;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

  @Autowired
  private BookService bookService;

  @Autowired
  private CategoryService categoryService;


  private final int NUM_BOOKS = 15;
  private final int NUM_CATEGORIES = 1;



  private final Faker faker = new Faker();

  public static byte[] getImageBytesFromUrl(String imageUrl) throws Exception {
    URL url = new URL(imageUrl);
    BufferedImage img = ImageIO.read(url);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(img, "jpg", baos);
    return baos.toByteArray();
  }

  private void initializeBooks() {
    List<Category> categories = categoryService.findAll(null);
    for (int i = 0; i < NUM_BOOKS; i++) {

      String title = faker.elderScrolls().city();
      String author = faker.book().author();
      String publisher = faker.book().publisher();
      String description = faker.lorem().sentence();
      String language = faker.nation().language();
      String summary = faker.lorem().paragraph(10);
      byte[] cover = null;
      try {
        cover = getImageBytesFromUrl("https://picsum.photos/200/300?random=1");
      } catch (Exception e) {
        e.printStackTrace();
      }
      LocalDate publicationDate = faker.date().past(18262, TimeUnit.DAYS).toInstant()
          .atZone(ZoneId.systemDefault()).toLocalDate();
      int numberOfPages = faker.number().numberBetween(100, 1000);
      Long isbn = faker.number().randomNumber();
      Double price = faker.number().randomDouble(2, 10, 50);
      int quantity = faker.number().numberBetween(1, 50);
      int randomNum = ThreadLocalRandom.current().nextInt(categories.size());
      Category category = categories.get(randomNum);
      Book book = bookService.findByTitle(title);
      if (book == null) {
        book = new Book();
        book.setTitle(title);
        book.setCover(cover);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setDescription(description);
        book.setLanguage(language);
        book.setSummary(summary);
        book.setNumberOfPages(numberOfPages);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setQuantity(quantity);
        book.setCategory(category);
        book.setPublicationDate(publicationDate);
        bookService.save(book);
      }
    }
  }

  private void initializeCategories() {
    for (int i = 0; i < NUM_CATEGORIES; i++) {
      String categoryName = faker.aviation().aircraft();
      byte[] cover = null;
      try {
        cover = getImageBytesFromUrl("https://picsum.photos/200/300?random=1");
      } catch (Exception e) {
        e.printStackTrace();
      }
      Category category = categoryService.findByName(categoryName);
      if (category == null) {
        category = new Category();
        category.setName(categoryName);
        category.setCoverImage(cover);
        categoryService.save(category);
      }
    }
  }

  @PostConstruct
  public void initializeData() {
    // initializeCategories();
    // initializeBooks();
    // categoryService.updateNumberOfBooks();

  }



}
