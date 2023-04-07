package com.yassine.web.service;

import static org.springframework.data.jpa.domain.Specification.where;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import com.yassine.web.model.Review;
import com.yassine.web.model.Filter.BookSpecifications;
import com.yassine.web.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookService {


  @Autowired
  public BookRepository bookRepository;

  @Autowired
  public CategoryService categoryService;

  @Autowired
  public ReviewService reviewService;


  public long countBooksByAuthor(String author) {
    List<Object[]> result = bookRepository.countBooksByAuthor();
    for (Object[] row : result)
      if (row[0].equals(author))
        return (long) row[1];
    return 0;
  }

  public List<Object[]> countBooksByAuthorOrd() {
    return bookRepository.countBooksByAuthor();
  }

  public void delete(Book book) {
    bookRepository.delete(book);
    categoryService.updateNumberOfBooks();
  }

  public void deleteAll() {
    bookRepository.deleteAll();
    categoryService.updateNumberOfBooks();
  }

  public void deleteById(long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    Category category = book.getCategory();

    // Remove the Book entity from the Category entity's collection of books
    if (category != null) {
      category.getBooks().remove(book);

      categoryService.save(category);
    }

    // Delete the Book entity
    bookRepository.delete(book);
    categoryService.updateNumberOfBooks();
  }

  public List<Book> findAll() {
    return bookRepository.findAll();
  }



  public Page<Book> findAll(Pageable paging) {
    return bookRepository.findAll(paging);
  }

  public Page<Book> findAll(PageRequest of) {
    return bookRepository.findAll(of);
  }

  public List<Book> findAll(String keyword) {
    if (keyword != null)
      return bookRepository.search(keyword);
    return bookRepository.findAll();
  }

  public List<String> findAllAuthors() {
    return bookRepository.findAllAuthors();
  }

  public List<String> findAllAuthorsOrderedAsc() {
    return bookRepository.findAllAuthorsOrderedAsc();
  }

  public List<Book> findBooksByPriceRange(double minPrice, double maxPrice) {
    return bookRepository.findByPriceBetween(minPrice, maxPrice);
  }

  public List<Book> findByAuthorAndPriceBetween(String author, Double minPrice, Double maxPrice) {
    return bookRepository.findByAuthorAndPriceBetween(author, minPrice, maxPrice);
  }



  public List<Book> findByCategory(Category category) {
    return bookRepository.findByCategory(category);
  }

  public List<Book> findByCategoryAndPriceBetween(Category category, Double minPrice,
      Double maxPrice) {
    return bookRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
  }

  public Book findById(Long id) {
    Optional<Book> optional = bookRepository.findById(id);
    Book book = null;
    if (optional.isPresent())
      book = optional.get();
    else
      throw new RuntimeException("Category not found for name :: " + id);
    return book;
  }

  public List<Book> findByOrderByPriceAsc() {
    return bookRepository.findByOrderByPriceAsc();
  }

  public List<Book> findByPriceBetween(double minPrice, double maxPrice) {
    return bookRepository.findByPriceBetween(minPrice, maxPrice);
  }

  public Page<Book> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable) {
    return bookRepository.findByPriceBetween(minPrice, maxPrice, pageable);
  }

  public Book findByTitle(String title) {
    return bookRepository.findByTitle(title);
  }

  public List<Book> findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(
      String keyword) {
    return bookRepository
        .findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(keyword);
  }

  public List<Book> findByTitleIgnoreCaseContaining(String title) {
    return bookRepository.findByTitleIgnoreCaseContaining(title);
  }

  public Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable paging) {
    return bookRepository.findByTitleIgnoreCaseContaining(title, paging);
  }

  public List<Book> findLastEightBooks() {
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("createdAt").descending());
    Page<Book> bookPage = bookRepository.findAll(pageRequest);
    return bookPage.getContent();
  }

  public List<Book> findRecentSimilarBooksByCategory(Category category) {
    PageRequest pageRequest = PageRequest.of(0, 8);
    Page<Book> bookPage = bookRepository.findByCategoryRandom(category, pageRequest);
    return bookPage.getContent();
  }

  public List<Review> getAllReviewsForBook(Long bookId) {
    return reviewService.findByBook(bookId);
  }

  public List<Book> getAllSpecifications(String author, Long categoryId, Double minPrice,
      Double maxPrice) {
    return bookRepository.findAll(
        where(BookSpecifications.hasCategory(categoryId)).and(BookSpecifications.hasAuthor(author))
            .and(BookSpecifications.priceGreaterThanOrEqual(minPrice))
            .and(BookSpecifications.priceLessThanOrEqual(maxPrice)));

  }

  public Book save(Book book) {
    Book b = bookRepository.save(book);
    categoryService.updateNumberOfBooks();
    return b;
  }



}