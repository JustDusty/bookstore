package com.yassine.web.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.yassine.web.model.Book;
import com.yassine.web.model.Category;
import jakarta.persistence.OrderBy;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
  @Query("SELECT b.author, COUNT(b) FROM Book b GROUP BY b.author ORDER BY b.author ASC")
  List<Object[]> countBooksByAuthor();


  long countByCategory(Category category);

  @Override
  @OrderBy("createdAt DESC")
  Page<Book> findAll(Pageable pageable);

  @Query("SELECT DISTINCT b.author FROM Book b")
  List<String> findAllAuthors();

  @Query("SELECT DISTINCT b.author FROM Book b ORDER BY b.author ASC")
  List<String> findAllAuthorsOrderedAsc();


  List<Book> findByAuthorAndPriceBetween(String author, Double minPrice, Double maxPrice);

  List<Book> findByCategory(Category category);

  Page<Book> findByCategory(Category category, Pageable pageable);

  List<Book> findByCategoryAndPriceBetween(Category category, Double minPrice, Double maxPrice);

  @Query(value = "SELECT b FROM Book b WHERE b.category = :category ORDER BY function('RAND') ")
  Page<Book> findByCategoryRandom(Category category, PageRequest pageRequest);

  List<Book> findByOrderByPriceAsc();

  List<Book> findByPriceBetween(double minPrice, double maxPrice);


  Page<Book> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

  Book findByTitle(String title);


  @Query(value = "Select b FROM Book b WHERE UPPER(b.title) LIKE CONCAT('%',UPPER(?1),'%')"
      + " OR UPPER(b.author) LIKE CONCAT('%',UPPER(?1),'%') "
      + "OR b.isbn LIKE CONCAT('%',?1,'%') ")
  List<Book> findByTitleContainingOrAuthorContainingOrIsbnContainingIgnoreCase(String keyword);


  List<Book> findByTitleIgnoreCaseContaining(String title);

  Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable paging);


  @Query("SELECT b FROM Book b WHERE UPPER(b.title) LIKE CONCAT('%',UPPER(?1),'%')")
  List<Book> search(String keyword);
}
