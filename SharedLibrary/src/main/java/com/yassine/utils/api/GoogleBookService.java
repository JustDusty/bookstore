package com.yassine.utils.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.yassine.utils.ImageUtils;
import com.yassine.utils.api.GoogleBookCollection.GoogleBook;
import com.yassine.utils.api.GoogleBookCollection.GoogleBook.VolumeInfo;
import com.yassine.utils.api.GoogleBookCollection.GoogleBook.VolumeInfo.ImageLinks;
import com.yassine.utils.api.GoogleBookCollection.GoogleBook.VolumeInfo.IndustryIdentifier;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.model.Review;
import com.yassine.web.service.BookService;
import com.yassine.web.service.CategoryService;
import com.yassine.web.service.ReviewService;

@Component
public class GoogleBookService {
  private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private CategoryService categoryService;

  @Autowired
  private BookService bookService;

  @Autowired
  private ReviewService reviewService;


  private Faker faker = new Faker();


  @Value("${google.books.api.key}")
  private String googleApiKey;



  private String[] categories = {"Fiction", "Romance", "Thriller", "Mystery", "Horror", "Fantasy",
      "History", "Science Fiction", "Biography", "Autobiography", "Politics", "Philosophy",
      "Travel", "Religion", "Cookbooks", "Psychology", "Art", "Music", "Sports", "SOCIAL SCIENCE",
      "TRUE CRIME", "POETRY", "MEDICAL"};


  private URL buildGoogleURL(String category, int startIndex)
      throws MalformedURLException, UnsupportedEncodingException {
    String encodedCategory = URLEncoder.encode(category, "UTF-8");
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL)
        .queryParam("q", "subject:" + encodedCategory).queryParam("orderBy", "relevance")
        .queryParam("printType", "books").queryParam("startIndex", startIndex)
        .queryParam("maxResults", 40).queryParam("key", googleApiKey);
    return builder.build().toUri().toURL();
  }

  private LocalDate convertToDate(String dateString) {
    String validDateString = dateString.replaceAll("[^\\d-]", "");
    try {
      if (validDateString.length() == 4) {
        // If the string is a year only (yyyy), parse it as a year
        int year = Integer.parseInt(validDateString);
        return LocalDate.of(year, 1, 1);
      } else if (validDateString.length() == 7) {
        // If the string is a year and month (yyyy-mm), parse it as a year and month
        String[] parts = validDateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        return LocalDate.of(year, month, 1);
      } else if (validDateString.length() == 10) {
        // If the string is a full date (yyyy-mm-dd), parse it as a full date
        String[] parts = validDateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
      } else
        throw new IllegalArgumentException("Failed to parse LocalDate");
    } catch (NumberFormatException | IndexOutOfBoundsException | DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date string: " + validDateString, e);
    }
  }


  private void fetchBooksFromURL(String category) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    int startIndex = 0;
    int totalItems = 0;


    do {
      logger.info("startIndex: " + startIndex);
      URL url = buildGoogleURL(category, startIndex);
      GoogleBookCollection collection = mapper.readValue(url, GoogleBookCollection.class);
      totalItems = collection.getTotalItems();
      try {
        List<GoogleBook> googleBooks = collection.getItems();
        Category fetchCategory = categoryService.findByName(category);
        for (GoogleBook googleBook : googleBooks) {
          Book book = toBook(googleBook);
          double price = roundPrice(faker.number().randomDouble(2, 15, 99));
          book.setPrice(price);
          book.setQuantity(faker.number().numberBetween(2, 9));
          book.setCategory(fetchCategory);
          boolean exists = bookService.findByTitle(book.getTitle()) != null;
          if (!exists)
            bookService.save(book);
          categoryService.save(fetchCategory);
        }
        startIndex += 40;
      } catch (NullPointerException e) {
        logger.error("Failed to fetch books from URL: " + url);
        break;
      }
    } while (startIndex < totalItems);
  }


  private void initializeCategories() {
    List<Category> list = new ArrayList<>();
    for (String cat : categories) {
      String categoryName = cat;
      String description = faker.lorem().sentence(15);
      byte[] cover = null;
      try {
        cover = ImageUtils.getImageBytesFromUrl("https://picsum.photos/200/300?random=1");
      } catch (Exception e) {
        e.printStackTrace();
      }
      Category category = categoryService.findByName(categoryName);
      if (category == null) {
        category = new Category();
        category.setName(categoryName);
        category.setCoverImage(cover);
        category.setDescription(description);

        list.add(category);
      }
    }
    categoryService.saveAll(list);
  }

  private List<Review> initializeReviews(Book book) {
    List<Review> reviews = new ArrayList<>();
    int num = faker.number().numberBetween(20, 60);
    for (int i = 0; i < num; i++) {
      Review review = new Review();
      String email = faker.internet().emailAddress();
      String fullName = faker.name().fullName();
      String message = faker.lorem().sentence(20);
      review.setBook(book);
      review.setEmail(email);
      review.setFullName(fullName);
      review.setMessage(message);
      review.setRating(ThreadLocalRandom.current().nextDouble(1, 6));
      reviews.add(review);
    }
    return reviews;
  }

  private void initReviews() {
    List<Book> books = bookService.findAll();
    for (Book book : books) {
      List<Review> reviews = initializeReviews(book);
      book.setReviews(reviews);
      bookService.save(book);
      for (Review review : reviews)
        reviewService.save(review);
    }
  }

  private double roundPrice(double price) {
    Double roundedPrice;
    if (price % 1 <= 0.25)
      roundedPrice = Math.floor(price) + 0.00;
    else if (price % 1 <= 0.75)
      roundedPrice = Math.floor(price) + 0.50;
    else
      roundedPrice = Math.ceil(price) - 0.01;
    return roundedPrice;
  }



  public byte[] getThumbnail(GoogleBook googleBook) {
    byte[] cover = null;
    String thumbnail = null;
    ImageLinks imageLinks = googleBook.getVolumeInfo().getImageLinks();

    if (imageLinks == null)
      throw new NullPointerException();
    else {
      String smallThumbnailUrl = imageLinks.getSmallThumbnail();
      String thumbnailUrl = imageLinks.getThumbnail();
      if (smallThumbnailUrl != null)
        thumbnail = smallThumbnailUrl;
      else if (thumbnailUrl != null)
        thumbnail = thumbnailUrl;
    }
    try {
      cover = ImageUtils.getImageBytesFromUrl(thumbnail);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return cover;
  }


  public void processApiData() throws IOException {
    List<Category> categs = categoryService.findAll(null);
    for (Category category : categs)
      fetchBooksFromURL(category.getName());


  }

  public Book toBook(GoogleBook googleBook) throws NullPointerException {
    VolumeInfo volumeInfo = googleBook.getVolumeInfo();
    List<IndustryIdentifier> identifiers = volumeInfo.getIndustryIdentifiers();
    String language = volumeInfo.getLanguage();
    String publishedDate = volumeInfo.getPublishedDate();
    List<String> authors = volumeInfo.getAuthors();
    Book book = new Book();
    book.setTitle(volumeInfo.getTitle());
    book.setAuthor(authors.stream().findFirst().orElse(null));
    book.setPublisher(volumeInfo.getPublisher());
    book.setPublicationDate(convertToDate(publishedDate));
    book.setLanguage(language.toUpperCase());
    book.setDescription(volumeInfo.getDescription());
    book.setIsbn(identifiers.get(0).getIdentifier());
    book.setTags(volumeInfo.getCategories());
    book.setNumberOfPages(volumeInfo.getPageCount());
    book.setPreviewLink(volumeInfo.getPreviewLink());
    book.setInfoLink(volumeInfo.getInfoLink());
    book.setCover(getThumbnail(googleBook));
    return book;
  }

}
