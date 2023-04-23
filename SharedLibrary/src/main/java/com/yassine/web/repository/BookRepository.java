package com.yassine.web.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import jakarta.persistence.OrderBy;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
  @Query("SELECT b.author, COUNT(b) FROM Book b GROUP BY b.author ORDER BY b.author ASC")
  List<Object[]> countBooksByAuthor();


  long countByCategory(Category category);


  @Query("SELECT COUNT(r) FROM Review r WHERE r.book = :book")
  Long countReviewsByBook(@Param("book") Book book);

  @Modifying
  @Query(
      value = "DELETE b1 FROM Book b1, Book b2 WHERE b1.title = b2.title AND b1.book_id > b2.book_id",
      nativeQuery = true)
  void deleteDuplicateBooks();


  @Override
  @OrderBy("createdAt DESC")
  Page<Book> findAll(Pageable pageable);

  @Query("SELECT DISTINCT b.author FROM Book b")
  List<String> findAllAuthors();


  @Query("SELECT DISTINCT b.author FROM Book b ORDER BY b.author ASC")
  List<String> findAllAuthorsOrderedAsc();

  @Query("SELECT AVG(rating) FROM Review WHERE book.id = :bookId")
  Double findAverageRatingByBookId(@Param("bookId") Long bookId);

  List<Book> findByAuthorAndPriceBetween(String author, Double minPrice, Double maxPrice);

  Page<Book> findByAuthorAndPriceBetween(String author, Double minPrice, Double maxPrice,
      Pageable pageable);

  List<Book> findByCategory(Category category);

  Page<Book> findByCategory(Category category, Pageable pageable);

  List<Book> findByCategoryAndPriceBetween(Category category, Double minPrice, Double maxPrice);

  Page<Book> findByCategoryAndPriceBetween(Category category, Double minPrice, Double maxPrice,
      Pageable pageable);


  @Query(value = "SELECT b FROM Book b WHERE b.category = :category ORDER BY function('RAND') ")
  Page<Book> findByCategoryRandom(Category category, PageRequest pageRequest);

  List<Book> findByOrderByPriceAsc();


  List<Book> findByPriceBetween(double minPrice, double maxPrice);


  Page<Book> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);


  Optional<Book> findByTitle(String title);

  @Query(value = "Select b FROM Book b WHERE UPPER(b.title) LIKE CONCAT('%',UPPER(?1),'%')"
      + " OR UPPER(b.author) LIKE CONCAT('%',UPPER(?1),'%') "
      + "OR b.isbn LIKE CONCAT('%',?1,'%') ")
  List<Book> findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(String keyword);


  @Query(value = "Select b FROM Book b WHERE UPPER(b.title) LIKE CONCAT('%',UPPER(?1),'%')"
      + " OR UPPER(b.author) LIKE CONCAT('%',UPPER(?1),'%') "
      + "OR b.isbn LIKE CONCAT('%',?1,'%') ")
  Page<Book> findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(String keyword,
      Pageable pageable);


  List<Book> findByTitleIgnoreCaseContaining(String title);


  Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable paging);

  @Query("SELECT b FROM Book b WHERE UPPER(b.title) LIKE CONCAT('%',UPPER(?1),'%')")
  List<Book> search(String keyword);

  @Modifying
  @Query("UPDATE Book b SET b.rating = :rating WHERE b.id = :bookId")
  void updateRating(@Param("bookId") Long bookId, @Param("rating") Double averageRating);
}
